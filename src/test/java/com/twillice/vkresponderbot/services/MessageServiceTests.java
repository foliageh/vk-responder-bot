package com.twillice.vkresponderbot.services;

import com.twillice.vkresponderbot.restclients.VkApiClient;
import com.twillice.vkresponderbot.configuration.VkApiProperties;
import com.twillice.vkresponderbot.events.objects.Message;
import com.twillice.vkresponderbot.events.objects.NewMessage;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(VkApiClient.class)
@TestPropertySource(properties = {
        "vk.api.version=1.0",
        "vk.api.access-token=accesstoken",
        "vk.api.server-confirm-code=somecode",
})
public class MessageServiceTests {
    @Autowired
    private VkApiClient vkApiClient;
    @Autowired
    private MockRestServiceServer vkApiServer;

    @MockBean
    private VkApiProperties apiProperties;

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(vkApiClient);
    }

    @Test
    void replyNewMessage_whenValidMessage_sendsReply() {
        int messageUserId = 123;
        String messageText = "hello";
        var newMessage = new NewMessage(new Message(messageUserId, messageText));

        var expectedReplyMessage = "Вы сказали: " + messageText;
        vkApiServer.expect(request -> assertEquals("/method/messages.send", request.getURI().getPath()))
                .andExpect(method(HttpMethod.POST))
                .andExpect(queryParam("access_token", apiProperties.getAccessToken()))
                .andExpect(queryParam("v", apiProperties.getVersion()))
                .andExpect(queryParam("user_id", String.valueOf(messageUserId)))
                .andExpect(queryParam("random_id", CoreMatchers.anything()))
                .andExpect(queryParam("message", UriUtils.encodeQueryParam(expectedReplyMessage, StandardCharsets.UTF_8)))
                .andRespond(withSuccess("{\"_\": 0}", MediaType.APPLICATION_JSON));

        messageService.replyNewMessage(newMessage);
        vkApiServer.verify();
    }
}
