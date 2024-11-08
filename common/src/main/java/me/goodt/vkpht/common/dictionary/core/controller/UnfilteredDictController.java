package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Базовая реализация {@link DictController} но без возможности поиска по фильтру.
 *
 * <p>Вместо метода поиска по фильтру предоставляет метод только постраничного поиска.
 *
 * @param <ID> Тип идентификатора записи
 * @param <T>  Тип обрабатываего объекта записи (DTO)
 */
public abstract class UnfilteredDictController<ID, T> extends MetaDictController<ID, T, Object> {

    public UnfilteredDictController(DictionaryMetaGenerator metaGenerator) {
        super(metaGenerator);
    }

    @Override
    protected Class<Object> getFilterClass() {
        return null;
    }

    @Override
    public Page<T> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "50") int size,
                           @RequestParam(name = "sortBy", required = false) String sortBy,
                           @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection,
                           Object dummy) {
        return findAll(page, size, sortBy, sortDirection);
    }

    /**
     * Абстрактный вариант метода "findAll" для постраничного получения записей без реализации фильтра.
     */
    public abstract Page<T> findAll(int page, int size, String sortBy, Sort.Direction sortDirection);
}
