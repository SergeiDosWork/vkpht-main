package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentCategoryFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentCategoryEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QAssignmentCategoryEntity;

@Repository
public class AssignmentCategoryDao extends AbstractDao<AssignmentCategoryEntity, Long> {

    private static final QAssignmentCategoryEntity meta = QAssignmentCategoryEntity.assignmentCategoryEntity;

    public AssignmentCategoryDao(EntityManager em) {
        super(AssignmentCategoryEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public Page<AssignmentCategoryEntity> find(AssignmentCategoryFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(AssignmentCategoryFilter filter) {
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
