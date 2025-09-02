package app.k12onos.tickets.event_management.domain.requests;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.domain.entities.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateTicketTypeRequest(
        @NotBlank(message = "TicketType name is required") String name,

        String description,

        @NotNull(message = "Price is required") @PositiveOrZero(message = "Price must be greater than or equal to zero") Double price,

        Integer totalAvailable) {

    public TicketType toEntity(Event event) {
        TicketType ticketType = new TicketType();
        ticketType.setName(this.name());
        ticketType.setDescription(this.description());
        ticketType.setPrice(this.price());
        ticketType.setTotalAvailable(this.totalAvailable());
        ticketType.setEvent(event);

        return ticketType;
    }

}
