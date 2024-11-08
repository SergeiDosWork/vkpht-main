package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QAssignmentStatusEntity;

@Repository
public class AssignmentStatusDao extends AbstractDao<AssignmentStatusEntity, Integer> {

    private static final QAssignmentStatusEntity meta = QAssignmentStatusEntity.assignmentStatusEntity;

    public AssignmentStatusDao(EntityManager em) {
        super(AssignmentStatusEntity.class, em);
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<AssignmentStatusEntity> find(AssignmentStatusFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(AssignmentStatusFilter filter) {
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
