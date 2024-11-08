package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionRankFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionRankEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionRankEntity;

@Repository
public class PositionRankDao extends AbstractDao<PositionRankEntity, Long> {

    private static final QPositionRankEntity meta = QPositionRankEntity.positionRankEntity;

    public PositionRankDao(EntityManager em) {
        super(PositionRankEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<PositionRankEntity> find(PositionRankFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    public List<PositionRankEntity> find(PositionRankFilter filter) {
        Predicate where = toPredicate(filter);

        return findAll(where);
    }

    private Predicate toPredicate(PositionRankFilter filter) {
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
