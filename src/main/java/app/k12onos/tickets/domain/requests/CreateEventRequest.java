package app.k12onos.tickets.domain.requests;

import java.time.LocalDateTime;
import java.util.List;

import app.k12onos.tickets.domain.enums.EventStatus;
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

}
