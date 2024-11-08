package me.goodt.vkpht.common.dictionary.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Общий неспециализированных класс для возврата ответа с ошибкой "Некорректный запрос".
 *
 * <p>Выбрасывается при ошибках валидации параметров входящего запроса.
 * А также при невозможности применения передаваемых параметров для указанного ресура.
 *
 * <p>Может выступать в качестве общего исключения для всех ошибок,
 * где требуется возвращать ответ с кодом 4xx.
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
