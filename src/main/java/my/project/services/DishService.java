package my.project.services;

import my.project.models.Dish;
import my.project.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class DishService {

	private final DishRepository dishRepository;

	private final RestaurantService restaurantService;

	@Autowired
	public DishService(DishRepository dishRepository, RestaurantService restaurantService) {
		this.dishRepository = dishRepository;
		this.restaurantService = restaurantService;
	}

	public Dish findById(int id) {
		return dishRepository.findById(id);
	}


	@Transactional
	public void addDish(Dish newDish, int restId) {
		Dish dish = restaurantService.addDish(newDish, restId);
		if (dish != null) {
			dish.setIsDeleted(false);
			dishRepository.save(dish);
		}
	}

	@Transactional
	public void editDish(Dish newDish, int id) {
		Dish dish = dishRepository.findById(id);
		if (dish != null) {
			dish.setName(newDish.getName());
			dish.setPrice(newDish.getPrice());
			dish.setPhoto(newDish.getPhoto());
			dish.setCategory(newDish.getCategory());
			dish.setWeightGrams(newDish.getWeightGrams());
			dish.setDescription(newDish.getDescription());
			dish.setIngredients(newDish.getIngredients());
			dish.setCalories(newDish.getCalories());
			dish.setProteins(newDish.getProteins());
			dish.setFats(newDish.getFats());
			dish.setCarbohydrates(newDish.getCarbohydrates());
			dish.setPreparationTimeMinutes(newDish.getPreparationTimeMinutes());
			dishRepository.save(dish);
		}
	}

	@Transactional
	public void delete(int id) {
		Dish dish = dishRepository.findById(id);
		if (dish != null) {
			dish.setIsDeleted(true);
		}
	}
}
