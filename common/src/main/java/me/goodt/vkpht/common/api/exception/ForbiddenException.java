package me.goodt.vkpht.common.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при невозможности применения
 * запрашиваемого действия к текущему состоянию данных.
 *
 * <p>Является разновидностью ошибок запроса с возвращаемым кодом ответа 406.
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ForbiddenException extends RuntimeException {

	public ForbiddenException(String message) {
		super(message);
	}

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
