package me.goodt.vkpht.common.dictionary.core.dao;

import jakarta.persistence.AttributeConverter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEnumConverter<T extends Enum<T> & PersistableEnum<E>, E> implements AttributeConverter<T, E> {

    private final String enumClassName;
    private final Map<E, T> values;

    protected AbstractEnumConverter(Class<T> clazz) {
        enumClassName = clazz.getSimpleName();
        this.values = new HashMap<>(clazz.getEnumConstants().length);
        for (T v : clazz.getEnumConstants()) {
            this.values.put(v.getValue(), v);
        }
    }

    @Override
    public E convertToDatabaseColumn(T attr) {
        return attr != null ? attr.getValue() : null;
    }

    @Override
    public T convertToEntityAttribute(E dbVal) {
        if (dbVal == null) {
            return null;
        }
        final T val = values.get(dbVal);
        if (val == null) {
            valueNotFound(dbVal);
        }
        return val;
    }

    /**
     * Метод генерации исключения в случае, если оно не содержится в enum
     * можно определить свое поведение
     */
    public void valueNotFound(E dbVal) {
        final String msg = "Converter %s receive unsupported value %s".formatted(enumClassName + "." + this.getClass().getSimpleName(), dbVal);
        throw new UnsupportedOperationException(msg);
    }
}
