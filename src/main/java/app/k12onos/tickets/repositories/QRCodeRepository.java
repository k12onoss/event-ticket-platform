package app.k12onos.tickets.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.k12onos.tickets.domain.entities.QRCode;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, UUID> {

    @Query("SELECT Q FROM QRCode Q LEFT JOIN Ticket T ON Q.ticket.id = T.id WHERE Q.token = ?1")
    Optional<QRCode> findByToken(String token);

    @Query("SELECT Q FROM QRCode Q WHERE Q.ticket.id = ?1 AND Q.ticket.purchaser.id = ?2 AND Q.status = QRCodeStatus.ACTIVE")
    Optional<QRCode> findActiveQRCodeForTicketAndPurchaser(UUID ticketId, UUID purchaserId);

}
