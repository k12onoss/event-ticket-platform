package app.k12onos.tickets.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.responses.ListEventResponse;
import app.k12onos.tickets.domain.responses.PublishedEventResponse;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT E FROM Event E WHERE E.organizer.id = ?1")
    Page<ListEventResponse> findEventsByOrganizer(UUID organizerId, Pageable pageable);

    @Query("SELECT E FROM Event E LEFT JOIN FETCH E.ticketTypes WHERE E.organizer.id = ?1 AND E.id = ?2")
    Optional<Event> findEventByOrganizer(UUID organizerId, UUID eventId);

    @Query("SELECT E FROM Event E WHERE E.status = EventStatus.PUBLISHED")
    Page<PublishedEventResponse> findPublishedEvents(Pageable pageable);

}
