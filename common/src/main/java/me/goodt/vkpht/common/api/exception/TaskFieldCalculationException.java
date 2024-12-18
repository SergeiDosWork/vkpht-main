package me.goodt.vkpht.common.api.exception;

import lombok.Getter;

/**
 * Ошибка при расчете value вычисляемых полей
 */
@Getter
public class TaskFieldCalculationException extends RuntimeException {

    public TaskFieldCalculationException(String message) {
        super(message);
    }
}
