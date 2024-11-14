package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.domain.dao.filter.JobTitleFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QJobTitleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.projection.EmployeeJobTitleProjection;

@Repository
public class JobTitleDao extends AbstractDao<JobTitleEntity, Long> {

    private static final QJobTitleEntity meta = QJobTitleEntity.jobTitleEntity;

    public JobTitleDao(EntityManager em) {
        super(JobTitleEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public List<JobTitleEntity> find(JobTitleFilter filter) {
        final Predicate exp = toPredicate(filter);
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public List<EmployeeJobTitleProjection> findUsingEmployeeIds(Collection<Long> employeeIds, String unitCode) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;

        var projection = Projections.bean(
            EmployeeJobTitleProjection.class,
            positionAssignment.employeeId,
            meta.shortName.as("jobTitle")
        );

        BooleanExpression filter = Expressions.allOf(
            positionAssignment.dateTo.isNull(),
            position.dateTo.isNull(),
            meta.dateTo.isNull(),
            positionAssignment.employeeId.in(employeeIds),
            meta.unitCode.eq(unitCode)
        );

        return query().select(projection)
            .from(meta)
            .where(filter)
            .join(position).on(position.jobTitleId.eq(meta.id))
            .join(positionAssignment).on(positionAssignment.positionId.eq(position.id))
            .fetch();
    }

    public Page<JobTitleEntity> find(JobTitleFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        return findAll(where, pageable);
    }

    private Predicate toPredicate(JobTitleFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getIds() != null && !filter.getIds().isEmpty()) {
            where.and(meta.id.in(filter.getIds()));
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
