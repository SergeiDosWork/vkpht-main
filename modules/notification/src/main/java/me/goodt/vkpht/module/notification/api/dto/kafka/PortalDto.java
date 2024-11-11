package me.goodt.vkpht.module.notification.api.dto.kafka;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.goodt.drive.notify.application.dto.DtoTagConstants.USE_SYSTEM_TEMPLATES_TAG;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortalDto {
	@JsonSetter(USE_SYSTEM_TEMPLATES_TAG)
	private Boolean useSystemTemplates;
	private String text;
}
