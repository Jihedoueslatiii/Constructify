package com.example.reports.Controller;

import lombok.Getter;
import lombok.Setter;

// ScheduleRequest.java
@Getter
@Setter
public class ScheduleRequest {
    private String command;
    private String timeZone; // Optional
}
