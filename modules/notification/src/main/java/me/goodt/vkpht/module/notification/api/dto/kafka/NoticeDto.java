package me.goodt.vkpht.module.notification.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
    private static final String EVENT_TYPE_TAG = "event_type";
    @JsonSetter(EVENT_TYPE_TAG)
    private String eventType;
    private PayloadDto payload;
    private Long version;
}
