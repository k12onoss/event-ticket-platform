package app.k12onos.tickets.event_management.domain.requests;

import java.time.LocalDateTime;
import java.util.List;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.domain.entities.TicketType;
import app.k12onos.tickets.event.domain.enums.EventStatus;
import app.k12onos.tickets.security.domain.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateEventRequest(
        @NotBlank(message = "Event name is required") String name,

        LocalDateTime start,

        LocalDateTime end,

        @NotBlank(message = "Venue information is required") String venue,

        LocalDateTime salesStart,

        LocalDateTime salesEnd,

        @NotNull(message = "Event status is required") EventStatus status,

        @NotEmpty(message = "At least one ticket type is required") @Valid List<CreateTicketTypeRequest> ticketTypes) {

    public CreateEventRequest {
        if (ticketTypes == null) {
            ticketTypes = List.of();
        }
    }

    public Event toEntity(User organizer) {
        Event event = new Event();

        List<TicketType> ticketTypes = this
                .ticketTypes()
                .stream()
                .map(ticketType -> ticketType.toEntity(event))
                .toList();

        event.setName(this.name());
        event.setStart(this.start());
        event.setEnd(this.end());
        event.setVenue(this.venue());
        event.setSalesStart(this.salesStart());
        event.setSalesEnd(this.salesEnd());
        event.setStatus(this.status());
        event.setOrganizer(organizer);
        event.setTicketTypes(ticketTypes);

        return event;
    }

}
