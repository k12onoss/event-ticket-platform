package app.k12onos.tickets.event_management.ui.components;

import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import app.k12onos.tickets.event_management.domain.dto.TicketTypeRequest;

public class TicketTypeForm extends FormLayout {

    private final Binder<TicketTypeRequest> binder;
    private final TextField id;

    TicketTypeForm() {
        this.id = new TextField("Id");
        this.id.setVisible(false);
        this.id.setReadOnly(true);

        TextField name = new TextField("Name");
        name.setRequired(true);

        TextArea description = new TextArea("Description");
        description.setHelperText("Supports markdown");

        NumberField price = new NumberField("Price");
        price.setRequired(true);

        IntegerField totalAvailable = new IntegerField("Total Available");

        this.setAutoResponsive(true);
        this.setMaxColumns(2);
        this.setExpandColumns(true);
        this.setExpandFields(true);

        this.addFormRow(this.id);
        this.addFormRow(name);
        this.addFormRow(description);
        this.addFormRow(price, totalAvailable);

        this.binder = new Binder<>(TicketTypeRequest.class);
        this.binder
            .forField(this.id)
            .withConverter(
                str -> (str == null || str.isBlank()) ? null : UUID.fromString(str),
                uuid -> uuid == null ? "" : uuid.toString(),
                "Invalid UUID format")
            .bind(TicketTypeRequest.PROP_ID);
        this.binder.forField(name).asRequired("Name is required").bind(TicketTypeRequest.PROP_NAME);
        this.binder.forField(description).bind(TicketTypeRequest.PROP_DESCRIPTION);
        this.binder
            .forField(price)
            .asRequired("Price is required")
            .withValidator(p -> p >= 0, "Price must be positive")
            .bind(TicketTypeRequest.PROP_PRICE);
        this.binder.forField(totalAvailable).bind(TicketTypeRequest.PROP_TOTAL_AVAILABLE);
    }

    public Optional<TicketTypeRequest> getTicketType() {
        try {
            return Optional.of(this.binder.writeRecord());
        } catch (ValidationException e) {
            return Optional.empty();
        }
    }

    public void setTicketType(@Nullable TicketTypeRequest ticketType) {
        if (ticketType != null) {
            if (ticketType.id() != null) {
                this.id.setVisible(true);
            }
            this.binder.readRecord(ticketType);
        } else {
            this.binder.refreshFields();
        }
    }

}
