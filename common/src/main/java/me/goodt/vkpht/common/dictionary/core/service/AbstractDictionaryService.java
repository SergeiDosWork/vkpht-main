package me.goodt.vkpht.common.dictionary.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

import me.goodt.vkpht.common.dictionary.core.entity.AbstractEntity;

/**
 * Базовый класс CRUD-service для справочников.
 */
public abstract class AbstractDictionaryService<T extends AbstractEntity<I>, I extends Serializable>
    extends AbstractCrudService<T, I> {

    @Override
    public <R> Page<R> getAll(Pageable paging, Converter<T, R> converter) {
        Page<T> page = getDao().findAll(paging);
        return page.map(converter::convert);
    }
}
