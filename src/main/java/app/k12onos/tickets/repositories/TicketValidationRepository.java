package app.k12onos.tickets.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.TicketValidation;

@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {

    @Query("SELECT COUNT(TV) FROM TicketValidation TV WHERE TV.ticket.id = ?1 AND TV.status = TicketValidationStatus.VALID")
    long countValidValidationsByTicket(UUID ticketId);

}
