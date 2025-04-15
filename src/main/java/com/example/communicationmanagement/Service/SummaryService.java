package com.example.communicationmanagement.Service;


import com.example.communicationmanagement.entities.Message;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@Service
public class SummaryService {

    private final WebClient.Builder webClientBuilder;
    private final MessageService messageService;


    public SummaryService(WebClient.Builder webClientBuilder, MessageService messageService) {
        this.webClientBuilder = webClientBuilder;
        this.messageService = messageService;
    }

    public List<Message> getMessages(Long userId, Long conversationId){
        List<Message> messages = this.messageService.getMessages(userId, conversationId);
        //System.out.println(messages.get(0).getContent());
        return messages;
    }

    public String summarize(Long userId, Long conversationId) {
        List<Message> messages = getMessages(userId, conversationId);

        return webClientBuilder.build()
                .post()
                .uri("http://127.0.0.1:5000/summarize")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("messages", messages))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("summary"))
                .block();
    }

}