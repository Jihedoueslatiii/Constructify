package com.example.reports.Service;

import com.example.reports.Entity.Report;
import com.example.reports.Entity.ScheduleConfig;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NaturalLanguageParser {
    // Email pattern for extraction
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    // Time pattern (matches "3pm", "10:30am", etc.)
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2})(?::(\\d{2}))?\\s*([ap]m)");

    public ScheduleConfig parseScheduleCommand(String command, Report report) {
        ScheduleConfig config = new ScheduleConfig();
        config.setReport(report);

        // Parse and set frequency
        config.setFrequency(extractFrequency(command));

        // Parse and set time
        config.setScheduledTime(parseTime(command));

        // Parse and set recipients if any
        List<String> emails = extractEmails(command);
        if (!emails.isEmpty()) {
            config.setRecipients(String.join(",", emails));
        }

        // Calculate next run time
        config.setNextRunTime(calculateNextRun(config.getFrequency(), config.getScheduledTime()));
        config.setFirstRun(LocalDateTime.now());
        config.setActive(true);

        return config;
    }

    public String extractFrequency(String command) {
        if (command == null || command.isEmpty()) {
            return "daily"; // Default frequency
        }

        command = command.toLowerCase();

        if (command.contains("every day") || command.contains("daily")) {
            return "DAILY";
        } else if (command.contains("every week") || command.contains("weekly")) {
            return "WEEKLY";
        } else if (command.contains("every month") || command.contains("monthly")) {
            return "MONTHLY";
        } else if (command.contains("every monday") || command.contains("mondays")) {
            return "MON";
        } else if (command.contains("every friday") || command.contains("fridays")) {
            return "FRI";
        } else if (command.matches(".*every\\s+\\d+\\s+days.*")) {
            return extractNumberAndUnit(command, "days");
        }

        return "DAILY"; // Default fallback
    }

    private LocalTime parseTime(String command) {
        Matcher matcher = TIME_PATTERN.matcher(command.toLowerCase());
        if (matcher.find()) {
            int hour = Integer.parseInt(matcher.group(1));
            int minute = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
            String period = matcher.group(3);

            if ("pm".equals(period) && hour < 12) hour += 12;
            if ("am".equals(period) && hour == 12) hour = 0;

            return LocalTime.of(hour, minute);
        }
        return LocalTime.of(9, 0); // default to 9 AM
    }

    private LocalDateTime calculateNextRun(String frequency, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = LocalDateTime.of(now.toLocalDate(), time);

        if (now.isAfter(nextRun)) {
            switch (frequency) {
                case "WEEKLY": return nextRun.plusWeeks(1);
                case "MONTHLY": return nextRun.plusMonths(1);
                default: return nextRun.plusDays(1); // DAILY
            }
        }
        return nextRun;
    }

    public String extractTime(String command) {
        if (command == null || command.isEmpty()) {
            return "09:00"; // Default time
        }

        Matcher matcher = TIME_PATTERN.matcher(command.toLowerCase());
        if (matcher.find()) {
            int hour = Integer.parseInt(matcher.group(1));
            int minute = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
            String period = matcher.group(3);

            // Convert to 24-hour format
            if ("pm".equals(period) && hour < 12) {
                hour += 12;
            } else if ("am".equals(period) && hour == 12) {
                hour = 0;
            }

            return String.format("%02d:%02d", hour, minute);
        }

        return "09:00"; // Default fallback
    }

    public List<String> extractEmails(String command) {
        List<String> emails = new ArrayList<>();
        if (command == null || command.isEmpty()) {
            return emails;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(command);
        while (matcher.find()) {
            emails.add(matcher.group());
        }

        return emails;
    }

    private String extractNumberAndUnit(String command, String unit) {
        Pattern pattern = Pattern.compile("every\\s+(\\d+)\\s+" + unit);
        Matcher matcher = pattern.matcher(command.toLowerCase());
        if (matcher.find()) {
            return matcher.group(1) + " " + unit;
        }
        return "1 " + unit; // Default
    }
}