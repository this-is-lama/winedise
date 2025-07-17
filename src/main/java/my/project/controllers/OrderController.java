package my.project.controllers;

import my.project.services.OrderService;
import my.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/order")
public class OrderController {

	private final UserService userService;
	private final OrderService orderService;

	@Autowired
	public OrderController(UserService userService, OrderService orderService) {
		this.userService = userService;
		this.orderService = orderService;
	}

	@GetMapping()
	public String order(Model model) {
		model.addAttribute("rest", orderService.getCurrentRestaurant());
		model.addAttribute("user", userService.getCurrentUser());
		model.addAttribute("dishes", orderService.getDishes());
		model.addAttribute("tables", orderService.getTables());
		model.addAttribute("order", orderService.getCurrentOrder());
		model.addAttribute("total", orderService.getTotalAmount());
		return "order/order";
	}

	@PostMapping("/confirm")
	public String confirm(@RequestParam("peopleCount") Integer peopleCount,
						  @RequestParam("specialRequests") String specialRequests,
						  Model model) {
		if (userService.getCurrentUser() == null) {
			return "redirect:/login";
		}
		boolean success = orderService.saveCurrentOrder(peopleCount, specialRequests);
		orderService.clearCurrentOrder();
		model.addAttribute("success", success);
		model.addAttribute("rest", orderService.getCurrentRestaurant());
		model.addAttribute("user", userService.getCurrentUser());
		model.addAttribute("dishes", orderService.getDishes());
		model.addAttribute("tables", orderService.getTables());
		model.addAttribute("order", orderService.getCurrentOrder());
		model.addAttribute("total", orderService.getTotalAmount());
		return "order/order";
	}

	@GetMapping("/all")
	public String allOrders(Model model) {
		model.addAttribute("allOrders", orderService.findAll());
		return "order/all-orders";
	}

	@DeleteMapping("/delete/{id}/from-all")
	public String deleteFromAlls(@PathVariable("id") int id) {
		orderService.deleteOrder(id);
		return "redirect:/order/all";
	}

	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		orderService.deleteOrder(id);
		return "redirect:/profile#orders";
	}

	@PostMapping("/rest/{rest_id}/add-dish/{id}/from-menu")
	public String addDishToOrderFromMenu(@PathVariable("id") int id,
								 @PathVariable("rest_id") int restId) {
		orderService.addDishToOrder(id);
		return "redirect:/winedise/" + restId + "#menu";
	}

	@PostMapping("/rest/{rest_id}/add-dish/{id}/from-dish")
	public String addDishToOrderFromDish(@PathVariable("id") int id,
								 @PathVariable("rest_id") int restId) {
		orderService.addDishToOrder(id);
		return "redirect:/rest/" + restId + "/dish/" + id;
	}

	@PostMapping("/add-dish/{id}/from-order")
	public String addDishToOrderFromOrder(@PathVariable("id") int id) {
		orderService.addDishToOrder(id);
		return "redirect:/order";
	}

	@DeleteMapping("/rest/{rest_id}/delete-dish/{id}/from-dish")
	public String deleteDishFromDish(@PathVariable("id") int id,
								  @PathVariable("rest_id") int restId) {
		orderService.deleteDishFromOrder(id);
		return "redirect:/rest/" + restId + "/dish/" + id;
	}

	@DeleteMapping("/delete-dish/{id}/from-order")
	public String deleteDishFromOrder(@PathVariable("id") int id) {
		orderService.deleteDishFromOrder(id);
		return "redirect:/order";
	}

	@DeleteMapping("/delete-dish/{id}/full")
	public String fullDeleteDishFromOrder(@PathVariable("id") int id) {
		orderService.fullDeleteDishFromOrder(id);
		return "redirect:/order";
	}

	@PostMapping("/rest/{rest_id}/add-table/{id}")
	public String addTableToOrder(@RequestParam(required = false, name = "date") LocalDate date,
								  @RequestParam(required = false, name = "start") LocalTime start,
								  @RequestParam(required = false, name = "end") LocalTime end,
								  @RequestParam(required = false, name = "count") Integer count,
								  @PathVariable("id") int id,
								  @PathVariable("rest_id") int restId) {
		orderService.addTableToOrder(id, date, start, end, count);
		return "redirect:/winedise/" + restId + "#tables";
	}

	@DeleteMapping("/delete-table/{id}")
	public String deleteTableFromOrder(@PathVariable("id") int id) {
		orderService.deleteTableFromOrder(id);
		return "redirect:/order";
	}
}
