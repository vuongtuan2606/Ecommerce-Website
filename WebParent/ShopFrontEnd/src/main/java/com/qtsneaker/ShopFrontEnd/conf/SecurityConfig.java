package com.qtsneaker.ShopFrontEnd.conf;

import com.qtsneaker.ShopFrontEnd.security.CustomerUserDetailsService;
import com.qtsneaker.ShopFrontEnd.security.DatabaseLoginSuccessHandler;
import com.qtsneaker.ShopFrontEnd.security.oauth.CustomerOAuth2UserService;
import com.qtsneaker.ShopFrontEnd.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {

    @Autowired private CustomerOAuth2UserService oAuth2UserService;

    @Autowired private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;

    @Autowired private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/account_details","/account_details/**",
                                        "/update_account_details","/cart","/checkout","/place_order").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .successHandler(databaseLoginSuccessHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                        .successHandler(auth2LoginSuccessHandler)
                )

                .logout(logout -> logout.permitAll()
                )
                .rememberMe(rem -> rem
                        .key("AbcDefgHijklmnOpqrs_1234567890")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                )

        ;

        return http.build();

    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception{
        return (web) -> web.ignoring().requestMatchers("/assets/**");
    }
    @Bean
    UserDetailsService userDetailsServices(){
        return  new CustomerUserDetailsService();
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
