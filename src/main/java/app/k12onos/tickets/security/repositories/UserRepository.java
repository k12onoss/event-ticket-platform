package app.k12onos.tickets.security.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.security.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {}
