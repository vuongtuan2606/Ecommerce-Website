package com.tuanvuong.qtsnearker.conf;

import com.tuanvuong.qtsnearker.security.AdminUserDetailsServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {

    @Bean
    SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception{
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage")
                        .usernameParameter("email")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .rememberMe(rem -> rem
                        .key("AbcDefgHijklmnOpqrs_1234567890")
                        .tokenValiditySeconds(7*24*60*60)
                )
        ;




        return http.build();

    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception{
        return (web) -> web.ignoring().requestMatchers("/administrator/image/**","/administrator/css/**","/administrator/js/**","/administrator/vendor/**","/administrator/webjars/**");
    }
    @Bean
    UserDetailsService userDetailsServices(){
        return  new AdminUserDetailsServices();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServices());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


}
