package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;
import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;
import me.goodt.vkpht.common.dictionary.core.entity.AbstractEntity;
import me.goodt.vkpht.common.dictionary.core.service.AbstractCrudService;
import me.goodt.vkpht.common.dictionary.core.util.Responses;

/**
 * Реализация DictController на основе AbstractCrudService и AbstractAsm,
 * но с возможностью поиска зписей по объекту фильтра.
 *
 * <p>Наследникам необходимо реализовать метод {@link #findByPageable(Pageable, Object)}
 * для вызова собственного метода в AbstractCrudService для поиска по фильтру.
 *
 * @param <I> тип поля идентификатора (ID) обрабатываемой сущности.
 * @param <R> Тип обрабатываемого ресурса (DTO)
 * @param <F> Тип объект фильтра.
 */
public abstract class AbstractFilterAsmDictController<I extends Serializable, R extends AbstractRes<R>, F>
    extends MetaDictController<I, R, F> {

    public AbstractFilterAsmDictController(DictionaryMetaGenerator metaGenerator) {
        super(metaGenerator);
    }

    protected abstract <T extends AbstractCrudService<? extends AbstractEntity<?>, I>> T getService();

    protected abstract <T extends AbstractAsm<Object, R>> T getAsm();

    @GetMapping
    public Page<R> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "50") int size,
                           @RequestParam(name = "sortBy", required = false) String sortBy,
                           @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection,
                           F filter) {
        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = Sort.by(sortDirection, sortBy);
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return findByPageable(pageRequest, filter);
    }

    /**
     * Получение страницы записей по указанному фильтру.
     *
     * <p>Вариант метода получения всех записей {@link #findAll(int, int, String, Sort.Direction, Object)},
     * с параметрами пагинации, собранными в один объект Pageable.
     */
    protected abstract Page<R> findByPageable(Pageable pageable, F filter);

    @GetMapping("{id}")
    public R get(@PathVariable("id") I id) {
        return getService().getById(id, t -> getAsm().toRes(t));
    }

    @PostMapping
    public R create(@RequestBody R res) {
        return getService().create(e -> getAsm().create(e, res), t -> getAsm().toRes(t));
    }

    @PutMapping("{id}")
    public R update(@PathVariable("id") I id, @RequestBody R res) {
        final AbstractEntity<I> entity = getService().update(id, e -> getAsm().update(e, res));
        return getAsm().toRes(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") I id) {
        getService().delete(id);
        return Responses.ok();
    }
}
