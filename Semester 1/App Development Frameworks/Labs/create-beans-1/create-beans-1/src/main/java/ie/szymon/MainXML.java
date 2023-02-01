package ie.szymon;

import ie.szymon.entities.CartoonCharacter;
import ie.szymon.entities.Department;
import ie.szymon.entities.Studio;
import ie.szymon.entities.School;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Locale;

public class MainXML {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println(context.getBean("disney", Studio.class));
        System.out.println(context.getBean("paramount", Studio.class));
        context.getBeansOfType(Studio.class).values().forEach(System.out::println);
        context.getBeansOfType(School.class).values().forEach(System.out::println);
        context.getBeansOfType(CartoonCharacter.class).values().forEach(System.out::println);
        context.getBeansOfType(Department.class).values().forEach(System.out::println);

        System.out.println(context.getMessage("greeting", null, Locale.ITALIAN));
        System.out.println(context.getMessage("greeting", null, Locale.FRENCH));
        System.out.println(context.getMessage("greeting", null, Locale.getDefault()));
    }
}