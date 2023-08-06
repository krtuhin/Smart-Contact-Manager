package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfiguration {

    //bean for user details service
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new CustomUserDetailService();
    }

    //bean for encoding password
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //bean for authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        //create constructor
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        //set value of user details service
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());

        //set value of encoded password
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());

        return daoAuthenticationProvider;
    }

    //new version spring security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        //protecting page
        httpSecurity.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/admin/**").hasAuthority("admin_role")
                .requestMatchers("/user/**").hasAuthority("user_role")
                .requestMatchers("/**").permitAll());

        //custom login page
        httpSecurity.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/success")
                .defaultSuccessUrl("/user/index"));

        //default login page
        //httpSecurity.formLogin(Customizer.withDefaults());

        //csrf security disable
        httpSecurity.csrf(csrf -> csrf.disable());

        //authentication provider
        httpSecurity.authenticationProvider(this.authenticationProvider());

        return httpSecurity.build();
    }
}