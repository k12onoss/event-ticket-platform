package app.k12onos.tickets.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

import app.k12onos.tickets.security.ui.views.SignInView;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class VaadinSecurityConfig {

    @Order(1)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, OidcUserAdapterService userService) throws Exception {
        http
            .with(VaadinSecurityConfigurer.vaadin(), configurer -> configurer.oauth2LoginPage(SignInView.path))
            .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(userService)));

        return http.build();
    }

}
