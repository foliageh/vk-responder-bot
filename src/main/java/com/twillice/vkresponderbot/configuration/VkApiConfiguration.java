package com.twillice.vkresponderbot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.ForwardedHeaderUtils;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class VkApiConfiguration {
    private final VkApiProperties apiProperties;

    @Bean
    public RestClient apiClient() {
        return RestClient.builder()
                .baseUrl("https://api.vk.com/method")
                .requestFactory(new SimpleClientHttpRequestFactory())  // because default JdkClientHttpRequestFactory suddenly doesn't set Content-Length to 0 automatically for empty body requests
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
                })
                .build();
    }
}
