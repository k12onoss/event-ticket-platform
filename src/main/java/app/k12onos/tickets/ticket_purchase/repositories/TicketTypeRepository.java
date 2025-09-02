package app.k12onos.tickets.ticket_purchase.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.event.domain.entities.TicketType;
import jakarta.persistence.LockModeType;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT TT FROM TicketType TT WHERE TT.id = ?1")
    Optional<TicketType> findByIdWithLock(UUID id);

}
