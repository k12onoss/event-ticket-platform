package app.k12onos.tickets;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestConfig {

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration registration = ClientRegistration
            .withRegistrationId("keycloak")
            .clientId("test-client")
            .clientSecret("test-secret")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("openid", "profile")
            .authorizationUri("http://localhost/auth")
            .tokenUri("http://localhost/token")
            .userInfoUri("http://localhost/userinfo")
            .userNameAttributeName("sub")
            .clientName("Keycloak")
            .build();

        return new InMemoryClientRegistrationRepository(registration);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return token -> Jwt
            .withTokenValue(token)
            .header("alg", "none")
            .claim("sub", "test-user")
            .claim("scope", "openid profile")
            .build();
    }

}
