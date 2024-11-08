package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionCategoryFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionCategoryEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionCategoryEntity;

@Repository
public class PositionCategoryDao extends AbstractDao<PositionCategoryEntity, Long> {

    private static final QPositionCategoryEntity meta = QPositionCategoryEntity.positionCategoryEntity;

    public PositionCategoryDao(EntityManager em) {
        super(PositionCategoryEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<PositionCategoryEntity> find(PositionCategoryFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }
    public List<PositionCategoryEntity> find(PositionCategoryFilter filter) {
        Predicate where = toPredicate(filter);

        return findAll(where);
    }

    private Predicate toPredicate(PositionCategoryFilter filter) {
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
