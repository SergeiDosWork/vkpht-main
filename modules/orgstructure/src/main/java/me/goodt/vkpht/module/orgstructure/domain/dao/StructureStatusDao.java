package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.StructureStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureStatusEntity;

@Repository
public class StructureStatusDao extends AbstractDao<StructureStatusEntity, Integer> {

    private static final QStructureStatusEntity meta = QStructureStatusEntity.structureStatusEntity;
    private static final QStructureEntity structure = QStructureEntity.structureEntity;

    public StructureStatusDao(EntityManager em) {
        super(StructureStatusEntity.class, em);
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public Page<StructureStatusEntity> find(StructureStatusFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        List<StructureStatusEntity> content = query().selectFrom(meta)
            .where(where)
            .join(structure).on(meta.id.eq(structure.status.id))
            .distinct()
            .fetch();
        return toPage(content, pageable);
    }

    private Page<StructureStatusEntity> toPage(List<StructureStatusEntity> list, Pageable pageable) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private Predicate toPredicate(StructureStatusFilter filter) {
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
