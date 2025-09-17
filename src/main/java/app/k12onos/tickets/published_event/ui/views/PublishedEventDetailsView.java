package app.k12onos.tickets.published_event.ui.views;

import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import app.k12onos.tickets.base.utils.DateTimeUtil;
import app.k12onos.tickets.base.utils.InMemoryUtil;
import app.k12onos.tickets.event_management.exceptions.EventNotFoundException;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventResponse;
import app.k12onos.tickets.published_event.services.PublishedEventService;
import app.k12onos.tickets.published_event.ui.components.TicketTypeCard;

@Route("event")
@AnonymousAllowed
public class PublishedEventDetailsView extends HorizontalLayout implements HasUrlParameter<String>, HasDynamicTitle {

    private String title;

    private final transient AuthenticationContext authenticationContext;
    private final PublishedEventService publishedEventService;

    private final Card image;
    private final H2 eventName;
    private final HorizontalLayout eventDate;
    private final HorizontalLayout eventVenue;
    private final VerticalLayout ticketTypeCards;

    PublishedEventDetailsView(
        AuthenticationContext authenticationContext,
        PublishedEventService publishedEventsService) {

        this.title = "Event Details";

        this.authenticationContext = authenticationContext;
        this.publishedEventService = publishedEventsService;

        this.setPadding(true);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO);
        this.addClassNames(Display.BLOCK, Display.Breakpoint.Medium.FLEX);

        this.image = new Card();
        this.eventName = new H2();
        this.eventDate = new HorizontalLayout(VaadinIcon.CALENDAR_O.create());
        this.eventVenue = new HorizontalLayout(VaadinIcon.MAP_MARKER.create());

        this.ticketTypeCards = new VerticalLayout();
        this.ticketTypeCards.setPadding(false);
        this.ticketTypeCards.setMaxWidth(411F, Unit.PIXELS);

        this.add(this.createEventDetailsLayout(), this.ticketTypeCards);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        UUID id = UUID.fromString(parameter);
        this.publishedEventService.getPublishedEvent(id).ifPresentOrElse(event -> {
            this.title = event.name();
            this.updateDetails(event);
        }, () -> {
            throw new EventNotFoundException();
        });
    }

    @Override
    public String getPageTitle() {
        return this.title;
    }

    private void updateDetails(PublishedEventResponse event) {
        this.eventName.setText(event.name());

        if (event.bannerUrl() != null) {
            Image image = new Image(event.bannerUrl(), "banner");
            image.addClassNames(BorderRadius.LARGE);
            this.image.setMedia(image);
        }
        this.eventDate.add(new Span(DateTimeUtil.formatDateTime(event.start(), event.end(), false)));
        this.eventVenue.add(new Span(event.venue()));

        event.ticketTypes().forEach(ticketType -> {
            InMemoryUtil.ticketTypesMap.putIfAbsent(ticketType.id(), ticketType);
            TicketTypeCard ticketTypeCard = new TicketTypeCard(event.id(), this.authenticationContext, ticketType);
            this.ticketTypeCards.add(ticketTypeCard);
        });
    }

    private VerticalLayout createEventDetailsLayout() {
        this.image.addClassNames(Padding.Bottom.NONE);
        this.image.setMedia(VaadinIcon.PICTURE.create());
        this.image.addThemeVariants(CardVariant.LUMO_COVER_MEDIA);
        this.image.setWidthFull();

        VerticalLayout layout = new VerticalLayout(this.image, this.eventName, this.eventDate, this.eventVenue);
        layout.setPadding(false);
        layout.setMaxWidth(821F, Unit.PIXELS);

        return layout;
    }

    public static RouterLink createPublishedEventLink(Component component, UUID id) {
        RouterLink link = new RouterLink();
        link.add(component);
        link.setRoute(PublishedEventDetailsView.class, id.toString());

        return link;
    }

    public static void showPublishedEventView(UUID id) {
        UI.getCurrent().navigate(PublishedEventDetailsView.class, id.toString());
    }

}
