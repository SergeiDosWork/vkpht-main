package me.goodt.vkpht.common.dictionary.core.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Ответ на запрос получения мета-данных справочника.
 *
 * <p>Расширяет RepresentationModel из HATEOAS
 * для указания ссылок "links" на другие связанные справчники.
 */
@Getter
@Setter
public class DictionaryMetaResponse extends RepresentationModel<DictionaryMetaResponse> {

    /**
     * Описание DTO с типами и ограничениями, используемого для редактирования модели справочника.
     */
    private Object dto;
    /**
     * Описание объекта фильтра, определяющего параметры для фильтрации и их допустимые значения.
     *
     * <p>Может быть <code>null</code>, если справочник не реализует фильтрацию.
     */
    private Object filter;
}
