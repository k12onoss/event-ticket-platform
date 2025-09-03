package app.k12onos.tickets.event.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.event.domain.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT E FROM Event E WHERE E.organizer.id = ?1")
    Page<Event> findEventsByOrganizer(UUID organizerId, Pageable pageable);

    @Query("SELECT E FROM Event E LEFT JOIN FETCH E.ticketTypes WHERE E.organizer.id = ?1 AND E.id = ?2")
    Optional<Event> findEventByOrganizer(UUID organizerId, UUID eventId);

    @Query("SELECT E FROM Event E WHERE E.status = EventStatus.PUBLISHED")
    Page<Event> findPublishedEvents(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM events WHERE status = 'PUBLISHED' AND to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) @@ plainto_tsquery('english', :searchTerm)", countQuery = "SELECT COUNT(*) FROM events WHERE status = 'PUBLISHED' AND to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '')) @@ plainto_tsquery('english', :searchTerm)")
    Page<Event> searchPublishedEvents(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT E FROM Event E JOIN FETCH E.ticketTypes WHERE E.id = ?1 AND E.status = EventStatus.PUBLISHED")
    Optional<Event> findPublishedEvent(UUID id);

}
