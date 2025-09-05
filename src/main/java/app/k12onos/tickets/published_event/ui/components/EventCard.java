package app.k12onos.tickets.published_event.ui.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import app.k12onos.tickets.base.utils.DateTimeUtil;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventSummaryResponse;

public class EventCard extends Card {

    public EventCard(PublishedEventSummaryResponse event) {
        this.setMaxWidth(300F, Unit.PIXELS);
        this.addClassNames(Margin.NONE, Width.FULL);
        this.addThemeVariants(CardVariant.LUMO_STRETCH_MEDIA);

        this.setMedia(LumoIcon.PHOTO.create());
        this.setTitle(event.name());
        this.setSubtitle(new Span(event.venue()));
        this.add(DateTimeUtil.formatDateTime(event.start(), event.end(), true));
    }

}
