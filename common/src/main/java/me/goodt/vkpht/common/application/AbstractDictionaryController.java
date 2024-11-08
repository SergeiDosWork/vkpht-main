package me.goodt.vkpht.common.application;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Objects;

import me.goodt.vkpht.common.application.asm.AbstractAsm;
import me.goodt.vkpht.common.application.util.DictionaryMetaGenerator;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@NoArgsConstructor
public abstract class AbstractDictionaryController <I extends Serializable, R> extends AbstractCrudController<I, R> {
    @Autowired
	private DictionaryMetaGenerator metaGenerator;


	@GetMapping
	public Page<R> findAll(@RequestParam(defaultValue = "0") int page,
						   @RequestParam(defaultValue = "50") int size,
						   @RequestParam(required = false) String sortBy,
						   @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {

		Sort sort = Sort.unsorted();
		if (sortBy != null) {
			sort = Sort.by(sortDirection, sortBy);
		}

		PageRequest pageRequest = PageRequest.of(page, size, sort);
		AbstractCrudService<? extends AbstractEntity<?>, I> var10000 = this.getService();
		AbstractAsm<Object, R> var10002 = this.getAsm();
		Objects.requireNonNull(var10002);
		return var10000.getAll(pageRequest, var10002::toRes);
	}

	@GetMapping({"/meta"})
	public DictionaryMetaResponse getMeta() {
		Class<R> dtoType = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		DictionaryMetaResponse response = new DictionaryMetaResponse();
		response.setDto(this.metaGenerator.generate(dtoType));
		Collection<Link> var10000 = this.getRelatedLinks();
		Objects.requireNonNull(response);
		var10000.forEach(response::add);
		return response;
	}

	protected abstract Collection<Link> getRelatedLinks();

}
