package app.k12onos.tickets.ticket.services;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.ticket.exceptions.QrCodeGenerationException;

@Service
public class TokenService {

    private static final int TOKEN_LENGTH = 16;
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final String hmacSecret;
    private final SecureRandom random;

    public TokenService(@Value("${tickets.hmac-secret}") String hmacSecret) {
        this.hmacSecret = hmacSecret;
        this.random = new SecureRandom();
    }

    public String generateBase62Token() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = this.random.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));
        }
        return sb.toString();
    }

    public String signToken(String token) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(this.hmacSecret.getBytes(), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(token.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new QrCodeGenerationException();
        }
    }

}
