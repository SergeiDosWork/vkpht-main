package me.goodt.vkpht.module.notification.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о прикрепляемому файлу вложения.")
public class NotificationAttachmentDto {

    @Schema(description = """
        Уникальный идентификатор записи о прикрепляемом файле вложения.
        		
        Обязательно к указанию в запросе изменения шаблона без изменения состава файлов вложения.""",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;

    @Schema(description = """
        Путь к файлу для получения из файлового хранилища.
        		
        Выдается сервисом файлового хранилища после успешной загрузки файла.""",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String storageFilePath;

    @Schema(description = "Исходное имя файла.",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    @Schema(description = "Тип файла (MIME Type).",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileType;

}
