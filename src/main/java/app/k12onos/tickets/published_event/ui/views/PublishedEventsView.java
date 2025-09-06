package app.k12onos.tickets.published_event.ui.views;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;

import app.k12onos.tickets.base.ui.components.PaginationControls;
import app.k12onos.tickets.base.ui.components.SearchField;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventSummaryResponse;
import app.k12onos.tickets.published_event.services.PublishedEventService;
import app.k12onos.tickets.published_event.ui.components.EventCard;

@Route("")
@PageTitle("Events")
@AnonymousAllowed
public class PublishedEventsView extends VerticalLayout implements AfterNavigationObserver {

    private final PublishedEventService publishedEventService;

    private final PaginationControls paginationControls;
    private final HorizontalLayout eventsLayout;

    PublishedEventsView(PublishedEventService publishedEventService) {
        this.publishedEventService = publishedEventService;

        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO);

        SearchField searchField = new SearchField();
        searchField.setSearchListner(searchQuery -> this.updateEvents(searchQuery, Pageable.ofSize(8).withPage(0)));

        this.paginationControls = new PaginationControls(List.of(8, 20, 60, 100));
        this.paginationControls.setPageChangedListner(pageable -> this.updateEvents(searchField.getValue(), pageable));
        this.paginationControls.addToMiddle(searchField);

        this.eventsLayout = new HorizontalLayout(JustifyContentMode.CENTER);
        this.eventsLayout.setWrap(true);
        this.eventsLayout.setPadding(false);
        this.eventsLayout.setMargin(false);
        this.eventsLayout.setWidthFull();

        this.add(this.paginationControls, this.eventsLayout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent navigationEvent) {
        this.updateEvents(null, Pageable.ofSize(20).withPage(0));
    }

    private void updateEvents(String searchQuery, Pageable pageable) {
        PagedModel<PublishedEventSummaryResponse> publishedEvents;
        if (searchQuery != null && !searchQuery.isBlank()) {
            publishedEvents = this.publishedEventService.searchPublishedEvent(searchQuery, pageable);
        } else {
            publishedEvents = this.publishedEventService.getPublishedEvents(pageable);
        }

        this.paginationControls.setValue(publishedEvents.getMetadata());

        this.eventsLayout.removeAll();
        publishedEvents.getContent().forEach(publishedEvent -> {
            EventCard card = new EventCard(publishedEvent);
            this.eventsLayout.add(card);
        });
    }

}
