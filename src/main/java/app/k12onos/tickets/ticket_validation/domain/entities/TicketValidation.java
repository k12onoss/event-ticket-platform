package app.k12onos.tickets.ticket_validation.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import app.k12onos.tickets.ticket.domain.entities.Ticket;
import app.k12onos.tickets.ticket_validation.domain.enums.TicketValidationMethod;
import app.k12onos.tickets.ticket_validation.domain.enums.TicketValidationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_validations")
public class TicketValidation {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketValidationStatus status;

    @Column(name = "validation_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketValidationMethod validationMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public TicketValidation(UUID id, TicketValidationStatus status, TicketValidationMethod validationMethod,
            Ticket ticket, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.status = status;
        this.validationMethod = validationMethod;
        this.ticket = ticket;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public TicketValidation() {
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TicketValidationStatus getStatus() {
        return this.status;
    }

    public void setStatus(TicketValidationStatus status) {
        this.status = status;
    }

    public TicketValidationMethod getValidationMethod() {
        return this.validationMethod;
    }

    public void setValidationMethod(TicketValidationMethod validationMethod) {
        this.validationMethod = validationMethod;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.updatedAt == null) ? 0 : this.updatedAt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        TicketValidation other = (TicketValidation) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        if (this.status != other.status)
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!this.updatedAt.equals(other.updatedAt))
            return false;
        return true;
    }

}
