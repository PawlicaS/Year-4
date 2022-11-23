package ie.szymon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class Security extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/", "/departments", "/offices", "/directorsfilms/{directorName}", "/edit", "/h2/**").permitAll()
                .antMatchers("/addoffice", "/deleteoffice", "/modifyofficeoccupancy").hasAnyRole("HOS","HOD")
                .antMatchers("/adddepartment", "/deletedepartment", "/moveoffice").hasRole("HOS")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/edit");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        String encodedPassword = passwordEncoder().encode("password");
        UserDetails user1 = User.withUsername("Head of Department").password(encodedPassword).roles("HOD").build();
        UserDetails user2 = User.withUsername("Head of School").password(encodedPassword).roles("HOS").build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}