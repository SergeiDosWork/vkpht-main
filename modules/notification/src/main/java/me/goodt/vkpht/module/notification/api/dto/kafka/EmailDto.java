package me.goodt.vkpht.module.notification.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.USE_SYSTEM_TEMPLATES_TAG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    @JsonSetter(USE_SYSTEM_TEMPLATES_TAG)
    private Boolean useSystemTemplates;
    private String body;
    private String subject;
}
