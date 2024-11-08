package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.StructureTypeFilter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureTypeEntity;

@Repository
public class StructureTypeDao extends AbstractDao<StructureTypeEntity, Integer> {

    private static final QStructureTypeEntity meta = QStructureTypeEntity.structureTypeEntity;
    private static final QStructureEntity structure = QStructureEntity.structureEntity;

    public StructureTypeDao(EntityManager em) {
        super(StructureTypeEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
                .where(meta.name.eq(name))
                .fetchCount() > 0;
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<StructureTypeEntity> find(StructureTypeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        List<StructureTypeEntity> content = query().selectFrom(meta)
            .where(where)
            .join(structure).on(meta.id.eq(structure.type.id))
            .distinct()
            .fetch();
        return toPage(content, pageable);
    }

    private Page<StructureTypeEntity> toPage(List<StructureTypeEntity> list, Pageable pageable) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private Predicate toPredicate(StructureTypeFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getExternalId() != null) {
            where.and(meta.externalId.eq(filter.getExternalId()));
        }
        if (filter.getName() != null) {
            where.and(meta.name.eq(filter.getName()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }
        return where;
    }
}
