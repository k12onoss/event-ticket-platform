package app.k12onos.tickets.base.ui.components;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel.PageMetadata;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

public class PaginationControls extends HorizontalLayout {

    private int currentPage = 1;
    private int totalPageCount = 1;

    private final Select<Integer> pageSizeSelect;
    private final Span currentPageLabel;
    private final Button firstPageButton;
    private final Button lastPageButton;
    private final Button goToPreviousPageButton;
    private final Button goToNextPageButton;

    private SerializableConsumer<Pageable> pageChangedListener;

    public PaginationControls() {
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.setSpacing("0.3rem");
        this.setWidthFull();

        Span pageSizeLable = new Span("Page size");
        pageSizeLable.setId("page-size-label");
        pageSizeLable.addClassNames(FontSize.SMALL, Display.HIDDEN, Display.Breakpoint.Small.FLEX);

        this.pageSizeSelect = new Select<>();
        this.pageSizeSelect.addThemeVariants(SelectVariant.LUMO_SMALL);
        this.pageSizeSelect.getStyle().set("--vaadin-input-field-value-font-size", "var(--lumo-font-size-s)");
        this.pageSizeSelect.setWidth("4.8rem");
        this.pageSizeSelect.setItems(8, 20, 60, 100);
        this.pageSizeSelect.addValueChangeListener(e -> {
            if (e.isFromClient()) {
                this.pageChanged();
            }
        });
        this.pageSizeSelect.setAriaLabelledBy("page-size-label");

        this.addToStart(pageSizeLable, this.pageSizeSelect);

        this.firstPageButton = this
            .createIconButton(VaadinIcon.ANGLE_DOUBLE_LEFT, "Go to first page", () -> this.currentPage = 1);

        this.firstPageButton.addClassNames(Display.HIDDEN, Display.Breakpoint.Small.FLEX);

        this.lastPageButton = this
            .createIconButton(
                VaadinIcon.ANGLE_DOUBLE_RIGHT,
                "Go to last page",
                () -> this.currentPage = this.totalPageCount);

        this.lastPageButton.addClassNames(Display.HIDDEN, Display.Breakpoint.Small.FLEX);

        this.currentPageLabel = new Span();
        this.currentPageLabel.addClassNames(FontSize.SMALL, Padding.Horizontal.SMALL);

        this.goToPreviousPageButton = this
            .createIconButton(VaadinIcon.ANGLE_LEFT, "Go to previous page", () -> this.currentPage--);

        this.goToNextPageButton = this
            .createIconButton(VaadinIcon.ANGLE_RIGHT, "Go to next page", () -> this.currentPage++);

        this
            .addToEnd(
                this.firstPageButton,
                this.goToPreviousPageButton,
                this.currentPageLabel,
                this.goToNextPageButton,
                this.lastPageButton);
    }

    public void setPageChangedListner(SerializableConsumer<Pageable> pageChangedListener) {
        this.pageChangedListener = pageChangedListener;
    }

    public void setValue(PageMetadata pageMetadata) {
        this.currentPage = (int) pageMetadata.number() + 1;
        this.totalPageCount = (int) pageMetadata.totalPages();

        this.pageSizeSelect.setValue((int) pageMetadata.size());
        this.currentPageLabel.setText(String.format("%d of %d", this.currentPage, this.totalPageCount));
        this.firstPageButton.setEnabled(this.currentPage > 1);
        this.lastPageButton.setEnabled(this.currentPage < this.totalPageCount);
        this.goToPreviousPageButton.setEnabled(this.currentPage > 1);
        this.goToNextPageButton.setEnabled(this.currentPage < this.totalPageCount);
    }

    private Button createIconButton(VaadinIcon icon, String ariaLabel, Runnable onClickListener) {
        Button button = new Button(new Icon(icon));
        button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
        button.addClickListener(_ -> {
            onClickListener.run();
            this.pageChanged();
        });
        button.setAriaLabel(ariaLabel);
        return button;
    }

    private void pageChanged() {
        Pageable pageable = Pageable.ofSize(this.pageSizeSelect.getValue()).withPage(this.currentPage - 1);

        if (this.pageChangedListener != null) {
            this.pageChangedListener.accept(pageable);
        }
    }

}
