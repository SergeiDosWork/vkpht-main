package me.goodt.vkpht.common.api.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Ответ с описанием ошибки, возникшей при обработке запроса к API.
 *
 * <p>Возвращается в качестве тела ответа во всех ответах * с кодом 4xx и 5xx.
 *
 * <p>Является базовым классом для всех других ответов с ошибками,
 * если требуется добавить дополнительные поля и данные к ответу.
 *
 * @see ValidationErrorResponse
 */
@Getter
@Schema(description = "Информация об ошибке, возникшей во время обработки запроса к API.")
public class ErrorResponse {

    @Schema(description = "Статус ответа.\n\nОбычно соответствует коду HTTP 4xx или 5xx",
            example = "404",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final int status;

    @Schema(description = "Строковый код ошибки, идентифицирующий возникшую ситуацию.",
            example = "NotFoundError",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final String error;

    @Schema(description = "Текстовое сообщение, описывающее произошедшее событие или причину ошибки.\n\n" +
                          "Может отсутствовать.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private final String message;

    @Schema(description = "Дата и время возникновения ошибки (формирования ответного сообщения).",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String error) {
        this(status, error, null);
    }

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
