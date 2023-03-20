package ie.szymon.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfigurationInMemory {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity https) throws Exception {
        https.authorizeRequests()
                .antMatchers("/offices/add", "/offices/{officeNo}/delete", "/offices/{officeNo}/occupancy").hasAnyRole("HOS","HOD")
                .antMatchers("/departments/add", "/departments/{title}/delete", "/offices/{officeNo}/move").hasRole("HOS")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().and().formLogin().and().csrf().disable();
        return https.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        String encodedPassword = passwordEncoder().encode("password");
        UserDetails hod = User.withUsername("HOD").password(encodedPassword).roles("HOD").build();
        UserDetails hos = User.withUsername("HOS").password(encodedPassword).roles("HOS").build();
        return new InMemoryUserDetailsManager(hod, hos);
    }
}
