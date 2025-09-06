package app.k12onos.tickets.event_management.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import app.k12onos.tickets.event.domain.enums.EventStatus;
import app.k12onos.tickets.event_management.domain.requests.CreateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.CreateTicketTypeRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateTicketTypeRequest;
import app.k12onos.tickets.event_management.domain.responses.EventResponse;

public record EventRequest(
    String name,
    LocalDateTime start,
    LocalDateTime end,
    String venue,
    LocalDateTime salesStart,
    LocalDateTime salesEnd,
    EventStatus status,
    List<TicketTypeRequest> ticketTypes) {

    public static final String PROP_NAME = "name";
    public static final String PROP_START = "start";
    public static final String PROP_END = "end";
    public static final String PROP_VENUE = "venue";
    public static final String PROP_SALES_START = "salesStart";
    public static final String PROP_SALES_END = "salesEnd";
    public static final String PROP_STATUS = "status";
    public static final String PROP_TICKET_TYPES = "ticketTypes";

    public static EventRequest from(EventResponse event) {
        List<TicketTypeRequest> ticketTypes = event.ticketTypes().stream().map(TicketTypeRequest::from).toList();
        return new EventRequest(
            event.name(),
            event.start(),
            event.end(),
            event.venue(),
            event.salesStart(),
            event.salesEnd(),
            event.status(),
            ticketTypes);
    }

    public CreateEventRequest toCreateDto() {
        List<CreateTicketTypeRequest> ticketTypes = this
            .ticketTypes()
            .stream()
            .map(TicketTypeRequest::toCreateDto)
            .toList();
        return new CreateEventRequest(
            this.name(),
            this.start(),
            this.end(),
            this.venue(),
            this.salesStart(),
            this.salesEnd(),
            this.status(),
            ticketTypes);
    }

    public UpdateEventRequest toUpdateDto() {
        List<UpdateTicketTypeRequest> ticketTypes = this
            .ticketTypes()
            .stream()
            .map(TicketTypeRequest::toUpdateDto)
            .toList();
        return new UpdateEventRequest(
            this.name(),
            this.start(),
            this.end(),
            this.venue(),
            this.salesStart(),
            this.salesEnd(),
            this.status(),
            ticketTypes);
    }

}
