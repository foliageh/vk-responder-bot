package com.twillice.vkresponderbot.restclients;

import com.twillice.vkresponderbot.configuration.VkApiProperties;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.ForwardedHeaderUtils;

import java.net.URI;
import java.util.Map;

@Component
public class VkApiClient {
    @Getter
    private final RestClient client;

    public VkApiClient(RestClient.Builder builder, VkApiProperties apiProperties) {
        client = builder.baseUrl("https://api.vk.com/method")
                .requestInterceptor((request, body, execution) -> {
                    HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                        @Override
                        public URI getURI() {
                            return ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders())
                                    .queryParam("access_token", apiProperties.getAccessToken())
                                    .queryParam("v", apiProperties.getVersion())
                                    .build(true)
                                    .toUri();
                        }
                    };
                    return execution.execute(modifiedRequest, body);
                }).build();
    }

    public Map<String, Object> sendMessage(int userId, String text, int randomId) {
        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages.send")
                        .queryParam("user_id", userId)
                        .queryParam("random_id", randomId)
                        .queryParam("message", text)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
