package me.goodt.vkpht.common.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое при отсутствии запрашиваемой сущности.
 *
 * <p>Может использоваться на уровне бизнес-логики(сервисов),
 * сигнализируя об отсутствии целевого объекта для требуемого действия,
 * так и на уровне REST API (контроллеры) для возврата ответа с кодом 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
