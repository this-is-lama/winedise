package my.project.controllers;

import my.project.models.Dish;
import my.project.services.DishService;
import my.project.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DishController {

	private final DishService dishService;
	private final OrderService orderService;

	@Autowired
	public DishController(DishService dishService, OrderService orderService) {
		this.dishService = dishService;
		this.orderService = orderService;
	}

	@GetMapping("/rest/{rest_id}/dish/{id}")
	public String dish(@PathVariable(name = "id") int id,
					   @PathVariable("rest_id") int restId,
					   Model model) {
		model.addAttribute("dish", dishService.findById(id));
		model.addAttribute("dishes", orderService.getDishes());
		model.addAttribute("rest_id", restId);
		return "menu/dish";
	}

	@GetMapping("/rest/{rest_id}/add-dish")
	public String addDish(@ModelAttribute("dish") Dish dish,
						  @PathVariable("rest_id") int restId,
						  Model model) {
		model.addAttribute("rest_id", restId);
		return "menu/add-dish";
	}

	@PostMapping("/rest/{rest_id}/add-dish")
	public String addDish(@ModelAttribute("dish") Dish dish,
						  @PathVariable("rest_id") int restId) {
		dishService.addDish(dish, restId);
		return "redirect:/winedise/" + restId + "#menu";
	}


	@GetMapping("/rest/{rest_id}/edit-dish/{id}")
	public String editDish(@PathVariable("rest_id") int restId,
						   @PathVariable("id") int id,
						   Model model) {
		model.addAttribute("dish", dishService.findById(id));
		model.addAttribute("rest_id", restId);
		return "menu/edit-dish";
	}

	@PatchMapping("/rest/{rest_id}/edit-dish/{id}")
	public String editDish(@ModelAttribute("dish") Dish dish,
						   @PathVariable("rest_id") int restId,
						   @PathVariable("id") int id) {
		dishService.editDish(dish, id);
		return "redirect:/winedise/" + restId + "#menu";
	}


	@GetMapping("/rest/{rest_id}/delete-dish/{id}")
	public String deleteDish(@PathVariable("rest_id") int restId,
							 @PathVariable(name = "id") int id) {
		dishService.delete(id);
		return "redirect:/winedise/" + restId + "#menu";
	}
}
