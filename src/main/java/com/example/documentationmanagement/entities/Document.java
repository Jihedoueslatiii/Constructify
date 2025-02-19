package com.example.documentationmanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    private String title; // Document name

    @Enumerated(EnumType.STRING)
    private DocumentType type; // Enum for "PDF", "DOCX", "TEXT", etc.

    @Enumerated(EnumType.STRING)
    private DocumentStatus status; // Enum for "DRAFT", "APPROVED", "PENDING_APPROVAL"

    private String storagePath; // For uploaded files (if applicable)

    @Column(columnDefinition = "TEXT")
    private String content; // Editable content (for text-based documents)

    private boolean isEditable; // True for WYSIWYG editor, false for uploaded docs

    private Long folderId; // Folder grouping (stored as ID)

    private Long projectId; // To associate the document with a project

    private Long authorId; // User who created the document (from User Microservice)

    private String permissions; // Store as JSON/String: "READ:1,EDIT:2,DOWNLOAD:3"

    private int versionNumber = 1; // Version tracking
    private String previousVersions; // JSON or CSV of old content/paths

    private boolean isDeleted = false; // Soft delete flag

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
