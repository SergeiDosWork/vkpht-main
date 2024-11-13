package me.goodt.vkpht.module.notification.api.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationLogResponseKafkaDto {

    private List<Long> notificationLogIds;  // список id в notification_log
    private boolean isSendSuccess;          // признак успешной отправки
    private String errorMessage;            // сообщение об ошибке во время отправки

}
