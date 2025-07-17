package my.project.repositories;

import my.project.models.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TableRepository extends JpaRepository<Tables, Integer> {

	Tables findById(int id);

}
