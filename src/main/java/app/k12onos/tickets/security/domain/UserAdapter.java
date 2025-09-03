package app.k12onos.tickets.security.domain;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import app.k12onos.tickets.security.domain.dto.UserDto;

public class UserAdapter implements OidcUser {

    private final UserDto user;
    private final OidcUser delegate;

    public UserAdapter(UserDto user, OidcUser delegate) {
        this.user = user;
        this.delegate = delegate;
    }

    public UserDto getUser() {
        return this.user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.delegate.getClaims();
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.delegate.getIdToken();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.delegate.getUserInfo();
    }

}
