package app.k12onos.tickets.event_management.ui.components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.LineHeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.TextOverflow;

import app.k12onos.tickets.event_management.domain.dto.TicketTypeRequest;

public class TicketTypeCard extends Card {

    private SerializableConsumer<TicketTypeRequest> editListner;
    private Runnable onDeleteCallback;

    private final Markdown description;
    private final Span price;
    private final TicketTypeForm ticketTypeForm;
    private final ConfirmDialog confirmDeleteDialog;

    public TicketTypeCard() {
        this.addThemeVariants(CardVariant.LUMO_OUTLINED);
        this.setWidthFull();
        this.setMaxWidth(616F, Unit.PIXELS);

        this.description = new Markdown();
        this.description.addClassNames(LineHeight.XSMALL, Overflow.HIDDEN, TextOverflow.ELLIPSIS);
        this.add(this.description);

        this.price = new Span();
        this.price.getElement().getThemeList().add("badge success");
        this.setHeaderSuffix(this.price);

        this.confirmDeleteDialog = this.getConfirmDeleteDialog();
        this.ticketTypeForm = new TicketTypeForm();
        Dialog editDialog = this.createEditDialog();
        this.addToFooter(this.createActionButtonsLayout(editDialog));
    }

    public void setValue(TicketTypeRequest ticketType) {
        this.setTitle(ticketType.name());

        this.setSubtitle(new Span("Total tickets: " + ticketType.totalAvailable()));

        this.description.setContent(ticketType.description());

        this.price.setText(ticketType.price().toString());

        this.ticketTypeForm.setTicketType(ticketType);

        this.confirmDeleteDialog.setText("Are you sure you want to delete \"" + ticketType.name() + "\"?");
    }

    public void setEditListner(SerializableConsumer<TicketTypeRequest> editListner) {
        this.editListner = editListner;
    }

    public void setOnDeleteCallback(Runnable onDeleteCallback) {
        this.onDeleteCallback = onDeleteCallback;
    }

    private Dialog createEditDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Ticket Type");

        dialog.add(this.ticketTypeForm);

        Button save = new Button("Save", _ -> {
            this.ticketTypeForm.getTicketType().ifPresent(savedTicketType -> {
                if (this.editListner != null) {
                    this.editListner.accept(savedTicketType);
                }
                dialog.close();
            });
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancel = new Button("Cancel", _ -> dialog.close());

        dialog.getFooter().add(cancel, save);

        return dialog;
    }

    private ConfirmDialog getConfirmDeleteDialog() {
        ConfirmDialog confirmDeleteDialog = new ConfirmDialog();
        confirmDeleteDialog.setHeader("Delete ticket type?");
        confirmDeleteDialog.setConfirmText("Delete");
        confirmDeleteDialog.setConfirmButtonTheme("error primary");
        confirmDeleteDialog.addConfirmListener(_ -> {
            if (this.onDeleteCallback != null) {
                this.onDeleteCallback.run();
            }
        });
        confirmDeleteDialog.setCancelable(true);

        return confirmDeleteDialog;
    }

    private HorizontalLayout createActionButtonsLayout(Dialog editDialog) {
        Button editButton = new Button("Edit", _ -> editDialog.open());
        editButton.addClassNames(Margin.NONE, Background.PRIMARY_10);

        Button deleteButton = new Button("Delete", _ -> this.confirmDeleteDialog.open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClassNames(Margin.NONE, Background.ERROR_10);

        HorizontalLayout actionButtons = new HorizontalLayout(editButton, deleteButton);
        actionButtons.setWidthFull();
        actionButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        actionButtons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        actionButtons.setPadding(false);
        actionButtons.setFlexGrow(1, editButton);
        actionButtons.setFlexGrow(1, deleteButton);

        return actionButtons;
    }

}
