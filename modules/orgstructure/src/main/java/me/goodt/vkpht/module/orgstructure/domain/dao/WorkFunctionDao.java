package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkFunctionFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkFunctionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkFunctionEntity;

@Repository
public class WorkFunctionDao extends AbstractDao<WorkFunctionEntity, Long> {

    private static final QWorkFunctionEntity meta = QWorkFunctionEntity.workFunctionEntity;

    public WorkFunctionDao(EntityManager em) {
        super(WorkFunctionEntity.class, em);
    }

    public Page<WorkFunctionEntity> find(WorkFunctionFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(WorkFunctionFilter filter) {
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

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
