package app.k12onos.tickets.published_event.ui.components;

import java.util.UUID;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import app.k12onos.tickets.published_event.domain.responses.TicketTypeSummaryResponse;
import app.k12onos.tickets.security.domain.UserRoles;
import app.k12onos.tickets.security.ui.views.SignInView;
import app.k12onos.tickets.ticket_purchase.ui.views.TicketPurchaseView;

public class TicketTypeCard extends Card {

    public TicketTypeCard(
        UUID eventId,
        AuthenticationContext authenticationContext,
        TicketTypeSummaryResponse ticketType) {

        this.setWidthFull();

        this.setTitle(ticketType.name());
        this.setSubtitle(new Span("Price: " + ticketType.price()));

        if (!authenticationContext.isAuthenticated()) {
            Button signInButton = new Button("Sign In to Buy", _ -> SignInView.showSignInView());
            this.addToFooter(signInButton);
        } else if (!authenticationContext.hasRole(UserRoles.ATTENDEE)) {
            Button signInButton = new Button("Sign In as Attendee", _ -> SignInView.showSignInView());
            this.addToFooter(signInButton);
        } else {
            Span buyTickets = new Span("Buy Tickets");
            buyTickets.addClassNames(Background.CONTRAST_5, BorderRadius.SMALL);
            buyTickets.addClassNames(Padding.Horizontal.MEDIUM, Padding.Vertical.SMALL);
            RouterLink buyTicketsLink = TicketPurchaseView
                .createTicketPurchaseLink(buyTickets, eventId, ticketType.id());

            this.addToFooter(buyTicketsLink);
        }
    }

}
