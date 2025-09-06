package app.k12onos.tickets.event_management.ui.views;

import java.util.UUID;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;

import app.k12onos.tickets.event_management.domain.dto.EventRequest;
import app.k12onos.tickets.event_management.exceptions.EventNotFoundException;
import app.k12onos.tickets.event_management.services.EventService;
import app.k12onos.tickets.event_management.ui.components.EventForm;
import app.k12onos.tickets.security.domain.UserAdapter;
import app.k12onos.tickets.security.domain.UserRoles;
import jakarta.annotation.security.RolesAllowed;

@Route("event_form")
@RolesAllowed(UserRoles.ORGANIZER)
public class EventFormView extends VerticalLayout implements HasDynamicTitle, HasUrlParameter<String> {

    private UUID eventIdForEdit;

    private final AuthenticationContext authenticationContext;
    private final EventService eventService;

    private final EventForm eventForm;

    public EventFormView(AuthenticationContext authenticationContext, EventService eventsService) {
        this.authenticationContext = authenticationContext;
        this.eventService = eventsService;

        this.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO);

        this.eventForm = new EventForm();
        this.add(this.eventForm);

        Button saveButton = new Button("Save Event", _ -> this.saveEvent());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel", _ -> EventsView.showEventsView());
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        this.add(buttonLayout);
    }

    @Override
    public String getPageTitle() {
        return this.eventIdForEdit != null ? "Edit Event" : "Create Event";
    }

    @Override
    public void setParameter(BeforeEvent navigationEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            this.eventIdForEdit = UUID.fromString(parameter);
            UUID userId = this.authenticationContext.getAuthenticatedUser(UserAdapter.class).get().getUser().id();
            this.eventService
                .getEventByOrganizer(userId, this.eventIdForEdit)
                .ifPresentOrElse(event -> this.eventForm.setEvent(EventRequest.from(event)), () -> {
                    throw new EventNotFoundException();
                });
        }
    }

    private void saveEvent() {
        Notification successNotification = new Notification();
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.setPosition(Notification.Position.TOP_CENTER);
        successNotification.setDuration(3000);

        UUID userId = this.authenticationContext.getAuthenticatedUser(UserAdapter.class).get().getUser().id();

        this.eventForm.getEvent().ifPresent(event -> {
            if (this.eventIdForEdit != null) {
                this.eventService.updateEventByOrganizer(userId, this.eventIdForEdit, event.toUpdateDto());
                successNotification.setText("Event updated successfully!");
            } else {
                this.eventService.createEvent(userId, event.toCreateDto());
                successNotification.setText("Event created successfully!");
            }

            successNotification.open();
            EventsView.showEventsView();
        });
    }

    public static RouterLink createEventFormLink(String label, UUID eventId) {
        RouterLink link;
        if (eventId != null) {
            link = new RouterLink(label, EventFormView.class, eventId.toString());
        } else {
            link = new RouterLink(label, EventFormView.class);
        }
        return link;
    }

}
