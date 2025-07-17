package my.project.repositories;

import my.project.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

	Dish findById(int id);

	List<Dish> findByRestaurantIdAndIsDeletedFalse(int restaurantId);

	@Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId " +
			"AND d.isDeleted = false " +
			"AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')))")
	List<Dish> searchByRestaurantIdAndQuery(@Param("restaurantId") int restaurantId,
											@Param("query") String query);

}
