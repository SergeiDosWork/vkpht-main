package me.goodt.vkpht.module.notification.api.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventFormat {

    @JsonProperty("full-time")
    FULL_TIME,

    @JsonProperty("distance")
    DISTANCE
}
