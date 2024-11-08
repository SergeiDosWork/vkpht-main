package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentRotationFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QAssignmentRotationEntity;

@Repository
public class AssignmentRotationDao extends AbstractDao<AssignmentRotationEntity, Integer> {

    private static final QAssignmentRotationEntity meta = QAssignmentRotationEntity.assignmentRotationEntity;

    public AssignmentRotationDao(EntityManager em) {
        super(AssignmentRotationEntity.class, em);
    }

    public Page<AssignmentRotationEntity> find(AssignmentRotationFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        return findAll(where, pageable);
    }

    public List<AssignmentRotationEntity> findAll(AssignmentRotationFilter filter) {
        Predicate exp = toPredicate(filter);
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    private Predicate toPredicate(AssignmentRotationFilter filter) {
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
