package com.example.reports.Service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemporalExpressionParser {

    public String convertToCron(String frequency, String time) {
        // Basic implementation - you should expand this with more cases
        if (frequency == null || time == null) {
            throw new IllegalArgumentException("Frequency and time cannot be null");
        }

        // Parse time (simplified example)
        int hour = parseHour(time);
        int minute = parseMinute(time);

        // Parse frequency (simplified example)
        String cronDay = parseDay(frequency);

        return String.format("%d %d ? * %s", minute, hour, cronDay);
    }

    public LocalDateTime getFirstRunDate(String command) {
        // Basic implementation - returns next occurrence based on command
        LocalDateTime now = LocalDateTime.now();

        if (command.toLowerCase().contains("monday")) {
            return now.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                    .withHour(9).withMinute(0).withSecond(0);
        } else if (command.toLowerCase().contains("friday")) {
            return now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
                    .withHour(17).withMinute(0).withSecond(0);
        }

        // Default to tomorrow at 9 AM
        return now.plusDays(1).withHour(9).withMinute(0).withSecond(0);
    }

    // Helper methods
    private int parseHour(String time) {
        // Simple parsing - in real implementation use proper time parsing
        if (time.contains("pm")) {
            return 15; // 3 PM as example
        }
        return 9; // default to 9 AM
    }

    private int parseMinute(String time) {
        return 0; // default to top of the hour
    }

    private String parseDay(String frequency) {
        if (frequency.toLowerCase().contains("monday")) {
            return "MON";
        } else if (frequency.toLowerCase().contains("friday")) {
            return "FRI";
        }
        return "*"; // default to every day
    }
}