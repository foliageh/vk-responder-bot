package com.twillice.vkresponderbot.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Map;

@Value
public class CallbackEvent {
    CallbackEventType type;
    Map<String, Object> object;
    @JsonProperty(value = "group_id")
    Long groupId;
}