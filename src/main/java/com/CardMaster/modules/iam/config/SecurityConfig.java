package com.CardMaster.modules.iam.config;

import com.CardMaster.modules.iam.security.JwtFilter;
import com.CardMaster.modules.iam.exception.CustomAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // enables @PreAuthorize annotations
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        // --- Public Endpoints ---
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .requestMatchers("/users/logout").authenticated()

                        // --- User & Audit Logs ---
                        .requestMatchers("/users", "/users/*", "/auditlogs/**").hasRole("ADMIN")

                        // --- Applications ---
                        .requestMatchers(HttpMethod.PUT, "/applications/*/scores").hasRole("UNDERWRITER")
                        .requestMatchers("/applications/*/scores", "/applications/*/scores/latest")
                        .hasAnyRole("UNDERWRITER", "ADMIN")
                        .requestMatchers("/applications/*/decisions", "/applications/*/decisions/latest")
                        .hasRole("UNDERWRITER")
                        .requestMatchers(HttpMethod.POST, "/applications/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/applications/**")
                        .hasAnyRole("UNDERWRITER", "ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/applications/**").hasRole("ADMIN")

                        // --- Card Issuance ---
                        .requestMatchers(HttpMethod.POST, "/api/cards/**").hasAnyRole("OFFICER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cards/**").hasAnyRole("OFFICER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/cards/**")
                        .hasAnyRole("CUSTOMER", "OFFICER", "ADMIN")

                        // --- Account Setup ---
                        .requestMatchers(HttpMethod.POST, "/api/accounts/**").hasAnyRole("OFFICER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/accounts/**").hasAnyRole("OFFICER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/accounts/**")
                        .hasAnyRole("CUSTOMER", "OFFICER", "ADMIN")

                        // --- Billing & Rewards ---
                       // .requestMatchers("/billing/**").hasRole("OPERATIONS_ANALYST")
                        .requestMatchers("/rewards/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // --- Fraud Monitoring ---
                        .requestMatchers("/fraud/**").hasRole("RISK")

                        // --- Product Configuration ---
                        .requestMatchers(HttpMethod.GET, "/api/products/**")
                        .hasAnyRole("CUSTOMER", "UNDERWRITER", "OFFICER", "ADMIN")
                        .requestMatchers("/api/fees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")

                        // --- Module 6: TAP ---
                        .requestMatchers(HttpMethod.POST, "/transactions/authorize")
                        .hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/transactions/post/*")
                        .hasAnyRole("ADMIN", "OFFICER")
                        .requestMatchers(HttpMethod.POST, "/transactions/reverse/*")
                        .hasAnyRole("ADMIN", "OFFICER", "RISK")
                        .requestMatchers(HttpMethod.GET, "/transactions/**")
                        .hasAnyRole("CUSTOMER", "ADMIN", "OFFICER", "RISK")
                        .requestMatchers(HttpMethod.POST, "/transaction-holds")
                        .hasAnyRole("ADMIN", "OFFICER")
                        .requestMatchers(HttpMethod.POST, "/transaction-holds/release/*")
                        .hasAnyRole("ADMIN", "OFFICER", "RISK")
                        .requestMatchers(HttpMethod.GET, "/transaction-holds/**")
                        .hasAnyRole("ADMIN", "OFFICER", "RISK")

                        // --- Module 7: BSP ---
                        .requestMatchers(HttpMethod.POST, "/billing/payments/capture")
                        .hasAnyRole("CUSTOMER", "ADMIN", "OFFICER")
                        .requestMatchers(HttpMethod.POST, "/billing/statements/generate")
                        .hasAnyRole("ADMIN", "OFFICER")
                        .requestMatchers(HttpMethod.POST, "/billing/statements/close/*")
                        .hasAnyRole("ADMIN", "OFFICER")
                        .requestMatchers(HttpMethod.GET, "/billing/statements/**")
                        .hasAnyRole("CUSTOMER", "ADMIN", "OFFICER")
                        //.requestMatchers(HttpMethod.POST, "/billing/payments/capture")
                        //.hasAnyRole("CUSTOMER", "ADMIN", "OFFICER")
                        .requestMatchers(HttpMethod.GET, "/billing/payments/**")
                        .hasAnyRole("CUSTOMER", "ADMIN", "OFFICER")

                        // --- Default ---
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler) // custom handler
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
