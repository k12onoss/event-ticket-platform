package app.k12onos.tickets.domain.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateTicketTypeRequest(
                UUID id,

                @NotBlank(message = "TicketType name is required") String name,

                String description,

                @NotNull(message = "Price is required") @PositiveOrZero(message = "Price must be greater than or equal to zero") Double price,

                Integer totalAvailable) {

}
