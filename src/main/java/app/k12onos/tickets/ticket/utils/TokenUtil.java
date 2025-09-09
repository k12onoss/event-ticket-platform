package app.k12onos.tickets.ticket.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import app.k12onos.tickets.ticket.exceptions.QrCodeGenerationException;

public class TokenUtil {

    private static final int TOKEN_LENGTH = 16;
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Value("${hmac.secret}")
    private static final String HMAC_SECRET = System.getenv("HMAC_SECRET");

    private static final SecureRandom random = new SecureRandom();

    public static String generateBase62Token() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));
        }
        return sb.toString();
    }

    public static String signToken(String token) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(HMAC_SECRET.getBytes(), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(token.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new QrCodeGenerationException();
        }
    }

}
