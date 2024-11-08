package me.goodt.vkpht.common.dictionary.core.error;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @deprecated Следует использовать специализированные классы исключений
 * из пакета {@link me.goodt.vkpht.common.dictionary.core.exception}
 **/
@Getter
@Accessors(chain = true, fluent = true)
@Deprecated
public class RTException extends RuntimeException {

    private final String msg;
    @Setter
    private Integer status = 500;

    protected RTException(String msg) {
        this.msg = msg;
    }

    public static RTException of(String msg) {
        return new RTException(msg);
    }
}
