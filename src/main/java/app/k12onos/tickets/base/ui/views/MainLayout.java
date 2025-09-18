package app.k12onos.tickets.base.ui.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.IconSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import app.k12onos.tickets.event_management.ui.views.EventsView;
import app.k12onos.tickets.security.domain.UserAdapter;
import app.k12onos.tickets.security.domain.UserRoles;
import app.k12onos.tickets.security.domain.dto.UserDto;
import app.k12onos.tickets.security.ui.views.SignInView;
import app.k12onos.tickets.ticket_management.ui.views.TicketsView;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final transient AuthenticationContext authenticationContext;

    MainLayout(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;

        this.setPrimarySection(Section.NAVBAR);

        HorizontalLayout navbarLayout = new HorizontalLayout(Alignment.CENTER);
        navbarLayout.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO, Padding.Horizontal.MEDIUM);

        navbarLayout.addToStart(this.createHeader());
        authenticationContext
            .getAuthenticatedUser(UserAdapter.class)
            .ifPresentOrElse(
                userAdapter -> navbarLayout.addToEnd(this.createUserMenu(userAdapter.getUser())),
                () -> navbarLayout.addToEnd(this.createSignInButton()));

        this.addToNavbar(navbarLayout);
    }

    private HorizontalLayout createHeader() {
        Icon appIcon = VaadinIcon.CUBES.create();
        appIcon.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

        Span appName = new Span("Tickets");
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        HorizontalLayout header = new HorizontalLayout(appIcon, appName);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return header;
    }

    private Button createSignInButton() {
        return new Button("Sign in", _ -> SignInView.showSignInView());
    }

    private MenuBar createUserMenu(UserDto user) {
        Avatar avatar = new Avatar(user.name());
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
        avatar.addClassNames(Margin.Right.SMALL);
        avatar.setColorIndex(5);

        MenuBar userMenu = new MenuBar();
        userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        userMenu.addClassNames(Margin.MEDIUM);

        MenuItem userMenuItem = userMenu.addItem(avatar);
        userMenuItem.add(user.name());

        if (this.authenticationContext.hasRole(UserRoles.ORGANIZER)) {
            userMenuItem.getSubMenu().addItem(EventsView.createEventsViewLink());
        } else if (this.authenticationContext.hasRole(UserRoles.ATTENDEE)) {
            userMenuItem.getSubMenu().addItem(TicketsView.createTicketsViewLink());
        }

        userMenuItem.getSubMenu().addItem("Logout", _ -> this.authenticationContext.logout());

        return userMenu;
    }

}
