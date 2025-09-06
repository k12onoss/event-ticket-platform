package app.k12onos.tickets.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import app.k12onos.tickets.security.domain.UserRoles;

@Configuration
public class RestApiSecurityConfig {

    @Order(0)
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {

        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers(HttpMethod.GET, "/api/v1/published-events/**")
                    .permitAll()
                    .requestMatchers("/api/v1/ticket-types/**")
                    .hasRole(UserRoles.ATTENDEE)
                    .requestMatchers("/api/v1/events/**")
                    .hasRole(UserRoles.ORGANIZER)
                    .requestMatchers("/api/v1/ticket-validations")
                    .hasRole(UserRoles.STAFF)
                    .requestMatchers("/api/**")
                    .authenticated())
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

}
