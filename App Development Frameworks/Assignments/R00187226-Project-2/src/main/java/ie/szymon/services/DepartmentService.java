package ie.szymon.services;

import ie.szymon.entities.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> listAll();
    Department addDepartment(String title, String email);
}
