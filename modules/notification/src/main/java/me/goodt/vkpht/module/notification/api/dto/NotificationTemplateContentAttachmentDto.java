package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationTemplateContentAttachmentDto {

	/**
	 * Шаблон уведомления, которому принадлежит текущая запись вложения
	 */
	private NotificationTemplateContentDto notificationTemplateContent; //

	/**
	 * Имя или путь к файлу для получения его содержимого из файлового хранилища.
	 *
	 * <p>Формат содержимого зависит от используемой реализации или внешнего сервиса,
	 * выступающего в виде файлового хранилища.
	 */
	private String storageFilePath;

	/**
	 * Исходное имя загруженного файла.
	 */
	private String fileName;

	/**
	 * Тип файла (MIME Type).
	 *
	 * <p>Требуется для указания типа файла вложения в MimeMessage отправляемого письма.
	 * <p>Может быть значение <code>null</code>, если тип не был явно указан при загрузке файла.
	 */
	private String fileType;

}
