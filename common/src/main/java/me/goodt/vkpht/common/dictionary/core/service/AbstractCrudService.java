package me.goodt.vkpht.common.dictionary.core.service;

import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.dictionary.core.error.RTException;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

public abstract class AbstractCrudService<T extends AbstractEntity<I>, I extends Serializable> implements CrudService<T, I> {

    protected abstract <D extends AbstractDao<T, I>> D getDao();

    @Override
    @Transactional(readOnly = true)
    public <R> R getById(I id, Converter<T, R> converter) {
        final T entity = getDao().findById(id).orElseThrow(() -> RTException.of("Entity %s not found by id %s".formatted(getEntityClazz(), id)));
        return converter.convert(entity);
    }

    protected Class<?> getEntityClazz() {
        ResolvableType resolvableType = ResolvableType.forClass(this.getClass()).as(AbstractCrudService.class);
        return resolvableType.getGeneric(0).resolve();
    }

    /**
     * iGurkin по умолчанию создаем сущность с пустым конструктором,
     * если надо что-то больше, то override в помощь
     */
    protected T inst() {
        final Class<?> clazz = getEntityClazz();
        try {
            //noinspection unchecked
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw RTException.of("No default constructor " + clazz.getSimpleName());
        }
    }

    @Override
    public <R> R getAll(Converter<List<T>, R> converter) {
        throw new UnsupportedOperationException();// потенциально опасная операция, поэтому в 99% реализация не требуется
    }

    @Override
    public <R> Page<R> getAll(Pageable paging, Converter<T, R> converter) {
        throw new UnsupportedOperationException();// потенциально опасная операция, поэтому в 99% реализация не требуется
    }

    @Override
    @Transactional
    public <R> R create(Updater<T> creator, Converter<T, R> converter) {
        T entity = inst();
        creator.update(entity);
        afterCreate(entity);
        getDao().save(entity);
        afterSaveCreated(entity);
        return converter.convert(entity);
    }

    protected void afterSaveCreated(T entity) {
    }

    protected void afterCreate(T entity) {
    }

    @Override
    @Transactional
    public T update(I id, Updater<T> updater) {
        final T entity = getById(id, o -> o);
        beforeUpdate(entity);
        updater.update(entity);
        afterUpdate(entity);
        return getDao().save(entity);
    }

    protected void beforeUpdate(T entity) {
    }

    protected void afterUpdate(T entity) {
    }

    @Override
    @Transactional
    public void delete(I id) {
        beforeDelete(id);
        getDao().deleteById(id);
    }

    protected void beforeDelete(I id) {

    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();// потенциально опасная операция, поэтому в 99% реализация не требуется
    }
}
