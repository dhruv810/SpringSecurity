package com.example.SpringSecurity.util;

import com.example.SpringSecurity.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // disable csrf. application is less secure now
        http.csrf(customizer -> customizer.disable());
        // authenticate all incoming http requests
        http.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.PUT, "/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/all").permitAll()
                        .anyRequest().authenticated());
        // FOR WEB USE: if not logged in, redirect to default Spring form login page
        http.formLogin(Customizer.withDefaults());
        // FOR POSTMAN: accepts username password in authentication parameters for login
        http.httpBasic(Customizer.withDefaults());
        // make your application stateless, meaning it doesn't keep track of your session token
        // new token will be created for each request
        // do this if you want to disable csrf, as new csrf token will be created after each visit
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // you can also use builder pattern like below
        // http.csrf(..).authorizeHttpRequests(..)..()....;

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // this mean password is not encoded
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        // to configure it for BCrypt change it to following
        // make sure to encode all password in service layer when making entry in database
        provider.setPasswordEncoder(this.bCryptPasswordEncoder);
        provider.setUserDetailsService(this.myUserDetailService);
        return provider;
    }



}



