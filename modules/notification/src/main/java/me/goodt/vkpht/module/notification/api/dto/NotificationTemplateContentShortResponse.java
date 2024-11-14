package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO для отображения данных о шаблонах уведомлений на странице "Уведомления о событиях".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateContentShortResponse {

    /**
     * Уникальный идентификатор шаблона уведомления
     */
    private Long id;
    /**
     * Уникальный идентификатор события
     */
    private Long notificationTemplateId;
    /**
     * Название шаблона
     */
    private String description;
    /**
     * Текст шаблона
     */
    private String bodyJson;
    /**
     * Наименования получателей, подразделений или вычисляемых получателей
     */
    private List<NotificationRecipientNameDto> recipients;
    /**
     * Канал отправки
     */
    private NotificationReceiverSystemDto receiverSystem;
    /**
     * Приоритет
     */
    private boolean priority;
    /**
     * Доступность
     */
    private boolean isEnabled;

    /**
     * Код модуля (из которого будет отправлено уведомление)
     */
    private String codeModule;
}
