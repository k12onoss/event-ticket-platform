package app.k12onos.tickets.base.ui.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import app.k12onos.tickets.event.exceptions.DeleteFileFailedException;
import app.k12onos.tickets.event.exceptions.GeneratePresignedUrlFailedException;
import app.k12onos.tickets.event_management.exceptions.EventNotFoundException;
import app.k12onos.tickets.event_management.exceptions.GetImageFailedException;
import app.k12onos.tickets.event_management.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.event_management.exceptions.UploadImageFailedException;
import app.k12onos.tickets.security.exceptions.UserNotFoundException;
import app.k12onos.tickets.ticket.exceptions.QrCodeGenerationException;
import app.k12onos.tickets.ticket.exceptions.QrCodeNotFoundException;
import app.k12onos.tickets.ticket_purchase.exceptions.TicketsSoldOutException;

@Configuration
public class MainErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MainErrorHandler.class);

    @Bean
    VaadinServiceInitListener errorHandlerInitializer() {
        return event -> event
            .getSource()
            .addSessionInitListener(
                sessionEvent -> sessionEvent.getSession().setErrorHandler(MainErrorHandler::errorHandler));
    }

    private static void errorHandler(ErrorEvent errorEvent) {
        Throwable throwable = errorEvent.getThrowable();

        while (throwable.getCause() != null && (throwable instanceof RuntimeException)) {
            throwable = throwable.getCause();
        }

        LOG.error("Caught " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage(), throwable);

        String message = switch (throwable) {
            case UserNotFoundException _ -> "User not found";
            case EventNotFoundException _ -> "Event not found";
            case TicketTypeNotFoundException _ -> "Ticket type not found";
            case QrCodeNotFoundException _ -> "QR code not found";
            case QrCodeGenerationException _ -> "Cannot generate QR code";
            case TicketsSoldOutException _ -> "Tickets are sold out";
            case GeneratePresignedUrlFailedException _,UploadImageFailedException _ -> "Image upload failed";
            case GetImageFailedException _ -> "Image request failed";
            case DeleteFileFailedException _ -> "Deleting image failed";
            default -> "An unexpected error has occurred. Please try again later.";
        };

        errorEvent.getComponent().flatMap(Component::getUI).ifPresent(ui -> {
            var notification = new Notification(message);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.setDuration(3000);
            ui.access(notification::open);
        });
    }

}
