package app.k12onos.tickets.ticket.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.ticket.domain.entities.QrCode;
import app.k12onos.tickets.ticket.domain.enums.QrCodeStatus;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    @Query("SELECT Q FROM QrCode Q LEFT JOIN Ticket T ON Q.ticket.id = T.id WHERE Q.token = ?1")
    Optional<QrCode> findByToken(String token);

    @Query("SELECT Q FROM QrCode Q WHERE Q.ticket.id = ?1 AND Q.ticket.purchaser.id = ?2 AND Q.status = ?3")
    Optional<QrCode> findQrCodeForTicketAndPurchaserAndStatus(UUID ticketId, UUID purchaserId, QrCodeStatus status);

}
