package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.FunctionStatusFilter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QFunctionStatusEntity;

@Repository
public class FunctionStatusDao extends AbstractDao<FunctionStatusEntity, Integer> {

    private static final QFunctionStatusEntity meta = QFunctionStatusEntity.functionStatusEntity;

    public FunctionStatusDao(EntityManager em) {
        super(FunctionStatusEntity.class, em);
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<FunctionStatusEntity> find(FunctionStatusFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        return findAll(where, pageable);
    }

    private Predicate toPredicate(FunctionStatusFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getName() != null) {
            where.and(meta.name.eq(filter.getName()));
        }
        if (filter.getExternalId() != null) {
            where.and(meta.externalId.eq(filter.getExternalId()));
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
