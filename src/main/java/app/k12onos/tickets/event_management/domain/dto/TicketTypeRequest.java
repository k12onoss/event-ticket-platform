package app.k12onos.tickets.event_management.domain.dto;

import java.util.UUID;

import app.k12onos.tickets.event_management.domain.requests.CreateTicketTypeRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateTicketTypeRequest;
import app.k12onos.tickets.event_management.domain.responses.TicketTypeResponse;

public record TicketTypeRequest(UUID id, String name, String description, Double price, Integer totalAvailable) {

    public static final String PROP_ID = "id";
    public static final String PROP_NAME = "name";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_PRICE = "price";
    public static final String PROP_TOTAL_AVAILABLE = "totalAvailable";

    public static TicketTypeRequest from(TicketTypeResponse ticketType) {
        return new TicketTypeRequest(
            ticketType.id(),
            ticketType.name(),
            ticketType.description(),
            ticketType.price(),
            ticketType.totalAvailable());
    }

    public CreateTicketTypeRequest toCreateDto() {
        return new CreateTicketTypeRequest(this.name(), this.description(), this.price(), this.totalAvailable());
    }

    public UpdateTicketTypeRequest toUpdateDto() {
        return new UpdateTicketTypeRequest(
            this.id(),
            this.name(),
            this.description(),
            this.price(),
            this.totalAvailable());
    }

}
