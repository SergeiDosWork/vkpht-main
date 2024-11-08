package me.goodt.vkpht.common.dictionary.core.controller;

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

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;
import me.goodt.vkpht.common.dictionary.core.dto.DictionaryMetaResponse;

/**
 * Базовый контроллер для обработки запросов к справочникам.
 *
 * @param <I> тип поля идентификатора (ID) обрабатываемой сущности.
 * @param <R>
 * @deprecated Необходимо использовать новую версию {@link AbstractAsmDictController}.
 */
@Deprecated
public abstract class AbstractDictionaryController<I extends Serializable, R extends AbstractRes<R>>
    extends AbstractCrudController<I, R> {

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
        return getService().getAll(pageRequest, getAsm()::toRes);
    }

    @GetMapping("/meta")
    public DictionaryMetaResponse getMeta() {
        Class<R> dtoType = (Class<R>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[1];

        DictionaryMetaResponse response = new DictionaryMetaResponse();
        response.setDto(this.metaGenerator.generate(dtoType));

        getRelatedLinks().forEach(response::add);

        return response;
    }

    /**
     * Возвращает коллекцию со списком ссылок на связанные ресурсы.
     *
     * <p>Указанные ссылки будут подставляться в ответе на "/meta" запрос.
     * <pre>{@code
     * @Override
     * protected Collection<Link> getRelatedLinks() {
     *     return List.of(WebMvcLinkBuilder.linkTo(SomeDictController.class).withRel("some"));
     * }
     * }</pre>
     */
    protected abstract Collection<Link> getRelatedLinks();
}
