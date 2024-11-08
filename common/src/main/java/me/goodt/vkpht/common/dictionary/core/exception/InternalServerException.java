package me.goodt.vkpht.common.dictionary.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасывамое для возврата клиенту сигнала об ошибке с кодом 500 Internal Server Error.
 *
 * <p>Данный класс может служит для гарантированной доставки клиенту текста сообщения об ошибке,
 * если для всех неотловленных исключениях клиенту будет возвращаться сообщение-"заглушка".
 *
 * <p>Данный класс можно расширять для проброса более специфичных внутренних ошибок сервера.
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
