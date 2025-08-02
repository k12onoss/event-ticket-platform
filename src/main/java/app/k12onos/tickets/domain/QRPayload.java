package app.k12onos.tickets.domain;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.k12onos.tickets.exceptions.QRCodeGenerationException;
import app.k12onos.tickets.exceptions.QRCodeNotFoundException;

public record QRPayload(UUID qrCodeId, UUID ticketId) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String encodeToBase64() {
        try {
            byte[] val = MAPPER.writeValueAsBytes(this);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(val);
        } catch (JsonProcessingException e) {
            throw new QRCodeGenerationException();
        }
    }

    public static QRPayload decodeFromBase64(String value) {
        byte[] val = Base64.getUrlDecoder().decode(value);

        try {
            return MAPPER.readValue(val, QRPayload.class);
        } catch (IOException e) {
            throw new QRCodeNotFoundException();
        }
    }

}
