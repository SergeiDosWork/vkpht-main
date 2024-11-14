package me.goodt.vkpht.common.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасывамое для возврата клиенту сигнала об ошибке с кодом 500 Internal Server Error.
 *
 * <p>Данный класс служит для гарантированной доставки клиенту текста сообщения об ошибке,
 * так как для всех остальных неотловленных исключениях клиенту возвращается сообщение-"заглушка".
 *
 * <p>Данный класс можно расширять для проброса более специфичных внутренних ошибок.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
