package com.twillice.vkresponderbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class VkResponderBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(VkResponderBotApplication.class, args);
    }
}
