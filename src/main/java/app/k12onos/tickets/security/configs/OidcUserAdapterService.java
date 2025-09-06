package app.k12onos.tickets.security.configs;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import app.k12onos.tickets.security.domain.UserAdapter;
import app.k12onos.tickets.security.domain.dto.UserDto;
import app.k12onos.tickets.security.services.UserService;
import app.k12onos.tickets.security.utils.SecurityUtil;

@Component
public class OidcUserAdapterService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserService userService;

    public OidcUserAdapterService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserService oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);

        UserDto user = this.userService.findOrCreateUser(oidcUser);

        OidcIdToken idToken = userRequest.getIdToken();
        Collection<GrantedAuthority> authorities = SecurityUtil.mapAuthorities(idToken.getClaimAsMap("realm_access"));
        OidcUser enrichedOidcUser = new DefaultOidcUser(authorities, idToken, oidcUser.getUserInfo());

        return new UserAdapter(user, enrichedOidcUser);
    }

}
