package com.ml.hotel_ml_apigateway_service.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("auth/register").permitAll()
//                         .requestMatchers("auth/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/user/info").permitAll()
                        .requestMatchers("/admin/info").hasRole("ADMIN")
                        .anyRequest().authenticated())


                .httpBasic(Customizer.withDefaults())

//                .httpBasic(Customizer.withDefaults()).formLogin(formLogin -> formLogin
//                        .loginPage("/login")
//                        .permitAll()
//                )

//                .httpBasic(Customizer.withDefaults()).logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .addLogoutHandler(new SecurityContextLogoutHandler()))



                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean filter= new FilterRegistrationBean();
        filter.setFilter(new JwtFilter());
        // provide endpoints which needs to be restricted.
        // All Endpoints would be restricted if unspecified
        filter.addUrlPatterns("/user/info");
        filter.addUrlPatterns("/admin/info");
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
