package me.goodt.vkpht.common.application.exception;

import lombok.Getter;

/**
 * Исключительные ситуации в работе с рассчитываемыми ConditionEntity
 */
@Getter
public class ConditionCalculationException extends RuntimeException {

    public ConditionCalculationException(String msg) {
        super(msg);
    }

    public ConditionCalculationException(String msg, Throwable clause) {
        super(msg, clause);
    }
}
