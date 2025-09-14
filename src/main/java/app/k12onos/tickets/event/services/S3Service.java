package app.k12onos.tickets.event.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.k12onos.tickets.event.exceptions.DeleteFileFailedException;
import app.k12onos.tickets.event.exceptions.GeneratePresignedUrlFailedException;

@Service
public class S3Service {

    private final String bucket;
    private final String publicBaseUrl;
    private final String serviceRoleKey;
    private final HttpClient httpClient;

    public S3Service(
        @Value("${s3.bucket.event-assets}") String bucket,
        @Value("${s3.public-base-url}") String publicBaseUrl,
        @Value("${s3.service-role-key}") String serviceRoleKey) {

        this.bucket = bucket;
        this.publicBaseUrl = publicBaseUrl;
        this.serviceRoleKey = serviceRoleKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    public String generatePresignedPutUrl(String key, String contentType, Duration duration) {
        try {
            URI uri = URI.create(this.publicBaseUrl + "/upload/sign/" + this.bucket + "/" + key);
            String body = "{\"expiresIn\":" + duration.getSeconds() + "}";

            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + this.serviceRoleKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            HttpStatusCode statusCode = HttpStatusCode.valueOf(response.statusCode());
            if (statusCode.is4xxClientError() || statusCode.is5xxServerError()) {
                throw new GeneratePresignedUrlFailedException();
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            return this.publicBaseUrl.replace("object", json.get("url").asText());
        } catch (InterruptedException | IOException e) {
            throw new GeneratePresignedUrlFailedException(e.getMessage());
        }
    }

    public void deleteFile(String key) {
        try {
            URI uri = URI.create(this.publicBaseUrl + "/" + key);

            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + this.serviceRoleKey)
                .DELETE()
                .build();

            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new DeleteFileFailedException(response.body());
            }
        } catch (Exception e) {
            throw new DeleteFileFailedException(e.getMessage());
        }
    }

    public String generateReadUrl(String key) {
        if (key == null) {
            return null;
        }

        return this.publicBaseUrl + "/public/" + key;
    }

}
