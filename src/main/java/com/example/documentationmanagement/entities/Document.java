package com.example.documentationmanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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
    private DocumentType type; // Enum for "PDF", "DOCX", etc.

    @Enumerated(EnumType.STRING)
    private DocumentStatus status; // Enum for "DRAFT", "APPROVED", etc.

    private String storagePath; // Path for uploaded files

    @Column(columnDefinition = "TEXT")
    private String content; // Editable content (for text-based documents)

    private boolean isEditable; // Whether the document can be edited

    private Long folderId;

    private Long projectId;

    private Long authorId;

    private String permissions; // Store roles and permissions (e.g., "READ:1,EDIT:2")

    private int versionNumber = 1; // Version tracking

    private String previousVersions; // Store previous versions (as CSV/JSON)

    private boolean isDeleted = false; // Soft delete flag

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String tags; // New field to store tags as a comma-separated string
    private String description; // New field for a brief description of the document
}
