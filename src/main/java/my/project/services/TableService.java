package my.project.services;

import my.project.models.Tables;
import my.project.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class TableService {

	public final TableRepository tableRepository;

	private final RestaurantService restaurantService;

	@Autowired
	public TableService(TableRepository tableRepository, RestaurantService restaurantService) {
		this.tableRepository = tableRepository;
		this.restaurantService = restaurantService;
	}

	public Tables findById(int id) {
		return tableRepository.findById(id);
	}


	@Transactional
	public void addTable(Tables newTable, int RestId) {
		Tables table = restaurantService.addTable(newTable, RestId);
		if (table != null) {
			newTable.setIsDeleted(false);
			tableRepository.save(newTable);
		}
	}

	@Transactional
	public void editTable(Tables newTable, int id) {
		Tables table = tableRepository.findById(id);
		if (table != null) {
			table.setName(newTable.getName());
			table.setDescription(newTable.getDescription());
			table.setCapacity(newTable.getCapacity());
			tableRepository.save(table);
		}
	}

	@Transactional
	public void deleteTable(int tableId) {
		Tables table = tableRepository.findById(tableId);
		if (table != null) {
			table.setIsDeleted(true);
		}
	}
}
