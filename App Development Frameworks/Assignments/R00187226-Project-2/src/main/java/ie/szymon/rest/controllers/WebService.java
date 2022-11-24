package ie.szymon.rest.controllers;

import ie.szymon.entities.Department;
import ie.szymon.entities.Office;
import ie.szymon.repos.DepartmentRepo;
import ie.szymon.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebService {
    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private OfficeRepo officeRepo;

    @GetMapping("/departments")
    List<Department> getAll(){
        return departmentRepo.findAll();
    }

    @GetMapping("/offices")
    List<Office> getAllOffices(){
        return officeRepo.findAll();
    }
}
