package ie.szymon;

import ie.szymon.entities.Department;
import ie.szymon.entities.Office;
import ie.szymon.repos.DepartmentRepo;
import ie.szymon.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    OfficeRepo officeRepo;

    @Override
    public void run(String... args) throws Exception {
        Department d2 = new Department("test55", "test5@mtu.ie");
        Department d1 = departmentRepo.save(new Department("Test5", "test5@mtu.ie"));
        departmentRepo.save(new Department("Test1", "test1@mtu.ie"));
        departmentRepo.save(new Department("Test2", "test2@mtu.ie"));
        departmentRepo.save(new Department("Test3", "test3@mtu.ie"));

        officeRepo.save(new Office(123, 30, 0, d1));
        officeRepo.save(new Office(124, 37, 36, d1));
        officeRepo.save(new Office(120, 20, 0, d1));
        officeRepo.save(new Office(122, 20, 20, d1));

        System.out.println(departmentRepo.findByTitle("Test1"));
        System.out.println(departmentRepo.findByTitle("test55").isEmpty());
//        departmentRepo.findAll().forEach(System.out::println);
//        System.out.println(departmentRepo.findByTitle("Test2"));
//        officeRepo.findAll().forEach(System.out::println);
//        System.out.println();
//        officeRepo.findAllEmptyOffices().forEach(System.out::println);
//        System.out.println();
//        officeRepo.findAllNotFullOffices().forEach(System.out::println);
//        System.out.println();
//        officeRepo.findAllByDepartment_Title("Test2").forEach(System.out::println);
//        System.out.println();
//        officeRepo.findAllByDepartment_Title("Test5").forEach(System.out::println);
//        System.out.println();
//        officeRepo.updateOfficeCurrOccupancy(19, 120);
//        officeRepo.findAll().forEach(System.out::println);
//        System.out.println();
//        officeRepo.findAllEmptyOffices().forEach(System.out::println);
//        System.out.println(d1);
    }
}
