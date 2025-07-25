package app.k12onos.tickets.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

}
