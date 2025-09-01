package app.k12onos.tickets.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query("SELECT COUNT(T) FROM Ticket T WHERE T.ticketType.id = ?1")
    int findCount(UUID ticketTypeId);

    @Query("SELECT T FROM Ticket T LEFT JOIN TicketType TT ON T.ticketType.id = TT.id LEFT JOIN Event E ON TT.event.id = E.id WHERE T.purchaser.id = ?1")
    Page<Ticket> findTicketsForPurchaser(UUID purchaserId, Pageable pageable);

}
