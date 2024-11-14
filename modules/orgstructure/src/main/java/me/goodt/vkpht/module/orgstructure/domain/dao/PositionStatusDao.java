package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionStatusEntity;

@Repository
public class PositionStatusDao extends AbstractDao<PositionStatusEntity, Integer> {

    private static final QPositionStatusEntity meta = QPositionStatusEntity.positionStatusEntity;

    public PositionStatusDao(EntityManager em) {
        super(PositionStatusEntity.class, em);
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public Page<PositionStatusEntity> find(PositionStatusFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(PositionStatusFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
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
