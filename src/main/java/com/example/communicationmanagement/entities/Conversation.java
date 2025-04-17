package com.example.communicationmanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long conversationId;

    @Enumerated(EnumType.STRING)
    ConversationType type; // GROUP or PRIVATE

    String name;  // Only for group chats

    Long createdBy;  // Who created the chat

    @ElementCollection
    List<Long> participants;  // User IDs of members in the chat

    LocalDateTime createdAt;

    // Linking messages to the conversation
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Message> messages;



}
