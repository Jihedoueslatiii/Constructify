package com.example.communicationmanagement.entities;

public enum MessageType {
    TEXT,    // Simple text message
    IMAGE,   // Image file (could store URL or file reference)
    FILE,    // Any file attachment (PDF, Word, etc.)
    VIDEO,   // Video file
    AUDIO    // Audio message or file
}
