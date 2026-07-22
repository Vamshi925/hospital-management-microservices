package com.example.appointmentservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
/**
 * The SecurityConfig class configures the security settings for the application.
 * It defines the security filter chain and specifies the authorization rules for different endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    /**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
        		.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()).
                        authorizeHttpRequests(request -> request

                		// All endpoints from AppointmentController
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(
                                "/api/appointments/getAll",
                                "/api/appointments/{appointmentId}",
                                "/api/appointments/add",
                                "/api/appointments/update/{appointmentId}",
                                "/api/appointments/delete/{appointmentId}",
                                "/api/appointments/deleteAll",
                                "/api/appointments/current/patient/{patientId}",
                                "/api/appointments/current/doctor/{doctorId}",
                                "/api/appointments/past/patient/{patientId}",
                                "/api/appointments/past/doctor/{doctorId}",
                                "/api/appointments/book",
                                "/api/appointments/reschedule",
                                "/api/appointments/cancel/{appointmentId}",
                                "/api/appointments/date/{appointmentDate}",
                                "/api/appointments/completeAppointment/{appointmentId}",
                                "/api/appointments/patient/{patientId}",
                                "/api/appointments/doctor/{doctorId}",
                                "/api/appointments/filter/{startDate}/{endDate}"
                        ).authenticated()
                            .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}




