package app.k12onos.tickets.ticket_management.ui.components;

import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.IconSize;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextOverflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;

import app.k12onos.tickets.base.utils.DateTimeUtil;
import app.k12onos.tickets.ticket.domain.enums.TicketStatus;
import app.k12onos.tickets.ticket_management.domain.responses.QrCodeResponse;
import app.k12onos.tickets.ticket_management.domain.responses.TicketResponse;

public class TicketCard extends HorizontalLayout {

    public TicketCard(TicketResponse ticket, Supplier<QrCodeResponse> getQrCodeCallback) {
        this.setSpacing(false);
        this.setMaxWidth(616F, Unit.PIXELS);
        this.setWidthFull();
        this.setAlignItems(Alignment.START);
        this.addClassNames(Background.CONTRAST_5, BorderRadius.MEDIUM);

        String eventTime = DateTimeUtil.formatDateTime(ticket.eventStart(), ticket.eventEnd(), true);

        VerticalLayout mainLayout = new VerticalLayout(
            this.createTitleLayout(ticket.eventName(), ticket.status()),
            this.createDetailLayout(VaadinIcon.TAG.create(), ticket.ticketTypeName()),
            this.createDetailLayout(VaadinIcon.MAP_MARKER.create(), ticket.eventVenue()),
            this.createDetailLayout(VaadinIcon.MONEY.create(), ticket.price().toString()),
            this.createDetailLayout(VaadinIcon.CLOCK.create(), eventTime));

        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.setMinWidth("0");
        mainLayout.addClassNames(Padding.Vertical.SMALL, Padding.Right.SMALL);

        this.add(this.createMediaComponent(ticket.eventPosterUrl()), mainLayout);

        TicketDialog qrDialog = new TicketDialog(ticket);

        this.addClickListener(_ -> {
            qrDialog.open();
            QrCodeResponse qrCode = getQrCodeCallback.get();
            qrDialog.setQrCode(qrCode);
        });
    }

    private Component createMediaComponent(String posterUrl) {
        if (posterUrl != null) {
            Image poster = new Image(posterUrl, "poster");
            poster.setWidth(156F, Unit.PIXELS);
            poster.addClassNames(BorderRadius.MEDIUM);

            return poster;
        } else {
            Icon icon = LumoIcon.PHOTO.create();
            icon.setSize("156px");

            Div placeholder = new Div(icon);
            placeholder.setWidth(156F, Unit.PIXELS);
            placeholder.setHeightFull();
            placeholder.addClassNames(Background.CONTRAST_10, BorderRadius.MEDIUM);
            placeholder.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER);

            return placeholder;
        }
    }

    private HorizontalLayout createTitleLayout(String eventName, TicketStatus status) {
        Span statusSpan = new Span(status.toString());
        String badgeType = switch (status) {
            case TicketStatus.PURCHASED -> "badge success";
            case TicketStatus.CANCELLED -> "badge error";
            default -> "badge";
        };
        statusSpan.getElement().getThemeList().add(badgeType);

        HorizontalLayout titleLayout = new HorizontalLayout(new H3(eventName), statusSpan);
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.addClassNames(Display.BLOCK, Display.Breakpoint.Small.FLEX);

        return titleLayout;
    }

    private HorizontalLayout createDetailLayout(Icon icon, String detail) {
        icon.addClassNames(Display.HIDDEN, Display.Breakpoint.Small.FLEX, IconSize.SMALL);

        Span detailSpan = new Span(detail);
        detailSpan.addClassNames(Overflow.HIDDEN, TextOverflow.ELLIPSIS, Whitespace.NOWRAP, FontSize.SMALL);

        HorizontalLayout layout = new HorizontalLayout(icon, detailSpan);
        layout.setPadding(false);
        layout.setWidthFull();

        return layout;
    }

}
