package app.k12onos.tickets.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.responses.ListEventResponse;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT E FROM Event E WHERE E.organizer.id = ?1")
    Page<ListEventResponse> findByOrganizerId(UUID organizerId, Pageable pageable);

}
