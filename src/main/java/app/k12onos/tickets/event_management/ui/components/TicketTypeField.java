package app.k12onos.tickets.event_management.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import app.k12onos.tickets.event_management.domain.dto.TicketTypeRequest;

public class TicketTypeField extends CustomField<List<TicketTypeRequest>> {

    private final List<TicketTypeRequest> ticketTypeRequests;

    private final HorizontalLayout ticketTypesLayout;

    public TicketTypeField() {
        this.ticketTypeRequests = new ArrayList<>();

        this.ticketTypesLayout = new HorizontalLayout();
        this.ticketTypesLayout.setMinHeight(144F, Unit.PIXELS);
        this.ticketTypesLayout.setWrap(true);
        this.ticketTypesLayout.setPadding(false);
        this.ticketTypesLayout.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(this.createHeader(), this.ticketTypesLayout);
        mainLayout.setPadding(false);
        mainLayout.addClassNames(Padding.Vertical.MEDIUM);

        this.add(mainLayout);
    }

    @Override
    protected List<TicketTypeRequest> generateModelValue() {
        return this.ticketTypeRequests;
    }

    @Override
    protected void setPresentationValue(List<TicketTypeRequest> ticketTypes) {
        this.ticketTypeRequests.clear();
        this.ticketTypeRequests.addAll(ticketTypes);

        this.ticketTypesLayout.removeAll();
        ticketTypes.forEach(this::addTicketTypeCard);
    }

    private HorizontalLayout createHeader() {
        Button addTicketButton = new Button("Add Ticket Type", _ -> this.createTicketTypeDialog().open());

        HorizontalLayout header = new HorizontalLayout(new H3("Ticket Types"), addTicketButton);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();

        return header;
    }

    private Dialog createTicketTypeDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Ticket Type");

        TicketTypeForm form = new TicketTypeForm();
        dialog.add(form);

        Button save = new Button("Save", _ -> {
            form.getTicketType().ifPresent(savedTicketType -> {
                this.ticketTypeRequests.add(savedTicketType);
                this.addTicketTypeCard(savedTicketType);
                this.setModelValue(this.generateModelValue(), true);
                dialog.close();
            });
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", _ -> dialog.close());

        dialog.getFooter().add(cancel, save);

        return dialog;
    }

    private void addTicketTypeCard(TicketTypeRequest ticketType) {
        TicketTypeCard card = new TicketTypeCard();
        card.setEditListner(editedTicketType -> {
            this.ticketTypeRequests.remove(ticketType);
            this.ticketTypeRequests.add(editedTicketType);
            card.setValue(editedTicketType);
            this.setModelValue(this.generateModelValue(), true);
        });
        card.setOnDeleteCallback(() -> {
            this.ticketTypeRequests.remove(ticketType);
            this.ticketTypesLayout.remove(card);
            this.setModelValue(this.generateModelValue(), true);
        });
        card.setValue(ticketType);

        this.ticketTypesLayout.add(card);
    }

}
