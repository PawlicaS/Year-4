package ie.szymon.rest.repos;

import ie.szymon.rest.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepo extends JpaRepository<Department, String> {
    List<Department> findAll();
}
