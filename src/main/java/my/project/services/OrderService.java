package my.project.services;

import lombok.Getter;
import my.project.models.*;
import my.project.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserService userService;
	private final DishService dishService;
	private final TableService tableService;
	private final RestaurantService restaurantService;


	@Getter
	private Order currentOrder = null;

	private Integer currentRestaurant = null;

	@Getter
	private final Map<Dish, Integer> dishes = new HashMap<>();

	@Getter
	private final Set<Tables> tables = new HashSet<>();

	@Autowired
	public OrderService(OrderRepository orderRepository,
						UserService userService,
						DishService dishService,
						TableService tableService,
						RestaurantService restaurantService) {
		this.orderRepository = orderRepository;
		this.userService = userService;
		this.dishService = dishService;
		this.tableService = tableService;
		this.restaurantService = restaurantService;

	}

	public List<Order> findAll() {
		return orderRepository.findAllByOrderByIdDesc();
	}

	private boolean canCreateOrder(Integer peopleCount) {
		if (peopleCount == null || peopleCount <= 0) {
			return false;
		}
		if (currentRestaurant == null || currentOrder == null) {
			return false;
		}
		if (dishes.isEmpty() && tables.isEmpty()) {
			return false;
		}

		int totalCapacity = tables.stream()
				.mapToInt(Tables::getCapacity)
				.sum();
		if (peopleCount > totalCapacity) {
			return false;
		}
		return restaurantService.findById(currentRestaurant) != null;
	}

	@Transactional
	public boolean saveCurrentOrder(Integer count, String specialRequests) {
		if (!canCreateOrder(count)) {
			return false;
		}
		User user = userService.getCurrentUser();
		Restaurant restaurant = restaurantService.findById(currentRestaurant);
		currentOrder.setUser(user);
		currentOrder.setRestaurant(restaurant);
		currentOrder.setPeopleCount(count);
		currentOrder.setSpecialRequests(specialRequests);
		currentOrder.setTotalAmount(BigDecimal.valueOf(getTotalAmount()));
		currentOrder.setRestaurantTables(tables);

		Set<OrderDishes> orderDishes = new HashSet<>();

		for (var entry : dishes.entrySet()) {

			OrderDishesId orderDishesId = new OrderDishesId();
			orderDishesId.setDishId(entry.getKey().getId());
			orderDishesId.setOrderId(currentOrder.getId());

			OrderDishes orderDish = new OrderDishes();
			orderDish.setOrder(currentOrder);
			orderDish.setDish(entry.getKey());
			orderDish.setQuantity(entry.getValue());
			orderDish.setId(orderDishesId);

			orderDishes.add(orderDish);
		}

		currentOrder.setOrderDishes(orderDishes);
		currentOrder.setRestaurantTables(tables);

		currentOrder = orderRepository.save(currentOrder);

		for (OrderDishes orderDish : orderDishes) {
			orderDish.getId().setOrderId(currentOrder.getId());
		}

		return true;
	}


	@Transactional
	public void clearCurrentOrder() {
		this.currentOrder = null;
		this.currentRestaurant = null;
		this.dishes.clear();
		this.tables.clear();
	}

	@Transactional
	public void deleteOrder(int id) {
		orderRepository.deleteById(id);
	}

	@Transactional
	public void addDishToOrder(int id) {
		Dish dish = dishService.findById(id);
		if (dish != null) {
			if (currentRestaurant == null) {
				this.currentRestaurant = dish.getRestaurant().getId();
				dishes.merge(dish, 1, Integer::sum);
			} else if (currentRestaurant.equals(dish.getRestaurant().getId())) {
				dishes.merge(dish, 1, Integer::sum);
			}
		}
	}

	@Transactional
	public void deleteDishFromOrder(int id) {
		Dish dish = dishService.findById(id);
		if (dish != null && dishes.containsKey(dish)) {
			int newCount = dishes.get(dish) - 1;
			if (newCount == 0) {
				dishes.remove(dish);
			} else {
				dishes.put(dish, newCount);
			}
		}
		if (dishes.isEmpty() && tables.isEmpty()) {
			this.currentRestaurant = null;
		}
	}

	@Transactional
	public void fullDeleteDishFromOrder(int id) {
		Dish dish = dishService.findById(id);
		if (dish != null) {
			dishes.remove(dish);
		}
		if (dishes.isEmpty() && tables.isEmpty()) {
			this.currentRestaurant = null;
		}
	}

	@Transactional
	public void addTableToOrder(int id, LocalDate date, LocalTime start, LocalTime end, Integer count) {
		if (date == null || start == null || end == null || count == null) {
			return;
		}
		Tables table = tableService.findById(id);
		if (table != null) {
			if (currentRestaurant == null) {
				this.currentRestaurant = table.getRestaurant().getId();
				if (currentOrder == null) {
					currentOrder = new Order(date, start, end, count);
					tables.add(table);
				} else if (currentOrder.getDate().equals(date)) {
					tables.add(table);
				}
			} else if (currentRestaurant.equals(table.getRestaurant().getId())) {
				if (currentOrder == null) {
					currentOrder = new Order(date, start, end, count);
					tables.add(table);
				} else if (currentOrder.getDate().equals(date)) {
					tables.add(table);
				}
			}
		}
	}

	@Transactional
	public void deleteTableFromOrder(int id) {
		Tables table = tableService.findById(id);
		if (table != null) {
			tables.remove(table);
		}
		if (dishes.isEmpty() && tables.isEmpty()) {
			this.currentRestaurant = null;
		}
	}

	public Restaurant getCurrentRestaurant() {
		if (currentRestaurant != null) {
			return restaurantService.findById(currentRestaurant);
		}
		return null;
	}

	public int getTotalAmount() {
		int sum = 0;
		for (var dish : dishes.entrySet()) {
			sum += dish.getValue() * dish.getKey().getPrice();
		}
		return sum;
	}


}
