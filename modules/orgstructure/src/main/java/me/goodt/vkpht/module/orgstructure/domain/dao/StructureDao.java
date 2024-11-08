package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.StructureFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QStructureTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.StructureEntity;

@Repository
public class StructureDao extends AbstractDao<StructureEntity, Long> {

    private static final QStructureEntity meta = QStructureEntity.structureEntity;

    public StructureDao(EntityManager em) {
        super(StructureEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public StructureEntity find(StructureFilter filter) {
        Predicate where = toPredicate(filter);
        final QStructureStatusEntity qStatus = new QStructureStatusEntity("status");
        final QStructureTypeEntity qType = new QStructureTypeEntity("type");
        return query().selectFrom(meta)
            .where(where)
            .join(qStatus).on(meta.status.id.eq(qStatus.id))
            .join(qType).on(meta.type.id.eq(qType.id))
            .fetchFirst();
    }

    private Predicate toPredicate(StructureFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getLocationId() != null) {
            where.and(meta.location.id.eq(filter.getLocationId()));
        }
        if (filter.getParentId() != null) {
            where.and(meta.parent.id.eq(filter.getParentId()));
        }
        if (filter.getStatusId() != null) {
            where.and(meta.status.id.eq(filter.getStatusId()));
        }
        if (filter.getTypeId() != null) {
            where.and(meta.type.id.eq(filter.getTypeId()));
        }
        if (filter.getPrecursorId() != null) {
            where.and(meta.precursor.id.eq(filter.getPrecursorId()));
        }
        if (filter.getFullName() != null) {
            where.and(meta.fullName.eq(filter.getFullName()));
        }
        if (filter.getShortName() != null) {
            where.and(meta.shortName.eq(filter.getShortName()));
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
