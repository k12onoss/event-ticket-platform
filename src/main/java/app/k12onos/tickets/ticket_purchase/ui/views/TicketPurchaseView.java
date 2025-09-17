package app.k12onos.tickets.ticket_purchase.ui.views;

import java.util.Map;
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;

import app.k12onos.tickets.base.utils.InMemoryUtil;
import app.k12onos.tickets.published_event.domain.responses.TicketTypeSummaryResponse;
import app.k12onos.tickets.published_event.ui.views.PublishedEventDetailsView;
import app.k12onos.tickets.security.domain.UserAdapter;
import app.k12onos.tickets.security.domain.UserRoles;
import app.k12onos.tickets.ticket_purchase.services.TicketPurchaseService;
import jakarta.annotation.security.RolesAllowed;

@Route("event/:" + TicketPurchaseView.PARAM_EVENT_ID + "/purchase/:" + TicketPurchaseView.PARAM_TICKET_TYPE_ID)
@PageTitle("Purchase")
@RolesAllowed(UserRoles.ATTENDEE)
public class TicketPurchaseView extends VerticalLayout implements BeforeEnterObserver {

    public static final String PARAM_EVENT_ID = "eventId";
    public static final String PARAM_TICKET_TYPE_ID = "ticketTypeId";

    private final transient AuthenticationContext authenticationContext;
    private final TicketPurchaseService ticketPurchaseService;

    private final Card ticketTypeCard;
    private final Markdown description;
    private final Span price;
    private final Button purchaseButton;

    public TicketPurchaseView(
        AuthenticationContext authenticationContext,
        TicketPurchaseService ticketPurchaseService) {
        this.authenticationContext = authenticationContext;
        this.ticketPurchaseService = ticketPurchaseService;

        this.setSizeFull();
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.addClassNames(MaxWidth.SCREEN_XLARGE, Margin.AUTO);

        this.ticketTypeCard = new Card();
        this.ticketTypeCard.setWidthFull();
        this.ticketTypeCard.addClassNames(MaxWidth.SCREEN_SMALL, Margin.AUTO);

        this.description = new Markdown();
        this.ticketTypeCard.add(this.description);

        this.price = new Span();
        this.price.getElement().getThemeList().add("badge");
        this.ticketTypeCard.setHeaderSuffix(this.price);

        this.purchaseButton = new Button("Purchase");
        this.purchaseButton.setWidthFull();
        this.purchaseButton.setDisableOnClick(true);
        this.purchaseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        this.ticketTypeCard.addToFooter(this.purchaseButton);

        this.add(this.ticketTypeCard);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UUID userId = this.authenticationContext.getAuthenticatedUser(UserAdapter.class).get().getUser().id();
        UUID eventId = UUID.fromString(event.getRouteParameters().get(PARAM_EVENT_ID).get());
        UUID ticketTypeId = UUID.fromString(event.getRouteParameters().get(PARAM_TICKET_TYPE_ID).get());
        TicketTypeSummaryResponse ticketType = InMemoryUtil.ticketTypesMap.get(ticketTypeId);

        this.ticketTypeCard.setTitle(ticketType.name());
        this.description.setContent(ticketType.description());
        this.price.setText(ticketType.price().toString());

        Notification notification = new Notification("Ticket purchased!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Position.TOP_CENTER);
        notification.setDuration(5000);

        this.purchaseButton.addClickListener(_ -> {
            this.ticketPurchaseService.purchaseTicket(userId, ticketTypeId);
            notification.open();
            PublishedEventDetailsView.showPublishedEventView(eventId);
        });
    }

    public static RouterLink createTicketPurchaseLink(Component component, UUID eventId, UUID ticketTypeId) {
        RouterLink link = new RouterLink();
        link.add(component);
        link
            .setRoute(
                TicketPurchaseView.class,
                new RouteParameters(
                    Map.of(PARAM_EVENT_ID, eventId.toString(), PARAM_TICKET_TYPE_ID, ticketTypeId.toString())));
        return link;
    }

}
