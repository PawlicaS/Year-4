package ie.szymon;

import ie.szymon.service.SchoolService;
import ie.szymon.service.SchoolServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        SchoolService schoolService = context.getBean(SchoolServiceImpl.class);
        schoolService.findAll().forEach(System.out::println);
        System.out.println(schoolService.findASchool(1));
    }
}
