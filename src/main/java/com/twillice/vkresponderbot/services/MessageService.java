package com.twillice.vkresponderbot.services;

import com.twillice.vkresponderbot.restclients.VkApiClient;
import com.twillice.vkresponderbot.events.objects.Message;
import com.twillice.vkresponderbot.events.objects.NewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final VkApiClient vkApiClient;

    private final AtomicInteger messageCounter = new AtomicInteger();

    public void replyNewMessage(NewMessage newMessage) {
        Message userMessage = newMessage.getMessage();
        sendMessage(userMessage.getFromId(), "Вы сказали: " + userMessage.getText());
    }

    private Map<String, Object> sendMessage(int userId, String text) {
        return vkApiClient.sendMessage(userId, text, generateUniqueRandomId());
    }

    private int generateUniqueRandomId() {
        return Integer.MIN_VALUE + (int) (System.currentTimeMillis() / 1000) + messageCounter.getAndIncrement();
    }
}
