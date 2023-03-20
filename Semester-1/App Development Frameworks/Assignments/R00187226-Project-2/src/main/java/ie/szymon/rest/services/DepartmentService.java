package ie.szymon.rest.services;

import ie.szymon.rest.entities.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> listAll();

    Department findDepartment(String title);
    Department addDepartment(String title, String email);
}
