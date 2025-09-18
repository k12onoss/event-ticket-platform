package app.k12onos.tickets.ticket_management.domain.responses;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.k12onos.tickets.ticket.domain.entities.QrCode;

public record QrCodeResponse(String token, String signature) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String encodeToBase64() throws JsonProcessingException {
        byte[] val = MAPPER.writeValueAsBytes(this);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(val);
    }

    public static QrCode decodeFromBase64(String value) throws IOException {
        byte[] val = Base64.getUrlDecoder().decode(value);
        return MAPPER.readValue(val, QrCode.class);
    }

}
