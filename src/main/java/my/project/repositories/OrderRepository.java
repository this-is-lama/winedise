package my.project.repositories;

import my.project.models.Order;
import my.project.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByRestaurantAndDate(Restaurant restaurant, LocalDate date);

	List<Order> findAllByOrderByIdDesc();
}
