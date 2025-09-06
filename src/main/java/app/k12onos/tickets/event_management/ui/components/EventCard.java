package app.k12onos.tickets.event_management.ui.components;

import java.util.UUID;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextAlignment;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import app.k12onos.tickets.base.utils.DateTimeUtil;
import app.k12onos.tickets.event.domain.enums.EventStatus;
import app.k12onos.tickets.event_management.domain.responses.EventSummaryResponse;
import app.k12onos.tickets.event_management.ui.views.EventFormView;

public class EventCard extends Card {

    private final ConfirmDialog confirmDeleteDialog;

    public EventCard(EventSummaryResponse event, Runnable onDeleteEventCallback) {
        this.setMaxWidth(616F, Unit.PIXELS);
        this.setWidthFull();
        this.addThemeVariants(CardVariant.LUMO_HORIZONTAL, CardVariant.LUMO_STRETCH_MEDIA);

        Icon media = VaadinIcon.PICTURE.create();
        media.addClassNames(Background.CONTRAST_20, Padding.Horizontal.XLARGE);
        this.setMedia(media);

        HorizontalLayout titleLayout = this.createTitleLayout(event.name(), event.status());

        HorizontalLayout venueLayout = this
            .createDetailLayout(event.venue(), false, "Venue", VaadinIcon.MAP_MARKER.create());

        String time = DateTimeUtil.formatDateTime(event.start(), event.end(), true);
        boolean timeIsItalic = event.start() == null || event.end() == null;
        HorizontalLayout timeLayout = this
            .createDetailLayout(time, timeIsItalic, "Event Time", VaadinIcon.CLOCK.create());

        String salesTime = DateTimeUtil.formatDateTime(event.salesStart(), event.salesEnd(), true);
        boolean salesTimeIsItalic = event.salesStart() == null || event.salesEnd() == null;
        HorizontalLayout salesTimeLayout = this
            .createDetailLayout(salesTime, salesTimeIsItalic, "Ticket Sales Time", VaadinIcon.TICKET.create());

        this.confirmDeleteDialog = this.createConfirmDeleteDialog(event.name());
        this.confirmDeleteDialog.addConfirmListener(_ -> onDeleteEventCallback.run());

        this.add(titleLayout, venueLayout, timeLayout, salesTimeLayout);
        this.addToFooter(this.createFooterLayout(event.id()));
    }

    private HorizontalLayout createTitleLayout(String name, EventStatus status) {
        H4 nameHeader = new H4(name);

        Span statusSpan = new Span(status.toString());
        String badgeType = switch (status) {
            case EventStatus.PUBLISHED -> "badge success";
            case EventStatus.DRAFT -> "badge contrast";
            default -> "badge";
        };
        statusSpan.getElement().getThemeList().add(badgeType);

        HorizontalLayout titleLayout = new HorizontalLayout(nameHeader, statusSpan);
        titleLayout.setWidthFull();
        titleLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.setPadding(false);
        titleLayout.addClassNames(Display.BLOCK, Display.Breakpoint.Small.FLEX);

        return titleLayout;
    }

    private HorizontalLayout createDetailLayout(String value, boolean isItalic, String subject, Icon icon) {
        Span detail = new Span(value);
        detail.addClassNames(TextColor.TERTIARY, FontSize.SMALL);
        if (isItalic) {
            detail.getStyle().set("font style", "italic");
        }

        VerticalLayout detailSubLayout = new VerticalLayout(new Text(subject), detail);
        detailSubLayout.setSpacing(false);
        detailSubLayout.setPadding(false);
        detailSubLayout.setMargin(false);

        icon.addClassNames(Display.HIDDEN, Display.Breakpoint.Small.FLEX);

        HorizontalLayout venueLayout = new HorizontalLayout(icon, detailSubLayout);
        venueLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        venueLayout.setPadding(false);
        venueLayout.setMargin(false);
        venueLayout.addClassNames(Padding.Top.XSMALL);

        return venueLayout;
    }

    private ConfirmDialog createConfirmDeleteDialog(String name) {
        ConfirmDialog confirmDeleteDialog = new ConfirmDialog();
        confirmDeleteDialog.setText("Are you sure you want to delete \"" + name + "\"?");
        confirmDeleteDialog.setHeader("Delete event?");
        confirmDeleteDialog.setConfirmText("Delete");
        confirmDeleteDialog.setConfirmButtonTheme("error primary");
        confirmDeleteDialog.setCancelable(true);
        return confirmDeleteDialog;
    }

    private HorizontalLayout createFooterLayout(UUID id) {
        RouterLink editButton = EventFormView.createEventFormLink("Edit", id);
        editButton.addClassNames(Background.PRIMARY_10, BorderRadius.SMALL);
        editButton.addClassNames(Padding.Vertical.XSMALL, Padding.Horizontal.LARGE);
        editButton.addClassNames(TextColor.PRIMARY, TextAlignment.CENTER);

        Button deleteButton = new Button("Delete", _ -> this.confirmDeleteDialog.open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClassNames(Background.ERROR_10, Margin.NONE);

        HorizontalLayout footerLayout = new HorizontalLayout(editButton, deleteButton);
        footerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        footerLayout.setPadding(false);
        footerLayout.setMargin(false);
        footerLayout.setWidthFull();
        footerLayout.setFlexGrow(1, editButton);
        footerLayout.setFlexGrow(1, deleteButton);

        return footerLayout;
    }

}
