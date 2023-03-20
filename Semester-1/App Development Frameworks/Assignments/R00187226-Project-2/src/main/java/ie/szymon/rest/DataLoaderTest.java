package ie.szymon.rest;

import ie.szymon.rest.entities.Department;
import ie.szymon.rest.entities.Office;
import ie.szymon.rest.repos.DepartmentRepo;
import ie.szymon.rest.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class DataLoaderTest implements CommandLineRunner {
    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    OfficeRepo officeRepo;

    @Override
    public void run(String... args) throws Exception {
        Department d1 = departmentRepo.save(new Department("Test1", "test1@mtu.ie"));
        Department d2 = departmentRepo.save(new Department("Test2", "test2@mtu.ie"));
        Department d3 = departmentRepo.save(new Department("Test3", "test3@mtu.ie"));
        Department d4 = departmentRepo.save(new Department("Test4", "test4@mtu.ie"));

        Office o1 = officeRepo.save(new Office(121, 5, 5, d1));
        Office o2 = officeRepo.save(new Office(122, 4, 5, d1));
        Office o3 = officeRepo.save(new Office(123, 3, 5, d2));
        Office o4 = officeRepo.save(new Office(124, 4, 5, d3));
    }
}
