package me.goodt.vkpht.module.notification.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ContentDto {
    private static final String TEMPLATES_CONTEXT_TAG = "templates_context";
    @JsonSetter(TEMPLATES_CONTEXT_TAG)
    private JSONObject templatesContext;
    private EmailDto email;
}
