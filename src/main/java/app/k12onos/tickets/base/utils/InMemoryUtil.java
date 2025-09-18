package app.k12onos.tickets.base.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import app.k12onos.tickets.event.domain.responses.TicketTypeSummaryResponse;

public class InMemoryUtil {

    public static final Map<UUID, TicketTypeSummaryResponse> ticketTypesMap = new HashMap<>();

}
