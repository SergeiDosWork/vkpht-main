package me.goodt.vkpht.common.application.impl;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Objects;

import me.goodt.vkpht.common.dictionary.core.service.AbstractCrudService;
import me.goodt.vkpht.common.dictionary.core.service.Converter;
import me.goodt.vkpht.common.dictionary.core.service.Updater;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@NoArgsConstructor
public abstract class AbstractReadOnlyDictionaryService<T extends AbstractEntity<I>, I extends Serializable> extends AbstractCrudService<T, I> {

    public <R> Page<R> getAll(Pageable paging, Converter<T, R> converter) {
        Page<T> page = this.getDao().findAll(paging);
        Objects.requireNonNull(converter);
        return page.map(converter::convert);
    }

    @Override
    public final <R> R create(Updater<T> creator, Converter<T, R> converter) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    public final T update(I id, Updater<T> updater) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    public final void delete(I id) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    public final void deleteAll() {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    protected final void afterSaveCreated(T entity) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    protected final void afterCreate(T entity) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    protected final void beforeUpdate(T entity) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    protected final void afterUpdate(T entity) {
        throw new UnsupportedOperationException("Method is not allowed");
    }

    @Override
    protected final void beforeDelete(I id) {
        throw new UnsupportedOperationException("Method is not allowed");
    }
}
