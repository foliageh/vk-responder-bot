package com.twillice.vkresponderbot.services;

import com.twillice.vkresponderbot.events.objects.Message;
import com.twillice.vkresponderbot.events.objects.NewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RestClient apiClient;

    private final Random random = new Random();

    public void replyNewMessage(NewMessage newMessage) {
        Message userMessage = newMessage.getMessage();
        sendMessage(userMessage.getFromId(), "Вы сказали: " + userMessage.getText());
    }

    private Map<String, Object> sendMessage(long userId, String text) {
        return apiClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages.send")
                        .queryParam("user_id", userId)
                        .queryParam("random_id", random.nextInt())
                        .queryParam("message", text)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
