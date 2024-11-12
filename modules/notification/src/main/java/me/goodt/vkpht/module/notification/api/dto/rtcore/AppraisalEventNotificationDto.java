package me.goodt.vkpht.module.notification.api.dto.rtcore;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppraisalEventNotificationDto {
	private Long id;
	private String name;
	private Long divisionId;
	private Integer month;
	private Integer year;
	private Date meetingDate;
}
