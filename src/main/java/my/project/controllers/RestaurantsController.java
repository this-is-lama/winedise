package my.project.controllers;

import my.project.models.Restaurant;
import my.project.services.RestaurantService;
import my.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Controller
@RequestMapping("/winedise")
public class RestaurantsController {

	private final RestaurantService restaurantService;
	private final UserService userService;

	@Autowired
	public RestaurantsController(RestaurantService restaurantService, UserService userService) {
		this.restaurantService = restaurantService;
		this.userService = userService;
	}

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("restaurants", restaurantService.findAll());
		model.addAttribute("user", userService.getCurrentUser());
		return "restaurants/main";
	}

	@GetMapping("/{id}")
	public String restaurant(@PathVariable int id, Model model) {
		model.addAttribute("restaurant", restaurantService.findById(id));
		model.addAttribute("menu", restaurantService.getMenu(id));
		model.addAttribute("tables", restaurantService.getTables(id));
		model.addAttribute("user", userService.getCurrentUser());
		return "restaurants/restaurant";
	}

	@GetMapping("/table/{id}")
	public String tableSearch(@RequestParam(required = false, name = "date") LocalDate date,
							 @RequestParam(required = false, name = "start") LocalTime start,
							 @RequestParam(required = false, name = "end") LocalTime end,
							 @RequestParam(required = false, name = "count") Integer count,
							 @PathVariable int id, Model model) {
		model.addAttribute("restaurant", restaurantService.findById(id));
		model.addAttribute("menu", restaurantService.getMenu(id));
		model.addAttribute("tables", restaurantService.getTables(id, date, start, end, count));
		model.addAttribute("user", userService.getCurrentUser());
		return "restaurants/restaurant";
	}

	@GetMapping("/{id}/search")
	public String menuSearch(@RequestParam(required = false, name = "dishSearchQuery") String searchQuery,
							 Model model, @PathVariable int id) {
		model.addAttribute("restaurant", restaurantService.findById(id));
		model.addAttribute("menu", restaurantService.getMenu(id, searchQuery));
		model.addAttribute("tables", restaurantService.getTables(id));
		model.addAttribute("user", userService.getCurrentUser());
		return "restaurants/restaurant";
	}

	@GetMapping("/search")
	public String restSearch(@RequestParam(required = false) String searchQuery, Model model) {
		model.addAttribute("restaurants", restaurantService.searchByName(searchQuery));
		return "restaurants/main";
	}

	@GetMapping("/add-rest")
	public String addRest(@ModelAttribute("rest") Restaurant restaurant) {
		return "restaurants/add-restaurant";
	}

	@PostMapping("/add-rest")
	public String addRestaurant(@ModelAttribute("rest") Restaurant restaurant) {
		restaurantService.save(restaurant);
		return "redirect:/winedise#rests";
	}

	@GetMapping("/edit-rest/{id}")
	public String editRest(@PathVariable("id") int id, Model model) {
		model.addAttribute("rest", restaurantService.findById(id));
		return "restaurants/edit-restaurant";
	}

	@PatchMapping("/edit-rest/{id}")
	public String editRestaurant(@ModelAttribute("rest") Restaurant restaurant,
								 @PathVariable("id") int id) {
		restaurantService.edit(restaurant, id);
		return "redirect:/winedise#rests";
	}

	@GetMapping("/delete-rest/{id}")
	public String deleteRestaurant(@PathVariable("id") int id) {
		restaurantService.delete(id);
		return "redirect:/winedise#rests";
	}
}
