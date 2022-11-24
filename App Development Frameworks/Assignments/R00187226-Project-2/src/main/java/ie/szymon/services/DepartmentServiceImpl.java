package ie.szymon.services;

import ie.szymon.entities.Department;
import ie.szymon.repos.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentRepo departmentRepo;

    @Override
    public List<Department> listAll() {
        if (departmentRepo.findAll().size() == 0) {
            return null;
        }
        else {
            return departmentRepo.findAll();
        }
    }

    @Override
    public Department addDepartment(String title, String email) {
        Department department = new Department(title, email);
        if (departmentRepo.existsById(title))
            return null;
        else {
            return departmentRepo.save(department);
        }
    }
}
