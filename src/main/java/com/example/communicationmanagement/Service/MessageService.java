package com.example.communicationmanagement.Service;


import com.example.communicationmanagement.Repository.ConversationRepository;
import com.example.communicationmanagement.Repository.MessageRepository;
import com.example.communicationmanagement.entities.Conversation;
import com.example.communicationmanagement.entities.ConversationType;
import com.example.communicationmanagement.entities.Message;
import com.example.communicationmanagement.entities.MessageStatus;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService  {


    private ConversationRepository conversationRepository;

    private MessageRepository messageRepository;

    // Create a conversation (Only Project Manager can create group chats)
    public Conversation createConversation(Long creatorId, List<Long> participants, String name, boolean isGroup, boolean isPM) {
        System.out.println("Creating conversation: creatorId=" + creatorId + ", participants=" + participants + ", name=" + name + ", isGroup=" + isGroup + ", isPM=" + isPM);
        try {
            if (isGroup && !isPM) {
                throw new RuntimeException("Only project managers can create group chats.");
            }
            if(!participants.contains(creatorId)){
                throw new RuntimeException("The creator must be included in the participant list.");
            }

            Conversation conversation = new Conversation();
            conversation.setCreatedBy(creatorId);
            conversation.setParticipants(participants);
            conversation.setCreatedAt(LocalDateTime.now());
            conversation.setType(isGroup ? ConversationType.GROUP : ConversationType.PRIVATE);
            conversation.setName(isGroup ? name : null);

            Conversation savedConversation = conversationRepository.save(conversation);
            System.out.println("Conversation saved successfully: " + savedConversation);
            return savedConversation;
        } catch (Exception e) {
            System.err.println("Error creating conversation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating conversation", e);
        }
    }

    // Send a message (Members can only message participants in the same chat)
    public Message sendMessage(Long senderId, Long conversationId, String content, String mediaUrl, boolean isPM) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        if (conversationOpt.isEmpty()) {
            throw new RuntimeException("Conversation not found.");
        }

        Conversation conversation = conversationOpt.get();
        if (!conversation.getParticipants().contains(senderId)) {
            throw new RuntimeException("You are not a participant in this conversation.");
        }

        Message message = new Message();

        message.setSenderId(senderId);
        message.setConversation(conversation);
        message.setContent(content);
        message.setMediaUrl(mediaUrl);
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);

        return messageRepository.save(message);
    }

    // Fetch all messages from a conversation (Ensuring access control)
    public List<Message> getMessages(Long userId, Long conversationId) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        if (conversationOpt.isEmpty()) {
            throw new RuntimeException("Conversation not found.");
        }

        Conversation conversation = conversationOpt.get();
        if (!conversation.getParticipants().contains(userId)) {
            throw new RuntimeException("You are not allowed to view this conversation.");
        }

        return conversation.getMessages();
    }



    public Conversation getConversationByUsers(List<Long> userIds) {
        // First, check if we are dealing with a private conversation or a group chat
        if (userIds.size() == 2) { // Private conversation (exactly two users)
            return conversationRepository.findByParticipantsContainingAndType(userIds, ConversationType.PRIVATE);
        } else if (userIds.size() > 2) { // Group chat (more than two users)
            return conversationRepository.findByParticipantsContainingAndType(userIds, ConversationType.GROUP);
        } else {
            throw new RuntimeException("A conversation must have at least 2 participants.");
        }
    }

    public Conversation getConversationById(Long conversationId) {
        // Find the conversation by its ID
        return conversationRepository.findById(conversationId)
                .orElse(null);  // Return null if not found, or you can throw an exception if needed
    }

    public List<Conversation> getAllConversationsForUser(Long userId) {
        // Retrieve all conversations where the user is a participant
        return conversationRepository.findByUserIdInParticipants(userId);
    }


    // Method to retrieve users (participants) of a conversation by its ID
    public List<Long> getConversationParticipants(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() ->
                new RuntimeException("Conversation not found with id: " + conversationId));
        return conversation.getParticipants(); // Return the list of user IDs (participants)
    }


    public void deleteMessage(Long messageId, Long userId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new RuntimeException("Message not found.");
        }

        Message message = messageOpt.get();

        // Check if the user is the sender
        if (!message.getSenderId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this message.");
        }

        // Perform soft delete
        message.setIsDeleted(true);
        message.setDeletedAt(LocalDateTime.now());
        message.setDeletedBy(userId);

        messageRepository.save(message);
    }


    public Message editMessage(Long messageId, Long userId, String newContent) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new RuntimeException("Message not found.");
        }

        Message message = messageOpt.get();

        // Ensure only the sender can edit the message
        if (!message.getSenderId().equals(userId)) {
            throw new RuntimeException("You are not authorized to edit this message.");
        }

        // Ensure the message is not deleted
        if (Boolean.TRUE.equals(message.getIsDeleted())) {
            throw new RuntimeException("Cannot edit a deleted message.");
        }

        // Save the previous content to the edit history
        if (message.getEditHistory() == null) {
            message.setEditHistory(new ArrayList<>());
        }
        message.getEditHistory().add(message.getContent());

        // Update the message content
        message.setContent(newContent);
        message.setLastEditedAt(LocalDateTime.now());
        message.setLastEditedBy(userId);

        return messageRepository.save(message);
    }


    public Message pinMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsPinned(true);
        return messageRepository.save(message);
    }

    public Message unpinMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsPinned(false);
        return messageRepository.save(message);
    }
}


