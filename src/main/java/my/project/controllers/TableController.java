package my.project.controllers;

import my.project.models.Tables;
import my.project.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TableController {

	private final TableService tableService;

	@Autowired
	public TableController(TableService tableService) {
		this.tableService = tableService;
	}

	@GetMapping("/rest/{rest_id}/add-table")
	public String addTable(@PathVariable("rest_id") int restId,
						  @ModelAttribute("table") Tables table,
						  Model model) {
		model.addAttribute("rest_id", restId);
		return "tables/add-table";
	}

	@PostMapping("/rest/{rest_id}/add-table")
	public String addTable(@ModelAttribute("table") Tables table,
						  @PathVariable("rest_id") int restId) {
		tableService.addTable(table, restId);
		return "redirect:/winedise/" + restId + "#tables";
	}


	@GetMapping("/rest/{rest_id}/edit-table/{id}")
	public String editDish(@PathVariable("rest_id") int restId,
						   @PathVariable("id") int id,
						   Model model) {
		model.addAttribute("table", tableService.findById(id));
		model.addAttribute("rest_id", restId);
		return "tables/edit-table";
	}

	@PatchMapping("/rest/{rest_id}/edit-table/{id}")
	public String editDish(@ModelAttribute("table") Tables table,
						   @PathVariable("rest_id") int restId,
						   @PathVariable("id") int id) {
		tableService.editTable(table, id);
		return "redirect:/winedise/" + restId + "#tables";
	}

	//удаление
	@GetMapping("/rest/{rest_id}/delete-table/{id}")
	public String deleteDish(@PathVariable("rest_id") int restId,
							 @PathVariable("id") int id) {
		tableService.deleteTable(id);
		return "redirect:/winedise/" + restId + "#tables";
	}
}
