package me.goodt.vkpht.common.dictionary.core.persistence;

import java.util.Date;

/**
 * Интерфейс, сигнализирующий о том, что сущность является ревизионной.
 * <p>
 * Позволяет хранить и извлекать информацию о создании и изменении записи.
 */
public interface Auditable {

    /**
     * Возвращает дату создания сущности.
     */
    Date getDateFrom();

    /**
     * Устанавливает дату создания сущности.
     */
    void setDateFrom(Date dateFrom);

    /**
     * Возвращает идентификатор сотрудника(пользователя), создавшего сущность.
     */
    Long getAuthorEmployeeId();

    /**
     * Устанавливает идентификатор сотрудника(пользователя), создавшего сущность.
     */
    void setAuthorEmployeeId(Long authorEmployeeId);

    /**
     * Возвращает дату последнего изменения сущности.
     */
    Date getUpdateDate();

    /**
     * Устанавливает дату последнего изменения сущности.
     */
    void setUpdateFrom(Date updateFrom);

    /**
     * Возвращает идентификатор последнего сотрудника(пользователя), измненившего сущность.
     */
    Long getUpdateEmployeeId();

    /**
     * Устанавливает идентификатор последнего сотрудника(пользователя), измненившего сущность.
     */
    void setUpdateEmployeeId(Long updateEmployeeId);
}
