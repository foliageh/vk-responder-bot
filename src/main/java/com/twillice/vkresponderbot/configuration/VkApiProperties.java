package com.twillice.vkresponderbot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("vk.api")
@Getter @Setter
public class VkApiProperties {
    private String version;
    private String accessToken;
    private String serverConfirmCode;
}
