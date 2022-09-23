package ie.szymon;

import ie.szymon.entities.*;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ComponentScan("ie.szymon")
public class Config {
    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
    @Bean
    Studio wb() {
        return new Studio("Warner Bros", 1923);
    }

    @Bean
    Studio para() {
        return new Studio("Paramount", 1911);
    }

    @Bean
    CartoonCharacter lola() {
        return new CartoonCharacter("Lola Bunny", 1996, true, true, wb());
    }

    @Bean
    CartoonCharacter popeye() {
        return new CartoonCharacter("Popeye the Sailor Man", 1930, true, true, para());
    }

    @Bean
    School nmci() {
        return new School("National Maritime College of Ireland", "reception@nmci.ie");
    }

    @Bean
    School ccad() {
        return new School("Crawford College of Art and Design", "crawford.enquiries@mtu.ie");
    }

    @Bean
    Department art() {
        return new Department("Art in Health and Education", "louise.foott@cit.ie", ccad());
    }

    @Bean
    Department media() {
        return new Department("Media Communications", "rose.mcgrath@cit.ie", ccad());
    }

    @Bean
    Department marine() {
        return new Department("Marine Studies", "sinead.reen@nmci.ie", nmci());
    }
}
