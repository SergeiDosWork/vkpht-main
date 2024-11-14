package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * DTO для отображения данных о шаблонах уведомлений на странице "Уведомления о событиях".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о шаблоне уведомлений для страницы \"Уведомления о событиях\".")
public class NotificationTemplateContentResponse {

    @Schema(description = "Уникальный идентификатор шаблона уведомления.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "Уникальный идентификатор события.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private Long notificationTemplateId;

    @Schema(description = "Название шаблона.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Информация о замещающем шаблоне.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private NotificationTemplateContentSubstituteInfo substitute;

    @Schema(description = "Тема (title) и тело (body) шаблона уведомления в формате JSON.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String bodyJson;

    /**
     * Получатели
     */
    @Schema(description = "Список email-адресов, добавленных как получатели уведомления",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> emailRecipients;

    @Schema(description = "Список сотрудников из оргструктуры, добавленных как получатели уведомлений",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationEmployeeRecipientDto> employeeRecipients;

    @Schema(description = "Список подразделений из оргструктуры, добавленных как получатели уведомлений",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationDivisionRecipientDto> divisionRecipients;

    @Schema(description = "Список вычисляемых получателей уведомлений.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationDynamicRecipientDto> dynamicRecipients;

    /**
     * Получатели копий
     */
    @Schema(description = "Список email-адресов, добавленных как получатели копии уведомления",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> emailRecipientsCopy;

    @Schema(description = "Список сотрудников из оргструктуры, добавленных как получатели копии уведомлений",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationEmployeeRecipientDto> employeeCopyRecipients;

    @Schema(description = "Список подразделений из оргструктуры, добавленных как получатели копии уведомлений",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationDivisionRecipientDto> divisionCopyRecipients;

    @Schema(description = "Список вычисляемых получателей копии уведомлений.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationDynamicRecipientDto> dynamicCopyRecipients;

    @Schema(description = "Канал отправки",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private NotificationReceiverSystemDto receiverSystem;

    @Schema(description = "Признак указания приоритета уведомления. True - Высокий, False - Обычный",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean priority;

    @Schema(description = "Признак доступности шаблона уведомления. True - Включен, False - Выключен",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean isEnabled;

    @Schema(description = """
        Информация о прикрепленных файлах вложений.
        		
        Содержит пустой список, если к шаблону не были прикреплены файлы.""",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NotificationAttachmentDto> attachments;

    @Schema(description = "Дата создания шаблона уведомления",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private Date dateFrom;

    @Schema(description = "Дата удаления шаблона уведомления",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date dateTo;

    @Schema(description = "Код модуля (из которого будет отправлено уведомление)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true)
    private String codeModule;

}
