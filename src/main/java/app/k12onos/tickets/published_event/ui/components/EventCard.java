package app.k12onos.tickets.published_event.ui.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import app.k12onos.tickets.base.utils.DateTimeUtil;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventSummaryResponse;

public class EventCard extends Card {

    public EventCard(PublishedEventSummaryResponse event) {
        this.setWidthFull();
        this.setMaxWidth(300F, Unit.PIXELS);
        this.addClassNames(Margin.NONE);
        this.addThemeVariants(CardVariant.LUMO_STRETCH_MEDIA);
        this.getStyle().set("--vaadin-card-media-aspect-ratio", "3 / 4");

        if (event.posterUrl() != null) {
            this.setMedia(new Image(event.posterUrl(), "poster"));
        } else {
            this.setMedia(this.createPlaceholder());
        }
        this.setTitle(event.name());

        VerticalLayout subLayout = new VerticalLayout(
            new Span(event.venue()),
            new Span(DateTimeUtil.formatDateTime(event.start(), event.end(), true)));
        subLayout.setPadding(false);
        subLayout.setSpacing(false);
        this.setSubtitle(subLayout);
    }

    private Div createPlaceholder() {
        Icon icon = VaadinIcon.PICTURE.create();
        icon.setSize("180px");
        Div placeholder = new Div(icon);
        placeholder.setWidthFull();
        placeholder.getStyle().set("aspect-ratio", "3 / 4");
        placeholder.addClassNames(Background.CONTRAST_5);
        placeholder.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER);

        return placeholder;
    }

}
