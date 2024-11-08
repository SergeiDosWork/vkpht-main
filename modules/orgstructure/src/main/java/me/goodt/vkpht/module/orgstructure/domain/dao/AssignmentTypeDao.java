package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QAssignmentTypeEntity;

@Repository
public class AssignmentTypeDao extends AbstractDao<AssignmentTypeEntity, Integer> {

    private static final QAssignmentTypeEntity meta = QAssignmentTypeEntity.assignmentTypeEntity;

    public AssignmentTypeDao(EntityManager em) {
        super(AssignmentTypeEntity.class, em);
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<AssignmentTypeEntity> find(AssignmentTypeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(AssignmentTypeFilter filter) {
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
