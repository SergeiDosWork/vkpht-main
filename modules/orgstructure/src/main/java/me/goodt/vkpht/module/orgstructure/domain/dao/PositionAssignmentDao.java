package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
@Repository
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
public class PositionAssignmentDao extends AbstractDao<PositionAssignmentEntity, Long> {

    private static final QPositionAssignmentEntity meta = QPositionAssignmentEntity.positionAssignmentEntity;

    public PositionAssignmentDao(EntityManager em) {
        super(PositionAssignmentEntity.class, em);
    }

    public PositionAssignmentEntity getPositionAssignmentByPositionId(Long positionId) {
        BooleanExpression exp = meta.positionId.eq(positionId)
            .and(actual());
        return query().selectFrom(meta)
            .where(exp)
            .orderBy(meta.id.asc())
            .fetchFirst();
    }

    private BooleanExpression actual() {
        return meta.dateTo.isNull();
    }

    private BooleanExpression checkUnit(String unitCode) {
        final QSubstitutionTypeEntity st = QSubstitutionTypeEntity.substitutionTypeEntity;
        return st.unitCode.eq(unitCode).or(meta.substitutionType.isNull());
    }

    public Map<Long, List<PositionAssignmentEntity>> findActualByEmployeeIds(List<Long> employeeIds, String unitCode) {
        final QAssignmentCategoryEntity c = QAssignmentCategoryEntity.assignmentCategoryEntity;
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final QJobTitleEntity jt = QJobTitleEntity.jobTitleEntity;
        final QDivisionEntity d = QDivisionEntity.divisionEntity;
        final QAssignmentTypeEntity at = QAssignmentTypeEntity.assignmentTypeEntity;
        final QEmployeeEntity em = QEmployeeEntity.employeeEntity;
        final QPersonEntity p = QPersonEntity.personEntity;
        final QFamilyStatusEntity f = QFamilyStatusEntity.familyStatusEntity;
        final QSubstitutionTypeEntity st = QSubstitutionTypeEntity.substitutionTypeEntity;
        final BooleanExpression exp = actual()
            .and(meta.employeeId.in(employeeIds))
            .and(checkUnit(unitCode));
        final List<PositionAssignmentEntity> list = query()
            .selectFrom(meta)
            .leftJoin(meta.category, c).fetchJoin()
            .leftJoin(meta.position, pos).fetchJoin()
            .leftJoin(pos.jobTitle, jt).fetchJoin()
            .leftJoin(pos.division, d).fetchJoin()
            .leftJoin(meta.type, at).fetchJoin()
            .leftJoin(meta.employee, em).fetchJoin()
            .leftJoin(em.person, p).fetchJoin()
            .leftJoin(p.familyStatus, f).fetchJoin()
            .leftJoin(st).on(meta.substitutionType.id.eq(st.id))
            .where(exp)
            .distinct()
            .fetch();
        if (list.size() > 30) {
            log.info("PositionAssignmentDao.findActualByEmployeeIds count {}", list.size());
        }
        return list.stream()
            .collect(groupingBy(PositionAssignmentEntity::getEmployeeId, mapping(t -> t, toList())));
    }

    public Long findIdByExternalId(String externalId) {
        BooleanExpression exp = meta.externalId.eq(externalId);
        return query().from(meta)
            .select(meta.id)
            .where(exp)
            .fetchFirst();
    }

    public PositionAssignmentEntity find(PositionAssignmentFilter filter) {
        Predicate where = toPredicate(filter);
        return query(where).fetchFirst();
    }

    public List<PositionAssignmentEntity> findAll(PositionAssignmentFilter filter) {
        Predicate where = toPredicate(filter);
        return query(where).fetch();
    }

    public List<PositionAssignmentEntity> findAllByEmployeeId(List<Long> employeeIds) {
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final QDivisionEntity div = QDivisionEntity.divisionEntity;
        final QLegalEntityEntity leg = QLegalEntityEntity.legalEntityEntity;
        final QUnitEntity unit = QUnitEntity.unitEntity;
        return query(meta.employeeId.in(employeeIds))
            .leftJoin(meta.position, pos).fetchJoin()
            .leftJoin(pos.division, div).fetchJoin()
            .leftJoin(div.legalEntityEntity, leg).fetchJoin()
            .leftJoin(unit).on(leg.unitCode.eq(unit.code)).fetchJoin()
            .fetch();
    }

    private JPQLQuery<PositionAssignmentEntity> query(Predicate where) {
        final QAssignmentCategoryEntity c = QAssignmentCategoryEntity.assignmentCategoryEntity;
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final QJobTitleEntity jt = QJobTitleEntity.jobTitleEntity;
        final QDivisionEntity d = QDivisionEntity.divisionEntity;
        final QAssignmentTypeEntity at = QAssignmentTypeEntity.assignmentTypeEntity;
        final QEmployeeEntity em = QEmployeeEntity.employeeEntity;
        final QPersonEntity p = QPersonEntity.personEntity;
        final QFamilyStatusEntity f = QFamilyStatusEntity.familyStatusEntity;
        final QSubstitutionTypeEntity st = QSubstitutionTypeEntity.substitutionTypeEntity;
        return query().selectFrom(meta)
            .leftJoin(meta.category, c).fetchJoin()
            .leftJoin(meta.position, pos).fetchJoin()
            .leftJoin(pos.jobTitle, jt).fetchJoin()
            .leftJoin(pos.division, d).fetchJoin()
            .leftJoin(meta.type, at).fetchJoin()
            .leftJoin(meta.employee, em).fetchJoin()
            .leftJoin(em.person, p).fetchJoin()
            .leftJoin(p.familyStatus, f).fetchJoin()
            .leftJoin(meta.substitutionType, st).fetchJoin()
            .where(where);
    }

    private Predicate toPredicate(PositionAssignmentFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        QDivisionEntity qdv = QDivisionEntity.divisionEntity;
        if (filter.getUnitCode() != null) {
            where.and(checkUnit(filter.getUnitCode()));
        }
        if (filter.getPositions() != null && !filter.getPositions().isEmpty()) {
            where.and(meta.positionId.in(filter.getPositions()));
        }
        if (filter.getEmployeeId() != null) {
            where.and(meta.employeeId.eq(filter.getEmployeeId()));
        }
        if (filter.getPositionId() != null) {
            where.and(meta.positionId.eq(filter.getPositionId()));
        }
        if (filter.getLegalEntityId() != null) {
            where.and(qdv.legalEntityId.eq(filter.getLegalEntityId()));
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

    public List<PositionAssignmentEntity> findActual(Long id, String externalId, String unitCode) {
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final QEmployeeEntity em = QEmployeeEntity.employeeEntity;
        final QPersonEntity p = QPersonEntity.personEntity;
        final QSubstitutionTypeEntity st = QSubstitutionTypeEntity.substitutionTypeEntity;

        BooleanExpression exp = Expressions.allOf(
            id == null ? null : meta.employee.id.eq(id),
            externalId == null ? null : meta.employee.externalId.eq(externalId),
            meta.employee.dateTo.isNull(),
            meta.dateTo.isNull(),
            checkUnit(unitCode)
        );

        return query()
            .selectFrom(meta)
            .leftJoin(meta.position, pos).fetchJoin()
            .leftJoin(meta.employee, em).fetchJoin()
            .leftJoin(em.person, p).fetchJoin()
            .leftJoin(meta.substitutionType, st).fetchJoin()
            .where(exp)
            .fetch();
    }

    public PositionAssignmentEntity findByEmployeeId(Long employeeId) {
        final QAssignmentCategoryEntity c = QAssignmentCategoryEntity.assignmentCategoryEntity;
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final QJobTitleEntity jt = QJobTitleEntity.jobTitleEntity;
        final QDivisionEntity d = QDivisionEntity.divisionEntity;
        final QAssignmentTypeEntity at = QAssignmentTypeEntity.assignmentTypeEntity;
        final QEmployeeEntity em = QEmployeeEntity.employeeEntity;
        final QPersonEntity p = QPersonEntity.personEntity;
        final QFamilyStatusEntity f = QFamilyStatusEntity.familyStatusEntity;
        final BooleanExpression exp = actual()
            .and(meta.employeeId.eq(employeeId));
        final PositionAssignmentEntity rsl = query()
            .selectFrom(meta)
            .leftJoin(meta.category, c).fetchJoin()
            .leftJoin(meta.position, pos).fetchJoin()
            .leftJoin(pos.jobTitle, jt).fetchJoin()
            .leftJoin(pos.division, d).fetchJoin()
            .leftJoin(meta.type, at).fetchJoin()
            .leftJoin(meta.employee, em).fetchJoin()
            .leftJoin(em.person, p).fetchJoin()
            .leftJoin(p.familyStatus, f).fetchJoin()
            .where(exp)
            .fetchFirst();
        return rsl;
    }
}
