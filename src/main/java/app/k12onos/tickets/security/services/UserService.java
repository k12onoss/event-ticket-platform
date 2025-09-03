package app.k12onos.tickets.security.services;

import java.util.UUID;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.security.domain.entities.User;
import app.k12onos.tickets.security.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUserIfAbsent(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        if (!this.userRepository.existsById(id)) {
            User _ = this.createUser(id, jwt.getClaimAsString("name"), jwt.getClaimAsString("email"));
        }
    }

    public User findOrCreateUser(OidcUser oidcUser) {
        UUID id = UUID.fromString(oidcUser.getSubject());

        return this.userRepository.findById(id).orElse(this.createUser(id, oidcUser.getName(), oidcUser.getEmail()));
    }

    private User createUser(UUID id, String name, String email) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setName(name);
        newUser.setEmail(email);

        return this.userRepository.save(newUser);
    }

}
