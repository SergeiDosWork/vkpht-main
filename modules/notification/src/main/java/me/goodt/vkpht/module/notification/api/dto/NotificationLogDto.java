package me.goodt.vkpht.module.notification.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Карточка журнал уведомлений
 */
@Getter
@Setter
@NoArgsConstructor
public class NotificationLogDto {

    private Long receiverSystemId;                                      // id канала отправки уведомления
    private String receiverSystemName;                                  // Наименование канала отправки уведомления
    private String description;                                         // название уведомления
    private Long id;                                                    // ID шаблона уведомления
    private Boolean priority;                                           // приоритет уведомления
    private String recipients;                                          // получатели уведомления
    private String recipientsCopy;                                      // получатели копии уведомления
    private Date dateModify;                                            // дата и время изменения
    private String status;                                              // статус отправки уведомления
    private String errorMessage;                                        // описание ошибки
    private List<NotificationTemplateContentAttachmentDto> attachments; // вложения
    private String subscribe;                                           // тема уведомления
    private String message;                                             // текст уведомления
    private String codeModule;                                          // код модуля, из которого отправлено уведомление
    private String moduleName;                                          // Название модуля, из которого отправлено уведомление
}
