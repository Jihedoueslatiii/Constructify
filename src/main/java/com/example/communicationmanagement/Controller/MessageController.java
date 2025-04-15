package com.example.communicationmanagement.Controller;


import com.example.communicationmanagement.Service.IMessageService;

import com.example.communicationmanagement.Service.MessageService;
import com.example.communicationmanagement.Service.SummaryService;
import com.example.communicationmanagement.entities.Conversation;
import com.example.communicationmanagement.entities.Message;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Messages")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
@RequestMapping("messages")
public class MessageController {
    MessageService messageService;
    SummaryService summaryService;


    // Create a new conversation
    @PostMapping("/conversation")
    public ResponseEntity<Conversation> createConversation(
            @RequestParam Long creatorId,
            @RequestParam List<Long> participants,
            @RequestParam(required = false) String name,
            @RequestParam boolean isGroup,
            @RequestParam boolean isPM) {
        Conversation conversation = messageService.createConversation(creatorId, participants, name, isGroup, isPM);
        return ResponseEntity.ok(conversation);
    }

    // Send a message in a conversation
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long conversationId,
            @RequestParam String content,
            @RequestParam(required = false) String mediaUrl) {
        Message message = messageService.sendMessage(senderId, conversationId, content, mediaUrl);
        return ResponseEntity.ok(message);
    }

    // Retrieve messages from a conversation
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessages(
            @RequestParam Long userId,
            @PathVariable Long conversationId) {

        List<Message> messages = messageService.getMessages(userId, conversationId);

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/summarize/conversation/{conversationId}")
    public ResponseEntity<String> Summarize(
            @RequestParam Long userId,
            @PathVariable Long conversationId) {
        String summary = summaryService.summarize(userId,conversationId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/api/conversation/{conversationId}")
    public ResponseEntity<Conversation> getConversation(@PathVariable Long conversationId) {
        Conversation conversation = messageService.getConversationById(conversationId);
        if (conversation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/conversations/user/{userId}")
    public ResponseEntity<List<Conversation>> getAllConversationsForUser(@PathVariable Long userId) {
        List<Conversation> conversations = messageService.getAllConversationsForUser(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}/participants")
    public List<Long> getConversationParticipants(@PathVariable Long conversationId) {
        return messageService.getConversationParticipants(conversationId);
    }


    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId // Assume userId is sent from the frontend
    ) {
        try {
            messageService.deleteMessage(messageId, userId);
            return ResponseEntity.ok().body("Message deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{messageId}/edit")
    public ResponseEntity<Message> editMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId,
            @RequestParam String newContent) {

        Message updatedMessage = messageService.editMessage(messageId, userId, newContent);
        return ResponseEntity.ok(updatedMessage);
    }


    @PutMapping("/{messageId}/pin")
    public ResponseEntity<Message> pinMessage(@PathVariable Long messageId) {
        Message pinnedMessage = messageService.pinMessage(messageId);
        return ResponseEntity.ok(pinnedMessage);
    }

    @PutMapping("/{messageId}/unpin")
    public ResponseEntity<Message> unpinMessage(@PathVariable Long messageId) {
        Message unpinnedMessage = messageService.unpinMessage(messageId);
        return ResponseEntity.ok(unpinnedMessage);
    }
}
