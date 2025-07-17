package my.project.services;

import my.project.models.Dish;
import my.project.models.Order;
import my.project.models.Restaurant;
import my.project.models.Tables;
import my.project.repositories.DishRepository;
import my.project.repositories.OrderRepository;
import my.project.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final DishRepository dishRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public RestaurantService(RestaurantRepository restaurantRepository, DishRepository dishRepository, OrderRepository orderRepository) {
		this.restaurantRepository = restaurantRepository;
		this.dishRepository = dishRepository;
		this.orderRepository = orderRepository;
	}

	public List<Restaurant> findAll() {
		return restaurantRepository.findAll().stream()
				.filter(restaurant -> !restaurant.getIsDeleted())
				.collect(Collectors.toList());
	}

	public List<Restaurant> searchByName(String searchQuery) {
		if (searchQuery == null || searchQuery.isEmpty()) {
			return findAll();
		}
		return restaurantRepository.findByNameContainingIgnoreCase(searchQuery)
				.stream()
				.filter(restaurant -> !restaurant.getIsDeleted())
				.collect(Collectors.toList());
	}

	public Restaurant findById(int id) {
		return restaurantRepository.findById(id);
	}

	public Map<String, Set<Dish>> getMenu(int id) {
		return dishRepository.findByRestaurantIdAndIsDeletedFalse(id).stream()
				.collect(Collectors.groupingBy(
						Dish::getCategory,
						HashMap::new,
						Collectors.toSet()
				));
	}

	public Map<String, Set<Dish>> getMenu(int id, String searchQuery) {
		if (searchQuery == null || searchQuery.trim().isEmpty()) {
			return getMenu(id);
		}
		return dishRepository.searchByRestaurantIdAndQuery(id, searchQuery).stream()
				.collect(Collectors.groupingBy(
						Dish::getCategory,
						HashMap::new,
						Collectors.toSet()
				));
	}

	public Set<Tables> getTables(int id) {
		Restaurant restaurant = restaurantRepository.findById(id);
		return restaurant.getTables().stream()
				.filter(tables -> !tables.getIsDeleted())
				.collect(Collectors.toSet());
	}

	public Set<Tables> getTables(int id, LocalDate date, LocalTime start, LocalTime end, Integer count) {
		if (date == null || start == null || end == null || count == null) {
			return new HashSet<>();
		}
		Restaurant restaurant = restaurantRepository.findById(id);
		List<Order> ordersForDate = orderRepository.findByRestaurantAndDate(restaurant, date);

		return restaurant.getTables().stream()
				.filter(table ->
						!table.getIsDeleted() &&
								table.getCapacity() >= count &&
								ordersForDate.stream().noneMatch(order ->
										order.getRestaurantTables().contains(table) &&
												isTimeOverlapping(
														order.getReservationStart(),
														order.getReservationEnd(),
														start,
														end
												)
								)
				)
				.collect(Collectors.toSet());

	}

	private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
		return !start1.isAfter(end2) && !start2.isAfter(end1);
	}

	@Transactional
	public void save(Restaurant restaurant) {
		restaurant.setIsDeleted(false);
		restaurantRepository.save(restaurant);
	}

	@Transactional
	public void edit(Restaurant newRestaurant, int id) {
		Restaurant restaurant = restaurantRepository.findById(id);
		if (restaurant != null) {
			restaurant.setName(newRestaurant.getName());
			restaurant.setOpeningTime(newRestaurant.getOpeningTime());
			restaurant.setClosingTime(newRestaurant.getClosingTime());
			restaurant.setPhoto(newRestaurant.getPhoto());
			restaurant.setDescription(newRestaurant.getDescription());
			restaurant.setAddress(newRestaurant.getAddress());
			restaurant.setAdminPhone(newRestaurant.getAdminPhone());
			restaurant.setFloorPlan(newRestaurant.getFloorPlan());
			restaurantRepository.save(restaurant);
		}
	}

	@Transactional
	public Tables addTable(Tables table, int restId) {
		Restaurant restaurant = restaurantRepository.findById(restId);
		if (restaurant != null) {
			restaurant.getTables().add(table);
			table.setRestaurant(restaurant);
			return table;
		}
		return null;
	}

	@Transactional
	public Dish addDish(Dish dish, int restId) {
		Restaurant restaurant = restaurantRepository.findById(restId);
		if (restaurant != null) {
			restaurant.getDishes().add(dish);
			dish.setRestaurant(restaurant);
			return dish;
		}
		return null;
	}


	@Transactional
	public void delete(int id) {
		Restaurant restaurant = restaurantRepository.findById(id);
		if (restaurant != null) {
			restaurant.setIsDeleted(true);
		}
	}

}
