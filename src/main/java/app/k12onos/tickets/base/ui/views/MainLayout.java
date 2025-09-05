package app.k12onos.tickets.base.ui.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.IconSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    MainLayout() {
        this.setPrimarySection(Section.NAVBAR);

        HorizontalLayout navbarLayout = new HorizontalLayout(this.createHeader());
        navbarLayout.addClassNames(Flex.GROW, MaxWidth.SCREEN_XLARGE, Margin.AUTO);
        this.addToNavbar(navbarLayout);
    }

    private HorizontalLayout createHeader() {
        Icon appIcon = VaadinIcon.CUBES.create();
        appIcon.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

        Span appName = new Span("Tickets");
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        HorizontalLayout header = new HorizontalLayout(appIcon, appName);
        header.setPadding(true);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return header;
    }

}
