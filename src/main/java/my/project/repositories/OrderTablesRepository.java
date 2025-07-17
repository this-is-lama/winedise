package my.project.repositories;

import my.project.models.OrderTables;
import my.project.models.OrderTablesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTablesRepository extends JpaRepository<OrderTables, OrderTablesId> {

}
