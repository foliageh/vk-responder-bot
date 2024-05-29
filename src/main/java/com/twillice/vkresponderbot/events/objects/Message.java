package com.twillice.vkresponderbot.events.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    @JsonProperty(value = "from_id")
    Integer fromId;
    String text;
}
