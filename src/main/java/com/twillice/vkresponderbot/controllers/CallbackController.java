package com.twillice.vkresponderbot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twillice.vkresponderbot.configuration.VkApiProperties;
import com.twillice.vkresponderbot.events.CallbackEvent;
import com.twillice.vkresponderbot.events.objects.NewMessage;
import com.twillice.vkresponderbot.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/callback")
@RequiredArgsConstructor
public class CallbackController {
    private final MessageService messageService;
    private final VkApiProperties apiProperties;
    private final ObjectMapper objectMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CallbackController.class);

    @PostMapping
    public String handleCallback(@RequestBody CallbackEvent callbackEvent) {
        if (callbackEvent.getType() == null) {
            LOG.warn("Received unknown event");
            return "ok";
        }

        LOG.info("Received {} event", callbackEvent.getType());
        switch (callbackEvent.getType()) {
            case confirmation -> {
                LOG.info("Send confirmation");
                return apiProperties.getServerConfirmCode();
            }
            case message_new -> {
                var newMessage = objectMapper.convertValue(callbackEvent.getObject(), NewMessage.class);
                messageService.replyNewMessage(newMessage);
                LOG.info("Replied to message");
            }
        }
        return "ok";
    }
}
