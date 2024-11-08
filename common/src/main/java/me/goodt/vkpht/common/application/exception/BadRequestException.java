package me.goodt.vkpht.common.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Общий неспециализированных класс для возврата ошибки "Некорректный запрос".
 *
 * <p>Выбрасывается при ошибках валидации параметров входящего запроса.
 * А также при невозможности применения указанных параметров для текущего ресура.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
