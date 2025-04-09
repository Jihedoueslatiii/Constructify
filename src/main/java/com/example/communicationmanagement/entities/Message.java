package com.example.communicationmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
// User ID of the sender
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Long messageId;

    Long senderId;  // User ID of the sender
    String content;  // The message text/content

    String mediaUrl;  // Link to media file (if any)

    @Enumerated(EnumType.STRING)
    MessageType type;  // TEXT, IMAGE, FILE

    @Enumerated(EnumType.STRING)
    MessageStatus status;  // SENT, DELIVERED, READ

    LocalDateTime timestamp;  // Time when the message was sent

    Boolean isDeleted = false;  // Flag for deleted messages

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
            @JsonIgnore
    Conversation conversation;  // The conversation this message belongs to

    @ElementCollection
    List<Long> taggedUsers;  // List of tagged user IDs (for notifications)

    Boolean isPinned = false;  // Whether the message is pinned

    @ElementCollection
    List<String> editHistory;  // Keeps track of message edits (e.g., content before and after editing)

    // Logging the editing/deletion actions
    LocalDateTime lastEditedAt;  // Time of the last edit
    Long lastEditedBy;  // User ID who last edited the message

    LocalDateTime deletedAt;  // Time when message was deleted (soft delete)
    Long deletedBy;  // User ID who deleted the message



}
