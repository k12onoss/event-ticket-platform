package app.k12onos.tickets.event_management.ui.components;

import java.util.Base64;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.LineHeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;

import app.k12onos.tickets.event_management.domain.dto.ImageValue;

public class ImageUploadField extends CustomField<ImageValue> {

    private final Card layout;

    public ImageUploadField(String type) {
        Icon placeholderImage = VaadinIcon.PICTURE.create();
        placeholderImage.setSize("150px");
        placeholderImage.addClassNames(Background.CONTRAST_5, Padding.XLARGE);

        this.layout = new Card();
        this.layout
            .addThemeVariants(CardVariant.LUMO_STRETCH_MEDIA, CardVariant.LUMO_HORIZONTAL, CardVariant.LUMO_OUTLINED);
        this.layout.getStyle().set("--vaadin-card-media-aspect-ratio", "1/1");
        this.layout.addClassNames(LineHeight.SMALL, Margin.Top.MEDIUM);

        this.layout.setMedia(placeholderImage);

        Span hint = new Span("Maximum file size: 2MB\nAspect ratio: ");
        if (type.equals("poster")) {
            hint.add("3x4");
        } else if (type.equals("banner")) {
            hint.add("16x9");
        }
        hint.addClassNames(Whitespace.PRE_LINE, TextColor.SECONDARY, FontSize.SMALL);

        this.layout.add(hint);

        UploadHandler handler = UploadHandler.inMemory((metadata, data) -> {
            ImageValue value = new ImageValue(metadata.contentType(), data, true);
            this.updateImagePreview(value);
            this.setModelValue(value, true);
        });

        Upload upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(2 * 1024 * 1024); // 2 MB
        upload.setAcceptedFileTypes("image/*");
        upload.setAutoUpload(true);
        upload.setWidthFull();

        upload.setUploadButton(new Button("Upload " + type + "..."));
        upload.setDropLabel(new Span("Drop " + type + " here"));

        upload.addFileRejectedListener(rejectedEvent -> {
            Notification notification = new Notification(rejectedEvent.getErrorMessage(), 3000, Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        });

        upload.addFileRemovedListener(_ -> {
            this.layout.setMedia(placeholderImage);
            ImageValue value = new ImageValue(null, null, true);
            this.setModelValue(value, true);
        });

        this.layout.setHeader(upload);

        this.add(this.layout);
    }

    private void updateImagePreview(ImageValue value) {
        String base64 = Base64.getEncoder().encodeToString(value.imageData());
        Image image = new Image();
        image.setWidth(150F, Unit.PIXELS);
        image.setHeight(150F, Unit.PIXELS);
        image.setSrc("data:" + value.contentType() + ";base64," + base64);
        this.layout.setMedia(image);
    }

    @Override
    protected ImageValue generateModelValue() {
        return this.getValue();
    }

    @Override
    protected void setPresentationValue(ImageValue value) {
        if (value != null && value.imageData() != null && value.imageData().length > 0) {
            this.updateImagePreview(value);
        }
    }

}
