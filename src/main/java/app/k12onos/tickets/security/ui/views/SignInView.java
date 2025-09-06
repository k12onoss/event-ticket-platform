package app.k12onos.tickets.security.ui.views;

import com.vaadin.flow.component.UI;

public class SignInView {

    public static final String path = "/oauth2/authorization/keycloak";

    public static void showSignInView() {
        UI.getCurrent().getPage().setLocation(path);
    }

}
