package com.example.communicationmanagement.Controller;

import com.example.communicationmanagement.Service.MessageService;
import com.example.communicationmanagement.entities.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send") // client sends here
    public void send(Message message) {
        // save message
        Message saved = messageService.sendMessage(
                message.getSenderId(),
                message.getConversation().getConversationId(),
                message.getContent(),
                message.getMediaUrl()
        );

        // broadcast to subscribers of this conversation
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + message.getConversation().getConversationId(),
                saved
        );
    }
}