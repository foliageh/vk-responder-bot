package com.twillice.vkresponderbot.events.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewMessage {
    Message message;
    // ClientInfo clientInfo;
}
