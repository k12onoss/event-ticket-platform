package app.k12onos.tickets.ticket_management.ui.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.google.zxing.WriterException;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import app.k12onos.tickets.base.utils.DateTimeUtil;
import app.k12onos.tickets.ticket.exceptions.QrCodeGenerationException;
import app.k12onos.tickets.ticket_management.domain.responses.QrCodeResponse;
import app.k12onos.tickets.ticket_management.domain.responses.TicketResponse;
import app.k12onos.tickets.ticket_management.utils.QrCodeUtil;

public class TicketDialog extends Dialog {

    private final Image qrCodeImage;
    private final Span ticketId;

    TicketDialog(TicketResponse ticket) {
        this.setWidthFull();
        this.addClassNames(MaxWidth.SCREEN_MEDIUM);

        H4 eventName = new H4(ticket.eventName());

        Span eventTime = new Span(DateTimeUtil.formatDateTime(ticket.eventStart(), ticket.eventEnd(), true));

        Span eventVenue = new Span(ticket.eventVenue());

        this.qrCodeImage = new Image();
        this.qrCodeImage.setAlt("Qr Code");
        this.qrCodeImage.setMaxWidth(250F, Unit.PIXELS);
        this.qrCodeImage.setWidthFull();
        this.qrCodeImage.addClassNames(BorderRadius.LARGE);

        this.ticketId = new Span();
        this.ticketId.addClassNames(FontSize.SMALL, TextColor.SECONDARY);

        Span ticketType = new Span(ticket.ticketTypeName());

        Markdown description = new Markdown(ticket.description());
        description.addClassNames(Margin.NONE, Padding.NONE);
        Details details = new Details("Description", description);
        details.setWidthFull();
        details.addThemeVariants(DetailsVariant.FILLED, DetailsVariant.REVERSE);

        VerticalLayout qrCodeLayout = new VerticalLayout(this.qrCodeImage, this.ticketId);
        qrCodeLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        qrCodeLayout.setMaxWidth(250F, Unit.PIXELS);
        qrCodeLayout.setPadding(false);
        qrCodeLayout.setSpacing(4F, Unit.PIXELS);
        qrCodeLayout.addClassNames(Margin.Left.AUTO, Margin.Right.AUTO);

        VerticalLayout detailsLayout = new VerticalLayout(eventName, eventTime, eventVenue, ticketType, details);
        detailsLayout.setPadding(false);

        Div space = new Div();
        space.setWidth(0F, Unit.PIXELS);
        space.setHeight(16F, Unit.PIXELS);

        HorizontalLayout mainLayout = new HorizontalLayout(qrCodeLayout, space, detailsLayout);
        mainLayout.addClassNames(Display.BLOCK, Display.Breakpoint.Small.FLEX);

        this.add(mainLayout);

        Button closeButton = new Button("Close", _ -> this.close());

        this.getFooter().add(closeButton);
    }

    public void setQrCode(QrCodeResponse qrCode) {
        this.ticketId.setText("Ticket ID: " + qrCode.token());

        byte[] qrImage;
        try {
            qrImage = QrCodeUtil.generateQrCodeImage(qrCode.encodeToBase64());
        } catch (WriterException | IOException e) {
            throw new QrCodeGenerationException("Qr code generation failed");
        }

        DownloadHandler handler = DownloadHandler
            .fromInputStream(
                _ -> new DownloadResponse(
                    new ByteArrayInputStream(qrImage),
                    "qr-code-image.png",
                    "image/png",
                    qrImage.length))
            .inline();

        this.qrCodeImage.setSrc(handler);
    }

}
