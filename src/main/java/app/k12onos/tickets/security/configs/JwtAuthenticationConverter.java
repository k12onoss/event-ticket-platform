package app.k12onos.tickets.security.configs;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import app.k12onos.tickets.security.services.UserService;
import app.k12onos.tickets.security.utils.SecurityUtil;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final UserService userService;

    public JwtAuthenticationConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Nullable
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        this.userService.createUserIfAbsent(jwt);

        Collection<GrantedAuthority> authorities = SecurityUtil.mapAuthorities(jwt.getClaimAsMap("realm_access"));
        return new JwtAuthenticationToken(jwt, authorities);
    }

}
