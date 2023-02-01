package ie.szymon;

import ie.szymon.entities.CartoonCharacter;
import ie.szymon.entities.Department;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Locale;

public class MainJava {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        context.getBeansOfType(CartoonCharacter.class).values().forEach(System.out::println);
        context.getBeansOfType(Department.class).values().forEach(System.out::println);
        System.out.println(context.getMessage("greeting", null, Locale.ITALIAN));
        System.out.println(context.getMessage("greeting", null, Locale.FRENCH));
        System.out.println(context.getMessage("greeting", null, Locale.getDefault()));
    }
}
