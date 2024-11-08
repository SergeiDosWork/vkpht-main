package me.goodt.vkpht.common.dictionary.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import me.goodt.vkpht.common.dictionary.core.dto.DictionaryMetaResponse;

/**
 * Интерфейс контроллера для обработки запросов к справочникам.
 *
 * <p>Все справочники имеют шаблонный набор CRUD операций и представлют
 * мета-данные с описанием своих полей и связей с другими справочниками.
 *
 * <p>Данные интерфейс является обязательными для реализации всеми справочниками,
 * так как определяет "станадрт" для API работы со справчоными данными.
 *
 * @param <ID> Тип идентификатора записи справочника
 * @param <T>  Тип обрабатываемого объекта справочника (DTO)
 * @param <F>  Тип объекта с параметрами фильтрации.
 */
public interface DictController<ID, T, F> {

    /**
     * Получение объекта мета-модели с данными текущего справочника.
     */
    @GetMapping({"/meta"})
    DictionaryMetaResponse getMeta();

    /**
     * Постраничный поиск записей с возможностью фильтрации данных.
     */
    @GetMapping
    Page<T> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "50") int size,
                    @RequestParam(name = "sortBy", required = false) String sortBy,
                    @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection,
                    F filter);

    @GetMapping("/{id}")
    T get(@PathVariable("id") ID id);

    @PostMapping
    T create(@RequestBody T request);

    @PutMapping("/{id}")
    T update(@PathVariable("id") ID id, @RequestBody T request);

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable("id") ID id);
}
