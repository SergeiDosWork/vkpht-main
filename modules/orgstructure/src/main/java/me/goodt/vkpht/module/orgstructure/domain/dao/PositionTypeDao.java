package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionTypeEntity;

@Repository
public class PositionTypeDao extends AbstractDao<PositionTypeEntity, Long> {

    private static final QPositionTypeEntity meta = QPositionTypeEntity.positionTypeEntity;

    public PositionTypeDao(EntityManager em) {
        super(PositionTypeEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<PositionTypeEntity> find(PositionTypeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    public List<PositionTypeEntity> find(PositionTypeFilter filter) {
        Predicate where = toPredicate(filter);

        return findAll(where);
    }

    private Predicate toPredicate(PositionTypeFilter filter) {
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
