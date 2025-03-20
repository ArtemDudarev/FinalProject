package com.example.FinalProject.service.config;

import com.example.FinalProject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/**").permitAll()
                .requestMatchers("/", "/api/products").permitAll()
                .requestMatchers("/api/addresses/**",
                    "/api/categories",
                    "/api/categories/{id}",
                    "/api/images",
                    "/api/images/{id}",
                    "/api/images/by-product/{productId}",
                    "/api/loyalty-programs",
                    "/api/loyalty-programs/{id}",
                    "/api/orders/create",
                    "/api/orders/{orderId}/add-item",
                    "/api/orders/{orderId}/confirm",
                    "/api/orders/order/{id}",
                    "/api/payment-methods/{id}",
                    "/api/payment-methods",
                    "/api/products/{id}",
                    "/api/products",
                    "/api/profile/create",
                    "/api/profile/update/{id}",
                    "/api/profile/getProfile/{id}",
                    "/api/profile/delete/{id}").hasAnyRole("USER", "WORKER")
                .requestMatchers("/api/orders/**",
                    "/api/products/update/{id}",
                    "/api/roles/{id}").hasRole("WORKER")
                .requestMatchers( "/api/addresses/**",
                    "/api/admin/manage-users/**",
                    "/api/categories/**",
                    "/api/images/**",
                    "/api/loyalty-programs/**",
                    "/api/orders/**",
                    "/api/payment-methods/**",
                    "/api/products/**",
                    "/api/roles/**",
                    "/api/profile/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);

        return httpSecurity.build();
    }
}
