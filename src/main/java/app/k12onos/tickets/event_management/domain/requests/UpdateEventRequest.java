package app.k12onos.tickets.event_management.domain.requests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.domain.entities.TicketType;
import app.k12onos.tickets.event.domain.enums.EventStatus;
import app.k12onos.tickets.event_management.exceptions.TicketTypeNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateEventRequest(
        @NotBlank(message = "Event name is required") String name,

        LocalDateTime start,

        LocalDateTime end,

        @NotBlank(message = "Venue information is required") String venue,

        LocalDateTime salesStart,

        LocalDateTime salesEnd,

        @NotNull(message = "Event status is required") EventStatus status,

        @NotEmpty(message = "At least one ticket type is required") @Valid List<UpdateTicketTypeRequest> ticketTypes) {

    public void updateEntity(Event event) {
        event.setName(this.name());
        event.setStart(this.start());
        event.setEnd(this.end());
        event.setVenue(this.venue());
        event.setSalesStart(this.salesStart());
        event.setSalesEnd(this.salesEnd());
        event.setStatus(this.status());

        Set<UUID> ticketTypeIds = this
                .ticketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        event.getTicketTypes().removeIf(ticketType -> !ticketTypeIds.contains(ticketType.getId()));

        Map<UUID, TicketType> existingTicketTypesMap = event
                .getTicketTypes()
                .stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest newTicketType : this.ticketTypes()) {
            if (newTicketType.id() == null) {
                TicketType ticketType = newTicketType.toEntity(event);
                event.getTicketTypes().add(ticketType);
            } else if (existingTicketTypesMap.containsKey(newTicketType.id())) {
                TicketType existingTicketType = existingTicketTypesMap.get(newTicketType.id());
                newTicketType.updateEntity(existingTicketType);
            } else {
                throw new TicketTypeNotFoundException("TicketType with id " + newTicketType.id() + " not found");
            }
        }
    }

}
