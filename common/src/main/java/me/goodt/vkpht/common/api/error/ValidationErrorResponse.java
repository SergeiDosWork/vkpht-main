package me.goodt.vkpht.common.api.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "Информация об ошибках при проверке данных полученного запроса.")
public class ValidationErrorResponse extends ErrorResponse {

    @Schema(description = "Список сообщений всех ошибок при валидации запроса.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<String> constraints;

    public ValidationErrorResponse(int status, String error, String message, List<String> constraints) {
        super(status, error, message);
        this.constraints = constraints;
    }
}
