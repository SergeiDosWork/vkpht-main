package me.goodt.vkpht.module.notification.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Email-адрес получателя уведомления
 */
@Getter
@Setter
@NoArgsConstructor
public class NotificationRecipientEmailDto {
    private Long id;
    private String email;
    private Long notificationRecipientId;
    private NotificationRecipientDto notificationRecipient;
    private Boolean isSystem;
}
