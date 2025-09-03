package app.k12onos.tickets.security.domain.dto;

import java.util.UUID;

import app.k12onos.tickets.security.domain.entities.User;

public record UserDto(UUID id, String name, String email) {

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}
