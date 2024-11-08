package me.goodt.vkpht.common.application.util;

/**
 * Интерфейс для функциональности построения мета информации над используемым типом DTO-модели.
 *
 * @implNote Реализации генератора сами определяют тип и формат
 *           создаваемого объекта с мета-данными.
 */
public interface DictionaryMetaGenerator {

    /**
     * Возвращает объект с мета-данными для указанного класса DTO.
     */
    Object generate(Class<?> dtoClass);
}
