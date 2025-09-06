package app.k12onos.tickets.event_management.ui.views;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import app.k12onos.tickets.base.ui.components.PaginationControls;
import app.k12onos.tickets.event_management.domain.responses.EventSummaryResponse;
import app.k12onos.tickets.event_management.services.EventService;
import app.k12onos.tickets.event_management.ui.components.EventCard;
import app.k12onos.tickets.security.domain.UserAdapter;
import app.k12onos.tickets.security.domain.UserRoles;
import jakarta.annotation.security.RolesAllowed;

@Route("my_events")
@PageTitle("My Events")
@RolesAllowed(UserRoles.ORGANIZER)
public class EventsView extends VerticalLayout implements AfterNavigationObserver {

    private final transient AuthenticationContext authenticationContext;
    private final EventService eventService;

    private final PaginationControls paginationControls;
    private final HorizontalLayout eventsLayout;
    private final Notification deleteNotification;

    EventsView(AuthenticationContext authenticationContext, EventService eventsService) {
        this.authenticationContext = authenticationContext;
        this.eventService = eventsService;

        this.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO);

        RouterLink createEventButton = EventFormView.createEventFormLink("Create Event", null);
        createEventButton.addClassNames(Background.PRIMARY, BorderRadius.SMALL);
        createEventButton.addClassNames(Padding.Vertical.XSMALL, Padding.Horizontal.MEDIUM);
        createEventButton.addClassNames(TextColor.PRIMARY_CONTRAST, FontWeight.SEMIBOLD, TextAlignment.CENTER);

        this.paginationControls = new PaginationControls(List.of(6, 20, 50, 100));
        this.paginationControls.setPageChangedListner(this::updateEvents);
        this.paginationControls.addToEnd(createEventButton);

        this.eventsLayout = new HorizontalLayout(JustifyContentMode.CENTER);
        this.eventsLayout.setWrap(true);
        this.eventsLayout.setPadding(false);

        this.add(this.paginationControls, this.eventsLayout);

        this.deleteNotification = new Notification("Event deleted successfully");
        this.deleteNotification.setDuration(3000);
        this.deleteNotification.setPosition(Position.TOP_CENTER);
        this.deleteNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent navigationEvent) {
        this.updateEvents(Pageable.ofSize(6).withPage(0));
    }

    private void updateEvents(Pageable pageable) {
        UUID userId = this.authenticationContext.getAuthenticatedUser(UserAdapter.class).get().getUser().id();

        PagedModel<EventSummaryResponse> events = this.eventService.getEventsByOrganizer(userId, pageable);

        this.paginationControls.setValue(events.getMetadata());

        this.eventsLayout.removeAll();
        events.getContent().forEach(event -> {
            Runnable onDeleteCallback = () -> {
                this.eventService.deleteEventByOrganizer(userId, event.id());
                this.deleteNotification.close();
                this.deleteNotification.open();
                UI.getCurrent().getPage().reload();
            };
            EventCard card = new EventCard(event, onDeleteCallback);
            this.eventsLayout.add(card);
        });
    }

    public static RouterLink createEventsViewLink() {
        return new RouterLink("My Events", EventsView.class);
    }

    public static void showEventsView() {
        UI.getCurrent().navigate(EventsView.class);
    }

}
