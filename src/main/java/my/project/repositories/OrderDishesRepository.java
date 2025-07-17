package my.project.repositories;

import my.project.models.OrderDishes;
import my.project.models.OrderDishesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDishesRepository extends JpaRepository<OrderDishes, OrderDishesId> {

}