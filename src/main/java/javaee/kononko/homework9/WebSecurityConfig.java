package javaee.kononko.homework9;

import javaee.kononko.homework9.models.Permission;
import javaee.kononko.homework9.repositories.UserRepository;
import javaee.kononko.homework9.services.MyPasswordEncoder;
import javaee.kononko.homework9.services.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/wishlist/**").hasAuthority(Permission.WISHLIST.name())
                .anyRequest().permitAll()
                .and().formLogin().permitAll()
                .and().logout().permitAll();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(MyPasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder, @Qualifier("daoAuthenticationProvider") DaoAuthenticationProvider authenticationProvider){
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }
}
