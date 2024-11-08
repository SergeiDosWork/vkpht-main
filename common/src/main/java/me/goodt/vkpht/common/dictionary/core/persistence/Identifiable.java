package me.goodt.vkpht.common.dictionary.core.persistence;

/**
 * Базовые интерфейс для всех классов сущностей {@link jakarta.persistence.Entity},
 * идентифицируемых по уникальному ключу ("id").
 *
 * @param <T> Тип идентификатора.
 */
public interface Identifiable<T> {

    /**
     * Возвращает значение идентификатора сущности.
     *
     * @return значение id или {@literal null}, если новая запись, еще не сохраненная в БД.
     */
    T getId();
}
