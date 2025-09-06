package app.k12onos.tickets.event_management.ui.components;

import java.util.Optional;

import org.springframework.lang.Nullable;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.Autocapitalize;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;

import app.k12onos.tickets.event.domain.enums.EventStatus;
import app.k12onos.tickets.event_management.domain.dto.EventRequest;

public class EventForm extends FormLayout {

    private final Binder<EventRequest> eventBinder;

    public EventForm() {
        TextField name = new TextField("Name");
        name.setAutocapitalize(Autocapitalize.WORDS);
        name.setRequired(true);

        DateTimePicker start = new DateTimePicker("Start Time");
        DateTimePicker end = new DateTimePicker("End Time");

        TextField venue = new TextField("Venue");
        venue.setRequired(true);

        DateTimePicker salesStart = new DateTimePicker("Ticket Sale Start Time");
        DateTimePicker salesEnd = new DateTimePicker("Ticket Sale End Time");

        ComboBox<EventStatus> status = new ComboBox<>("Status", EventStatus.values());
        status.setRequired(true);

        TicketTypeField ticketTypes = new TicketTypeField();

        this.setAutoResponsive(true);
        this.setMaxColumns(2);
        this.setExpandColumns(true);
        this.setExpandFields(true);

        this.addFormRow(name, venue);
        this.addFormRow(start, end);
        this.addFormRow(salesStart, salesEnd);
        this.addFormRow(status);
        this.add(ticketTypes, 2);

        this.eventBinder = new Binder<>(EventRequest.class);
        this.eventBinder.forField(name).asRequired("Name is required").bind(EventRequest.PROP_NAME);
        this.eventBinder.forField(start).bind(EventRequest.PROP_START);
        this.eventBinder.forField(end).bind(EventRequest.PROP_END);
        this.eventBinder.forField(venue).asRequired("Venue is required").bind(EventRequest.PROP_VENUE);
        this.eventBinder.forField(salesStart).bind(EventRequest.PROP_SALES_START);
        this.eventBinder.forField(salesEnd).bind(EventRequest.PROP_SALES_END);
        this.eventBinder.forField(status).asRequired("Status is required").bind(EventRequest.PROP_STATUS);
        this.eventBinder
            .forField(ticketTypes)
            .withValidator(types -> types != null && types.size() > 0, "At least one ticket type is required")
            .bind(EventRequest.PROP_TICKET_TYPES);

        this.eventBinder.withValidator((event, _) -> {
            if (event.start() != null && event.end() != null && event.start().isAfter(event.end())) {
                return ValidationResult.error("Start date cannot be after end date");
            }
            if (event.salesStart() != null
                && event.salesEnd() != null
                && event.salesStart().isAfter(event.salesEnd())) {
                return ValidationResult.error("Ticket sale start date cannot be after ticket sale end date");
            }
            return ValidationResult.ok();
        });
    }

    public Optional<EventRequest> getEvent() {
        try {
            return Optional.of(this.eventBinder.writeRecord());
        } catch (ValidationException _) {
            return Optional.empty();
        }
    }

    public void setEvent(@Nullable EventRequest event) {
        if (event != null) {
            this.eventBinder.readRecord(event);
        } else {
            this.eventBinder.refreshFields();
        }
    }

}
