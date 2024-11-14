package me.goodt.vkpht.common.domain.mapper;

/**
 * Базовый интерфейс для маппинга классов сущностей в DTO.
 *
 * <p>Интерфейс также является контрактом для реализации маперов через Mapstruct.
 *
 * @param <E> тип сущности
 * @param <D> тип DTO-класса
 */
public interface DtoMapper<E, D> {
    D toDto(E entity);
}
