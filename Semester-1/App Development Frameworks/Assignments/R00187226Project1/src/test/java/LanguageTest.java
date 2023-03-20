import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/beans.xml"})
public class LanguageTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void testItalian() {
        String actual = context.getMessage("greeting", null, "Default", Locale.ITALIAN);
        assertEquals("Ciao", actual);
    }

    @Test
    public void testFrench() {
        String actual = context.getMessage("greeting", null, "Default", Locale.FRENCH);
        assertEquals("Bonjour", actual);
    }
}
