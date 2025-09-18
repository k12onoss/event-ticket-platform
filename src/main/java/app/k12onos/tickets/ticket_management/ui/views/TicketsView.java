package app.k12onos.tickets.ticket_management.ui.views;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;

import app.k12onos.tickets.base.ui.components.PaginationControls;
import app.k12onos.tickets.security.domain.UserAdapter;
import app.k12onos.tickets.security.domain.UserRoles;
import app.k12onos.tickets.ticket_management.domain.responses.TicketResponse;
import app.k12onos.tickets.ticket_management.services.QrCodeService;
import app.k12onos.tickets.ticket_management.services.TicketService;
import app.k12onos.tickets.ticket_management.ui.components.TicketCard;
import jakarta.annotation.security.RolesAllowed;

@Route("my_tickets")
@PageTitle("My tickets")
@RolesAllowed(UserRoles.ATTENDEE)
public class TicketsView extends VerticalLayout implements AfterNavigationObserver {

    private final transient AuthenticationContext authenticationContext;
    private final TicketService ticketService;
    private final QrCodeService qrCodeService;

    private final PaginationControls paginationControls;
    private final HorizontalLayout mainLayout;

    TicketsView(AuthenticationContext authenticationContext, TicketService ticketService, QrCodeService qrCodeService) {
        this.authenticationContext = authenticationContext;
        this.ticketService = ticketService;
        this.qrCodeService = qrCodeService;

        this.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO);

        this.paginationControls = new PaginationControls(List.of(6, 14, 20, 50, 100));
        this.paginationControls.setPageChangedListner(this::updateTickets);

        this.mainLayout = new HorizontalLayout();
        this.mainLayout.setWrap(true);
        this.mainLayout.setPadding(false);
        this.mainLayout.setWidthFull();

        this.add(this.paginationControls, this.mainLayout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        this.mainLayout.removeAll();
        this.updateTickets(Pageable.ofSize(6).withPage(0));
    }

    private void updateTickets(Pageable pageable) {
        UUID userId = this.authenticationContext.getAuthenticatedUser(UserAdapter.class).get().getUser().id();

        PagedModel<TicketResponse> tickets = this.ticketService.getTicketsForPurchaser(userId, pageable);

        this.paginationControls.setValue(tickets.getMetadata());

        tickets.getContent().stream().forEach(ticket -> {
            TicketCard card = new TicketCard(
                ticket,
                () -> this.qrCodeService.getActiveQrCodeForTicketAndPurchaser(ticket.id(), userId));

            this.mainLayout.add(card);
        });
    }

    public static RouterLink createTicketsViewLink() {
        return new RouterLink("My Tickets", TicketsView.class);
    }

}
