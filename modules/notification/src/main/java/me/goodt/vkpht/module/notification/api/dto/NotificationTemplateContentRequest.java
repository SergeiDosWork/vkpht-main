package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект запроса на создание или изменение шаблона уведомления о событии.")
public class NotificationTemplateContentRequest {

	@Schema(description = "Идентификатор записи уведомления о событии, к которой принадлежит шаблон",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long notificationTemplateId;

	@Schema(description = "Идентификатор канала отправки уведомления, для которого действует шаблон",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private Long receiverSystemId;

	@Schema(description = "Название шаблона уведомления",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String description;

	@Schema(description = """
		Идентификатор замещающего шаблона уведомления.
		
		Если указан, то уведомление отправляется по замещающему шаблону в случае, \
		если для текущего шаблона не будут вычислены получатели""",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private Long substituteId;

	@Schema(description = """
		Признак указания высокого приоритета уведомления.
		
		<code>true</code> - высокий; <code>false</code> - обычный.""",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private boolean priority;

	@Schema(description = """
		Признак доступности шаблона уведомления.
		
		<code>true</code> - включен; <code>false</code> - выключен.""",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private boolean enabled;

	@Schema(description = "Список идентификаторов сотрудников из оргструктуры, " +
		"добавленных в качестве получателей уведомлений.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<Long> recipientEmployeeIds;
	@Schema(description = "Список идентификаторов подразделений из оргструктуры, " +
		"добавленных в качестве получателей уведомлений.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<Long> recipientDivisionIds;
	@Schema(description = "Список идентификаторов вычисляемых получателей.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<Long> dynamicRecipientIds;
    @Schema(description = "Список email-адресов, добавленных как получатели уведомления",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true)
    private List<String> emailRecipients;

	@Schema(description = "Список идентификаторов сотрудников из оргструктуры, " +
		"добавленных в качестве получателей копии уведомлений.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<Long> copyRecipientEmployeeIds;
	@Schema(description = "Список идентификаторов подразделений из оргструктуры, " +
		"добавленных в качестве получателей копии уведомлений.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<Long> copyRecipientDivisionIds;
	@Schema(description = "Список идентификаторов вычисляемых получателей копии.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<Long> dynamicCopyRecipientIds;
    @Schema(description = "Список email-адресов, добавленных как получатели копии уведомления",
        requiredMode = Schema.RequiredMode.REQUIRED,
        nullable = true)
    private List<String> emailRecipientsCopy;

	@Schema(description = "Список прикрепляемых файлов вложений для отправки с сообщением.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		nullable = true)
	private List<NotificationAttachmentDto> attachments;

	@Schema(description = "Тема (title) и тело (body) шаблона уведомления в формате JSON.",
		requiredMode = Schema.RequiredMode.REQUIRED)
	private String bodyJson;

    @Schema(description = "Признак системного уведомления", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isSystem = false;

    @Schema(description = "Код модуля (из которого будет отправлено уведомление)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true)
    private String codeModule;
}
