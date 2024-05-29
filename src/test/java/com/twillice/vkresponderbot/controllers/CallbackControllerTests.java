package com.twillice.vkresponderbot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twillice.vkresponderbot.configuration.VkApiProperties;
import com.twillice.vkresponderbot.events.CallbackEvent;
import com.twillice.vkresponderbot.events.CallbackEventType;
import com.twillice.vkresponderbot.events.objects.Message;
import com.twillice.vkresponderbot.events.objects.NewMessage;
import com.twillice.vkresponderbot.services.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CallbackControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VkApiProperties apiProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MessageService messageServiceMock;

    @Test
    void handleCallback_whenConfirmationEvent_returnsConfirmCode() throws Exception {
        String callbackBody = "{\"type\": \"confirmation\"}";
        mockMvc.perform(post("/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(callbackBody))
                .andExpect(status().isOk())
                .andExpect(content().string(apiProperties.getServerConfirmCode()));
    }

    @Test
    void handleCallback_whenNewMessageEvent_sendsReplyAndReturnsOk() throws Exception {
        var newMessage = new NewMessage(new Message(123, "hello"));
        var event = new CallbackEvent(CallbackEventType.message_new, Map.of("message", newMessage.getMessage()), 500);
        String callbackBody = objectMapper.writeValueAsString(event);

        doNothing().when(messageServiceMock).replyNewMessage(newMessage);
        mockMvc.perform(post("/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(callbackBody))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(messageServiceMock).replyNewMessage(newMessage);
    }

    @Test
    void handleCallback_whenUnknownEvent_returnsOk() throws Exception {
        String callbackBody = "{\"type\": \"unknown\", \"object\": {\"_\": 0}, \"group_id\": \"500\"}";
        mockMvc.perform(post("/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(callbackBody))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
