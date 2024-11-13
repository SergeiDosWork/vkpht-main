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
public class NotificationTemplateContentEmployeeSubscribeDto {

    private Long employeeId;                                       // id сотрудника
    private String receiverSystemName;                             // наименование канала отправки (notification_receiver_system.name)
    private Boolean isEnabledAllNotifications;                     // все уведомления
    private List<NotificationStateDto> subscribes;                 // детализация подписок

    @Getter
    @Setter
    @NoArgsConstructor
    public static class NotificationStateDto {
        private Long id;                         // id шаблона уведомления (notification_template_content.id)
        private String description;              // наименование типа шаблона уведомления
        private Boolean isEnabled;               // включение/отключение подписки
    }
}
