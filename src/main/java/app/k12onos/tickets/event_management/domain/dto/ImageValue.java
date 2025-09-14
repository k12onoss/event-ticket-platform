package app.k12onos.tickets.event_management.domain.dto;

public record ImageValue(String contentType, byte[] imageData, boolean changed) {}
