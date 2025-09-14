package app.k12onos.tickets.event_management.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.k12onos.tickets.event_management.domain.dto.ImageValue;
import app.k12onos.tickets.event_management.exceptions.GetImageFailedException;
import app.k12onos.tickets.event_management.exceptions.UploadImageFailedException;

@Service
public class ImageService {

    private final HttpClient httpClient;

    public ImageService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public ImageValue getImage(String url) {
        try {
            HttpResponse<byte[]> response = this.httpClient
                .send(
                    HttpRequest.newBuilder().uri(URI.create(url)).GET().build(),
                    HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                String contentType = response.headers().firstValue("Content-Type").orElse("application/octet-stream");
                return new ImageValue(contentType, response.body(), false);
            } else {
                throw new GetImageFailedException();
            }
        } catch (IOException | InterruptedException e) {
            throw new GetImageFailedException(e.getMessage());
        }
    }

    public String uploadImage(String url, ImageValue value) {
        try {
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", value.contentType())
                .PUT(HttpRequest.BodyPublishers.ofByteArray(value.imageData()))
                .build();

            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            HttpStatusCode statusCode = HttpStatusCode.valueOf(response.statusCode());
            if (statusCode.is4xxClientError() || statusCode.is5xxServerError()) {
                throw new UploadImageFailedException();
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            return json.get("Key").asText();
        } catch (InterruptedException | IOException e) {
            throw new UploadImageFailedException(e.getMessage());
        }
    }

}
