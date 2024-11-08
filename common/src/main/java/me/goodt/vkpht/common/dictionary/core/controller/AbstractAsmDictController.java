package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * Реализация DictController как адаптер для справочников на основе CrudController и AbstractAsm.
 *
 * <p>Используется в качестве замены класса AbstractDictionaryService.
 *
 * @param <I> тип поля идентификатора (ID) обрабатываемой сущности.
 * @param <R> Тип обрабатываемого ресурса (DTO)
 * @apiNote Данные реализации справочников на основе AbstractCrudService
 * не поддерживают поиск с фильтрацией.
 */
public abstract class AbstractAsmDictController<I extends Serializable, R extends AbstractRes<R>>
    extends UnfilteredDictController<I, R> {

    public AbstractAsmDictController(DictionaryMetaGenerator metaGenerator) {
        super(metaGenerator);
    }

    protected abstract <T extends AbstractCrudService<? extends AbstractEntity<?>, I>> T getService();

    protected abstract <T extends AbstractAsm<Object, R>> T getAsm();

    public Page<R> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = Sort.by(sortDirection, sortBy);
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return getService().getAll(pageRequest, getAsm()::toRes);
    }

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
