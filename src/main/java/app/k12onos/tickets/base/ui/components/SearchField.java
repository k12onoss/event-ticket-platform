package app.k12onos.tickets.base.ui.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;

public class SearchField extends TextField {

    public SearchField() {
        this.setPrefixComponent(VaadinIcon.SEARCH.create());
        this.setPlaceholder("Search");
        this.setClearButtonVisible(true);
        this.addClassNames(MaxWidth.SCREEN_MEDIUM, Flex.GROW, Display.FLEX);
    }

    public void setSearchListner(SerializableConsumer<String> searchListner) {
        this.addValueChangeListener(searchValue -> searchListner.accept(searchValue.getValue()));
    }

}
