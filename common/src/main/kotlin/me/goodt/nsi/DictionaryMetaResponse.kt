package me.goodt.nsi

import lombok.Getter
import lombok.Setter
import org.springframework.hateoas.RepresentationModel

/**
 * Ответ на запрос получения мета-данных справочника.
 *
 *
 * Расширяет RepresentationModel из HATEOAS
 * для указания ссылок "links" на другие связанные справчники.
 */
class DictionaryMetaResponse : RepresentationModel<DictionaryMetaResponse?>() {
    /**
     * Описание DTO с типами и ограничениями, используемого для редактирования модели справочника.
     */
    var dto: Any? = null

    /**
     * Описание объекта фильтра, определяющего параметры для фильтрации и их допустимые значения.
     *
     *
     * Может быть `null`, если справочник не реализует фильтрацию.
     */
    var filter: Any? = null
}
