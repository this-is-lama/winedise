package my.project.repositories;

import my.project.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

	Restaurant findById(int id);

	Set<Restaurant> findByNameContainingIgnoreCase(String name);
}
