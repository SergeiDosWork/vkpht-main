package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionGroupFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionGroupEntity;

@Repository
public class PositionGroupDao extends AbstractDao<PositionGroupEntity, Long> {

    private static final QPositionGroupEntity meta = QPositionGroupEntity.positionGroupEntity;

    public PositionGroupDao(EntityManager em) {
        super(PositionGroupEntity.class, em);
    }

    public Page<PositionGroupEntity> find(PositionGroupFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    public List<PositionGroupEntity> find(PositionGroupFilter filter) {
        Predicate where = toPredicate(filter);

        return findAll(where);
    }

    private Predicate toPredicate(PositionGroupFilter filter) {
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
