package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateContentDto {
	private Long id;
	private NotificationTemplateDto notificationTemplate;
	private List<NotificationRecipientDto> notificationRecipient;
	private List<NotificationRecipientDto> notificationRecipientCopy;
	private NotificationReceiverSystemDto receiverSystem;
	private Integer isEnabled;
	private Boolean priority;
	private String bodyJson;
	private NotificationTemplateContentDto substitute;
}
