package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationRecipientNameDto {

	private String name;
	private NotificationRecipientType type;
}
