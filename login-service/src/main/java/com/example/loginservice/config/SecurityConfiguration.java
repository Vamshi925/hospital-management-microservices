package com.example.loginservice.config;

import com.example.loginservice.filter.JwtFilter;
import com.example.loginservice.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security. Configures authentication and
 * authorization for the application.
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Lazy
	@Autowired
	JwtFilter jwtFilter;

	@Lazy
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Creates a PasswordEncoder bean for encoding passwords.
	 *
	 * @return BCryptPasswordEncoder instance.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures the security filter chain.
	 *
	 * @param http HttpSecurity object.
	 * @return SecurityFilterChain instance.
	 * @throws Exception If an error occurs during configuration.
	 */
	@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        // 1. Disable CSRF for stateless REST APIs
        .csrf(csrf -> csrf.disable())

        // 2. Bypass Spring Security's CORS checks (Gateway handles CORS)
        .cors(cors -> cors.configurationSource(request -> {
            var config = new org.springframework.web.cors.CorsConfiguration();
            config.addAllowedOriginPattern("*");
            config.addAllowedMethod("*");
            config.addAllowedHeader("*");
            config.setAllowCredentials(true);
            return config;
        }))

        // 3. Authorization Rules
        .authorizeHttpRequests(request -> request
            // Explicitly allow all OPTIONS requests
            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
            
            // Allow all registration routes using wildcard matching
            .requestMatchers("/api/users/register/**", "/api/users/login", "/actuator/**").permitAll()
            
            // Authenticated endpoints
            .requestMatchers(
                "/api/patients/getAll", 
                "/api/patients/{patientId}",
                "/api/patients/name/{pName}", 
                "/api/patients/update",
                "/api/patients/delete/{patientId}", 
                "/api/patients/deleteAll",
                "/api/patients/current/patient/{patientId}", 
                "/api/patients/past/patient/{patientId}",
                "/api/patients/medicalHistory/{patientId}", 
                "/api/patients/appointment/{patientId}",
                "/api/patients/availableDoctors/{specializationName}/{availableDate}/{session}",
                "/api/patients/bookAppointment", 
                "/api/patients/rescheduleAppointment",
                "/api/patients/cancelAppointment/{appointmentId}",
                "/api/patients/notifications/{patientId}",
                "/api/patients/notifications/markAsRead/{notificationId}",
                "/api/patients/filterAppointmentsByDate/{startDate}/{endDate}/{patientId}/{appointmentStatus}"
            ).authenticated()
            
            .anyRequest().authenticated()
        )

        // 4. Stateless Sessions
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 5. JWT Filter
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}

	/**
	 * Creates an AuthenticationProvider bean.
	 *
	 * @return DaoAuthenticationProvider instance.
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	/**
	 * Creates an AuthenticationManager bean.
	 *
	 * @param config AuthenticationConfiguration object.
	 * @return AuthenticationManager instance.
	 * @throws Exception If an error occurs during configuration.
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
