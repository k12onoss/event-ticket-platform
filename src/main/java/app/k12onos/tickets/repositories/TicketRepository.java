package app.k12onos.tickets.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.responses.ListTicketResponse;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("SELECT COUNT(T) FROM Ticket T WHERE T.ticketType.id = ?1")
    int findCount(UUID ticketTypeId);

    @Query("SELECT T FROM Ticket T LEFT JOIN TicketType TT ON T.ticketType.id = TT.id WHERE T.attendee.id = ?1")
    Page<ListTicketResponse> findTicketsForPurchaser(UUID purchaserId, Pageable pageable);

    @Query("SELECT T FROM Ticket WHERE T.id = ?2 AND T.purchaser.id = ?1")
    Optional<Ticket> findTicketForPurchaser(UUID purchaserId, UUID ticketId);

}
