package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import me.goodt.vkpht.common.dictionary.core.dto.DictionaryMetaResponse;

/**
 * Базовый класс для DictController с реализацией генерации мета-информации.
 *
 * <p>Контроллеры справочников могут наследовать данный класс и реализовывать
 * непосредственно методы только CRUD операций.
 *
 * @param <ID> Тип идентификатора записи
 * @param <T>  Тип обрабатываего объекта записи (DTO)
 * @param <F>  Тип фильтра - объекта с полями-параметрами фильтрации
 */
public abstract class MetaDictController<ID, T, F> implements DictController<ID, T, F> {

    private final DictionaryMetaGenerator metaGenerator;

    public MetaDictController(DictionaryMetaGenerator metaGenerator) {
        this.metaGenerator = metaGenerator;
    }

    @GetMapping("/meta")
    public DictionaryMetaResponse getMeta() {
        DictionaryMetaResponse response = buildMetaResponse();
        getRelatedLinks().forEach(response::add);
        return response;
    }

    protected DictionaryMetaResponse buildMetaResponse() {
        Class<T> dtoType = getDtoClass();
        Class<F> filterType = getFilterClass();

        DictionaryMetaResponse response = new DictionaryMetaResponse();
        response.setDto(this.metaGenerator.generate(dtoType));
        // Контроллер может быть реализован как "без фильтрации".
        if (filterType != null) {
            response.setFilter(this.metaGenerator.generate(filterType));
        }
        return response;
    }

    /**
     * Возвращает класс обрабатываемого объекта записи (DTO), используемый в мета-модели.
     *
     * <p>Базовая реализация основывается на рефлексии и извлечении параметров интерфейса {@link DictController}.
     * Наследники могут переопределить метод, чтобы возвращать другой класс.
     */
    @SuppressWarnings("unchecked")
    protected Class<T> getDtoClass() {
        ParameterizedType controllerClass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) controllerClass.getActualTypeArguments()[1];

    }

    /**
     * Возвращает класс для объекта фильтра, используемый в мета-модели.
     *
     * <p>Базовая реализация основывается на рефлексии и извлечении параметров интерфейса {@link DictController}.
     * Наследники могут переопределить метод, чтобы возвращать другой класс.
     *
     * @apiNote Может возвращать <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    protected Class<F> getFilterClass() {
        ParameterizedType controllerClass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<F>) controllerClass.getActualTypeArguments()[2];

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
