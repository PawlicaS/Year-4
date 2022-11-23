package ie.szymon.repos;

import ie.szymon.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepo extends JpaRepository<Department, String> {
    List<Department> findAll();
    Optional<Department> findByDepartmentTitle(String departmentTitle);
}
