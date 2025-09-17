package app.k12onos.tickets.published_event.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Span;

import app.k12onos.tickets.published_event.domain.responses.TicketTypeSummaryResponse;

public class TicketTypeCard extends Card {

    public TicketTypeCard(TicketTypeSummaryResponse ticketType) {
        this.setWidthFull();

        this.setTitle(ticketType.name());
        this.setSubtitle(new Span("Price: " + ticketType.price()));

        Button buyButton = new Button("Buy Tickets");

        this.addToFooter(buyButton);
    }

}
