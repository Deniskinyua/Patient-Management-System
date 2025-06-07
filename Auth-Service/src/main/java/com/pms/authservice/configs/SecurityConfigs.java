package com.pms.authservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security settings.
 * <p>
 * Defines security filter chains and password encoding strategies for the application.
 * </p>
 *
 * <ul>
 *   <li>
 *     <b>SecurityFilterChain:</b> Configures HTTP security to permit all requests.
 *     This can be customized to restrict access to certain endpoints as needed.
 *   </li>
 *   <li>
 *     <b>PasswordEncoder:</b> Provides a BCrypt-based password encoder bean for secure password hashing.
 *   </li>
 * </ul>
 *
 * @author Denis Kinyua
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigs {

    public SecurityConfigs() {
        System.out.println("🔧 SecurityConfig loaded!");
    }

    /**
     * Configures the application's HTTP security.
     * <p>
     * Currently, all requests are permitted without authentication.
     * Modify this method to restrict access to specific endpoints as required.
     * </p>
     *
     * @param httpSecurity the {@link HttpSecurity} to modify
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("🔧 SecurityFilterChain bean created!");
        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
        System.out.println("🔧 SecurityFilterChain bean finished creation!");

        return httpSecurity.build();
    }

    /**
     * Provides a BCrypt password encoder bean.
     * <p>
     * This encoder is used for hashing and verifying user passwords securely.
     * </p>
     *
     * @return a {@link PasswordEncoder} instance using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}