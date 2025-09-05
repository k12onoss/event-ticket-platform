package app.k12onos.tickets.base.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("dd MMM | ha");
    private static final DateTimeFormatter longFormatter = DateTimeFormatter.ofPattern("EE, dd MMM, YYYY | ha");

    public static final String formatDateTime(LocalDateTime start, LocalDateTime end, boolean isShort) {
        DateTimeFormatter formatter = isShort ? shortFormatter : longFormatter;

        if (start != null && end != null) {
            return start.format(formatter) + " - " + end.format(formatter);
        } else if (start != null) {
            return "Starts from " + start.format(formatter);
        } else if (end != null) {
            return "Ends on " + end.format(formatter);
        } else {
            return "Not specified";
        }
    }

}
