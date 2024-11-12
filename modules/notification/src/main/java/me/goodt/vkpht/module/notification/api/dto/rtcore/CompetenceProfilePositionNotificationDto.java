package me.goodt.vkpht.module.notification.api.dto.rtcore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetenceProfilePositionNotificationDto {
	private Long id;
	private Long positionId;
	private Long competenceProfileId;
	private String competenceProfileName;
}
