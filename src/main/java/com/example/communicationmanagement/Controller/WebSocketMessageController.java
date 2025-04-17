package com.example.communicationmanagement.Controller;

import com.example.communicationmanagement.Service.MessageService;
import com.example.communicationmanagement.entities.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    @SendTo("/topic/conversations/{conversationId}")
    public void send(Message message) {
        // Log the conversationId to make sure it matches the frontend's subscription
        System.out.println("Sending message to conversationId: " + message.getConversation().getConversationId());

        // First save the message to database
        Message savedMessage = messageService.sendMessage(
                message.getSenderId(),
                message.getConversation().getConversationId(),
                message.getContent(),
                message.getMediaUrl()
        );

    }
}