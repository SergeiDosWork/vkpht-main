package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;
import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;
import me.goodt.vkpht.common.dictionary.core.entity.AbstractEntity;
import me.goodt.vkpht.common.dictionary.core.service.AbstractCrudService;
import me.goodt.vkpht.common.dictionary.core.util.MediaTypes;
import me.goodt.vkpht.common.dictionary.core.util.Responses;

@RequestMapping(produces = MediaTypes.JSON_UTF8)
public abstract class AbstractCrudController<I extends Serializable, R extends AbstractRes<R>> extends AbstractController implements CrudController<I, R> {

    protected abstract <T extends AbstractCrudService<? extends AbstractEntity<?>, I>> T getService();

    protected abstract <T extends AbstractAsm<Object, R>> T getAsm();

    @GetMapping("{id}")
    public R getById(@PathVariable I id) {
        return getService().getById(id, t -> getAsm().toRes(t));
    }

    @PostMapping
    public R create(@RequestBody R res) {
        return getService().create(e -> getAsm().create(e, res), t -> getAsm().toRes(t));
    }

    @PutMapping("{id}")
    public R update(@PathVariable I id, @RequestBody R res) {
        final AbstractEntity<I> entity = (AbstractEntity<I>) getService().update(id, e -> getAsm().update(e, res));
        return getAsm().toRes(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable I id) {
        getService().delete(id);
        return Responses.ok();
    }
}
