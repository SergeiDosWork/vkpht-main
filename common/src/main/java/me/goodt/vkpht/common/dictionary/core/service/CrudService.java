package me.goodt.vkpht.common.dictionary.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import me.goodt.vkpht.common.domain.entity.AbstractEntity;

public interface CrudService<T extends AbstractEntity<I>, I> {

    <K> K getById(I id, Converter<T, K> converter);

    <K> K getAll(Converter<List<T>, K> converter);

    <R> Page<R> getAll(Pageable paging, Converter<T, R> converter);

    <R> R create(Updater<T> creator, Converter<T, R> converter);

    T update(I id, Updater<T> updater);

    void delete(I id);

    void deleteAll();
}
