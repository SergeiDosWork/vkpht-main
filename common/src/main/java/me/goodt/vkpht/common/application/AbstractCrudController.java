package me.goodt.vkpht.common.application;

import jakarta.validation.groups.Default;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;

import me.goodt.vkpht.common.api.validation.Create;
import me.goodt.vkpht.common.api.validation.Update;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractController;
import me.goodt.vkpht.common.dictionary.core.controller.CrudController;
import me.goodt.vkpht.common.dictionary.core.service.AbstractCrudService;
import me.goodt.vkpht.common.dictionary.core.util.Responses;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@RequestMapping(
	produces = {"application/json;charset=UTF-8"}
)
public abstract class AbstractCrudController<I extends Serializable, R extends AbstractRes<R>> extends AbstractController implements CrudController<I, R> {
	protected AbstractCrudController() {
	}

	protected abstract <T extends AbstractCrudService<? extends AbstractEntity<?>, I>> T getService();

	protected abstract <T extends AbstractAsm<Object, R>> T getAsm();

	@GetMapping({"{id}"})
	public R getById(@PathVariable I id) {
		return this.getService().getById(id, t -> this.getAsm().toRes(t));
	}

	@PostMapping
	public R create(@Validated({Default.class, Create.class}) @RequestBody R res) {
		return this.getService().create(
			e -> this.getAsm().create(e, res),
			t -> this.getAsm().toRes(t)
		);
	}

	@PutMapping({"{id}"})
	public R update(@PathVariable I id, @Validated({Default.class, Update.class}) @RequestBody R res) {
		AbstractEntity<I> entity = this.getService().update(id, e -> this.getAsm().update(e, res));
		return this.getAsm().toRes(entity);
	}

	@DeleteMapping({"{id}"})
	public ResponseEntity<Void> delete(@PathVariable I id) {
		this.getService().delete(id);
		return Responses.ok();
	}
}
