package my.project.controllers;

import my.project.models.User;
import my.project.services.OrderService;
import my.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
public class ProfileController {

	private final UserService userService;
	private final OrderService orderService;

	@Autowired
	public ProfileController(UserService userService, OrderService orderService) {
		this.userService = userService;
		this.orderService = orderService;
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", currentUser);
		return "profile/profile";
	}

	@GetMapping("/profile/all")
	public String profileAll(Model model) {
		model.addAttribute("users", userService.findAll());
		return "profile/all-profiles";
	}

	@GetMapping("/login")
	public String loginForm(@ModelAttribute("user") User user) {
		return "profile/login";
	}

	@GetMapping("/register")
	public String registerForm(@ModelAttribute("user") User user) {
		return "profile/register";
	}


	@PostMapping("/login")
	public String login(@ModelAttribute("user") User user, Errors errors) {
		if (userService.login(user.getEmail(), user.getPassword()).equals("OK")) {
			return "redirect:/profile";
		} else {
			errors.rejectValue("email", "email.invalid");
			return "profile/login";
		}
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user) {
		return userService.register(user) ? "redirect:/profile" : "redirect:/login";
	}

	@GetMapping("/exit")
	public String exit() {
		userService.exit();
		orderService.clearCurrentOrder();
		return "redirect:/winedise";
	}

	@GetMapping("/edit-profile")
	public String edit(Model model) {
		model.addAttribute("user", userService.getCurrentUser());
		return "profile/edit-profile";
	}

	@PatchMapping("/edit-profile")
	public String edit(@ModelAttribute("user") User user) {
		userService.edit(user);
		return "redirect:/profile";
	}

	@PatchMapping("/profile/make-admin/{id}")
	public String makeAdmin(@PathVariable("id") int id) {
		userService.makeAdmin(id);
		return "redirect:/profile/all";
	}

	@GetMapping("/delete-profile")
	public String delete() {
		userService.delete();
		return "redirect:/winedise";
	}

}
