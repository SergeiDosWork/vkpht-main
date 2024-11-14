package me.goodt.vkpht.common.domain.mapper;

/**
 * Базовый интерфейс для маппинга сущностей в DTO и из DTO в сущности при CRUD операциях.
 *
 * @param <E> entity type
 * @param <D> dto type
 */
public interface CrudDtoMapper<E, D> extends DtoMapper<E, D> {
    E toNewEntity(D dto);

    E toUpdatedEntity(D dto, E entity);
}
