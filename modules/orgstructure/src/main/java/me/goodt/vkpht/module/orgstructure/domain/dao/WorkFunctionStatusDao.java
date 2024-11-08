package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkFunctionStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkFunctionStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionStatusEntity;

@Repository
public class WorkFunctionStatusDao extends AbstractDao<WorkFunctionStatusEntity, Integer> {

    private static final QWorkFunctionStatusEntity meta = QWorkFunctionStatusEntity.workFunctionStatusEntity;

    public WorkFunctionStatusDao(EntityManager em) {
        super(WorkFunctionStatusEntity.class, em);
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

    public Page<WorkFunctionStatusEntity> find(WorkFunctionStatusFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        return findAll(where, pageable);
    }

    private Predicate toPredicate(WorkFunctionStatusFilter filter) {
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
