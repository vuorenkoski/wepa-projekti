package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DevelopmentSecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Override
//    public void configure(WebSecurity sec) throws Exception {
//        // Pyyntöjä ei tarkasteta
//        sec.ignoring().antMatchers("/**");
//    }
    
        
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // poistetaan csrf-tarkistus käytöstä h2-konsolin vuoksi
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        
        http.authorizeRequests()
                .antMatchers("/signup","/","/styles.css").permitAll()
                .antMatchers("/h2-console","/h2-console/**").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login").permitAll().and()
                .logout().permitAll()
                .logoutSuccessUrl("/");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
