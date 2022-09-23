import ie.szymon.entities.Faculty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/beans.xml"})
public class TestFacultyComponentScan {
    @Autowired
    ApplicationContext context;

    @Test
    public void TestFacultyBean() {
        Faculty faculty = context.getBean(Faculty.class);
        Assertions.assertNotNull(faculty);
    }

    @Test
    public void TestFacultyBeanName() {
        Faculty faculty = context.getBean(Faculty.class);
        Assertions.assertEquals("Engineering and Science", faculty.getName());
    }

    @Test
    public void TestFacultyBeanPhone() {
        Faculty faculty = context.getBean(Faculty.class);
        Assertions.assertEquals("021 433 5450",  faculty.getPhone());
    }
}
