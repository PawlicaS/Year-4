package ie.szymon.rest;

import ie.szymon.rest.entities.Department;
import ie.szymon.rest.entities.Office;
import ie.szymon.rest.repos.DepartmentRepo;
import ie.szymon.rest.repos.OfficeRepo;
import ie.szymon.rest.services.DepartmentService;
import ie.szymon.rest.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    OfficeRepo officeRepo;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    OfficeService officeService;

    @Override
    public void run(String... args) throws Exception {
        Department d1 = departmentRepo.save(new Department("Computer Science", "computercience@mtu.ie"));
        Office o1 = officeRepo.save(new Office(101, 5, 0, d1));
        Office o2 = officeRepo.save(new Office(102, 6, 3, d1));
        Office o3 = officeRepo.save(new Office(103, 7, 2, d1));

        Department d2 = departmentRepo.save(new Department("Construction", "construction@mtu.ie"));
        Office o4 = officeRepo.save(new Office(111, 5, 2, d2));
        Office o5 = officeRepo.save(new Office(112, 6, 5, d2));

        Department d3 = departmentRepo.save(new Department("Business", "business@mtu.ie"));
        Office o6 = officeRepo.save(new Office(113, 7, 0, d3));
        Office o7 = officeRepo.save(new Office(121, 5, 4, d3));

        Department d4 = departmentRepo.save(new Department("Electrical Engineering", "electricalengineering@mtu.ie"));
        Office o8 = officeRepo.save(new Office(122, 6, 1, d4));
    }
}
