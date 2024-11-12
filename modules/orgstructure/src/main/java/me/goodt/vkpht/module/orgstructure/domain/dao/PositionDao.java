package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.api.exception.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodt.drive.rtcore.constants.ComponentFieldCode;
import com.goodt.drive.rtcore.dto.rostalent.position.PositionListResponse;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.QComponentFieldEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.QTaskFieldEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.QTaskTypeFieldEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class PositionDao extends AbstractDao<PositionEntity, Long> {

    private static final QPositionEntity meta = QPositionEntity.positionEntity;
    private static final QDivisionEntity division = QDivisionEntity.divisionEntity;
    private static final QLegalEntityEntity legalEntity = QLegalEntityEntity.legalEntityEntity;

    public PositionDao(EntityManager em) {
        super(PositionEntity.class, em);
    }

    private BooleanExpression checkUnit(String unitCode) {
        return legalEntity.unitCode.eq(unitCode);
    }

    public PositionEntity getById(Long positionId) {
        return findById(positionId)
            .orElseThrow(() -> new NotFoundException(String.format("Position with id: %d is not found", positionId)));
    }

    public List<PositionEntity> getPositionByDivisionId(Long divisionId, String unitCode) {
        return query().selectFrom(meta)
                .where(meta.division.id.eq(divisionId)
                    .and(checkUnit(unitCode)))
                .innerJoin(division).on(division.id.eq(meta.divisionId))
                .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
                .fetch();
    }

    public Set<PositionEntity> getActualByIdsOrDivisionIdsOrLegalEntityId(List<Long> ids, List<Long> divisionIds, boolean hasPositionAssignment, Long legalEntityId, String unitCode) {
        final QPositionAssignmentEntity pa = QPositionAssignmentEntity.positionAssignmentEntity;

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(pa.dateTo.isNull());

        if (!CollectionUtils.isEmpty(ids)) {
            predicates.add(meta.id.in(ids));
        }

        if (!CollectionUtils.isEmpty(divisionIds)) {
            predicates.add(meta.division.id.in(divisionIds));
        }

        if (legalEntityId != null) {
            predicates.add(meta.division.legalEntityEntity.id.eq(legalEntityId));
        }

        if (hasPositionAssignment) {
            predicates.add(pa.id.isNotNull());
        } else {
            predicates.add(pa.id.isNull());
        }
        predicates.add(meta.dateTo.isNull());
        predicates.add(checkUnit(unitCode));
        return new HashSet<>(query().selectFrom(meta)
            .innerJoin(division).on(division.id.eq(meta.divisionId))
            .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
            .leftJoin(pa).on(meta.id.eq(pa.position.id))
            .where(ExpressionUtils.allOf(predicates))
            .fetch());
    }

    //    @Query(value = "SELECT p.* FROM position AS p " +
    //            "INNER JOIN position_assignment AS pa ON p.id = pa.position_id " +
    //            "WHERE p.division_id = :divisionId AND pa.employee_id = :employeeId", nativeQuery = true)
    public List<PositionEntity> getPositionByEmployeeIdAndDivisionIds(Long employeeId, List<Long> divisionIds, String unitCode) {
        final QPositionAssignmentEntity pa = QPositionAssignmentEntity.positionAssignmentEntity;
        final BooleanExpression exp = Expressions.allOf(
                employeeId == null ? null : pa.employee.id.eq(employeeId),
                CollectionUtils.isEmpty(divisionIds) ? null : meta.division.id.in(divisionIds),
                checkUnit(unitCode)
        );

        return query().selectFrom(meta)
                .innerJoin(pa).on(meta.id.eq(pa.position.id))
                .innerJoin(division).on(division.id.eq(meta.divisionId))
                .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
                .where(exp)
                .fetch();
    }

    public List<PositionSuccessorEntity> getPositionByDivisionTeamAssignments(List<Long> dtaIds, String unitCode) {
        final QPositionAssignmentEntity pa = QPositionAssignmentEntity.positionAssignmentEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QPositionSuccessorEntity ps = QPositionSuccessorEntity.positionSuccessorEntity;

        BooleanExpression exp = Expressions.allOf(
                dta.id.in(dtaIds),
                ps.dateTo.isNull(),
                checkUnit(unitCode));

        return query().select(ps).from(dta)
                .innerJoin(dtr).on(dta.divisionTeamRoleId.eq(dtr.id))
                .innerJoin(dt).on(dtr.divisionTeam.id.eq(dt.id))
                .innerJoin(pa).on(dta.employeeId.eq(pa.employeeId))
                .innerJoin(meta).on(pa.position.id.eq(meta.id).and(meta.division.id.eq(dt.divisionId)))
                .innerJoin(ps).on(meta.id.eq(ps.position.id))
                .innerJoin(division).on(division.id.eq(meta.divisionId))
                .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
                .where(exp)
                .fetch();

    }

    public JPQLQuery<?> findAllByDivisionOrManager(List<Long> divisionTeams, String unitCode) {
        JPQLQuery<Long> employees;
        {
            QDivisionTeamRoleEntity qdtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
            QDivisionTeamAssignmentEntity qdta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
            BooleanExpression exp = qdtr.divisionTeamId.in(divisionTeams);
            employees = query().from(qdtr)
                    .join(qdta).on(qdta.divisionTeamRole.eq(qdtr))
                    .where(exp)
                    .select(qdta.employeeId);
        }
        QPositionAssignmentEntity qpa = QPositionAssignmentEntity.positionAssignmentEntity;
        QDivisionEntity qd = QDivisionEntity.divisionEntity;
        QDivisionTeamShort qdt = QDivisionTeamShort.divisionTeamShort;
        QJobTitleEntity qjt = QJobTitleEntity.jobTitleEntity;
        QPositionEntity qp = QPositionEntity.positionEntity;
        BooleanExpression in = qpa.employeeId.in(employees)
                .and(qpa.dateTo.isNull())
                .and(qdt.id.in(divisionTeams))
                .and(checkUnit(unitCode));

        return query().from(qpa)
                .join(qpa.position, qp)
                .join(qp.division, qd)
                .join(qp.jobTitle, qjt)
                .join(qdt).on(qdt.divisionId.eq(qd.id))
                .innerJoin(division).on(division.id.eq(meta.divisionId))
                .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
                .where(in);
    }

    public JPQLQuery<?> findAllByDivisionTeamAndEmployee(Collection<Long> employee, Collection<Long> divisionTeams, String unitCode) {
        QPositionAssignmentEntity qpa = QPositionAssignmentEntity.positionAssignmentEntity;
        QDivisionEntity qd = QDivisionEntity.divisionEntity;
        QDivisionTeamShort qdt = QDivisionTeamShort.divisionTeamShort;
        QJobTitleEntity qjt = QJobTitleEntity.jobTitleEntity;
        QPositionEntity qp = QPositionEntity.positionEntity;
        BooleanExpression in = qpa.employeeId.in(employee)
                .and(qpa.dateTo.isNull())
                .and(qdt.id.in(divisionTeams))
                .and(checkUnit(unitCode));

        return query().from(qpa)
                .join(qpa.position, qp)
                .join(qp.division, qd)
                .join(qp.jobTitle, qjt)
                .join(qdt).on(qdt.divisionId.eq(qd.id))
                .innerJoin(division).on(division.id.eq(meta.divisionId))
                .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
                .where(in);
    }

    public List<Long> findActualIdsByEmployeeIds(Collection<Long> employeeIds, String unitCode) {
        QPositionAssignmentEntity pa = QPositionAssignmentEntity.positionAssignmentEntity;
        BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                pa.dateTo.isNull(),
                pa.employeeId.in(employeeIds),
                checkUnit(unitCode)
        );
        return query().select(meta.id)
                .from(meta)
                .join(pa).on(pa.positionId.eq(meta.id))
                .innerJoin(division).on(division.id.eq(meta.divisionId))
                .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
                .where(exp)
                .fetch();
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Map<Long, PositionEntity> findByEmployeeIds(Collection<Long> employeeIds,
                                                       Long divisionTeamId,
                                                       String unitCode) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QDivisionEntity division = QDivisionEntity.divisionEntity;
        QDivisionTeamEntity divisionTeam = QDivisionTeamEntity.divisionTeamEntity;

        BooleanExpression filter = Expressions.allOf(
            positionAssignment.employeeId.in(employeeIds),
            checkUnit(unitCode),
            divisionTeam.id.eq(divisionTeamId),
            meta.dateTo.isNull(),
            positionAssignment.dateTo.isNull(),
            division.dateTo.isNull(),
            divisionTeam.dateTo.isNull()
        );

        return query().select(positionAssignment.employeeId, meta)
            .from(meta)
            .join(positionAssignment).on(positionAssignment.position.id.eq(meta.id))
            .join(division).on(meta.division.id.eq(division.id))
            .join(divisionTeam).on(division.id.eq(divisionTeam.divisionId))
            .innerJoin(division).on(division.id.eq(meta.divisionId))
            .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
            .where(filter)
            .fetch()
            .stream()
            .collect(
                Collectors.toMap(
                    tuple -> tuple.get(positionAssignment.employeeId),
                    tuple -> tuple.get(meta)
                )
            );
    }

    public Page<PositionListResponse> positionList(Long divisionId, String positionName,
        Pageable pageable) {
        QDivisionEntity division = QDivisionEntity.divisionEntity;
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
        QPersonEntity person = QPersonEntity.personEntity;
        QTaskFieldEntity tf = QTaskFieldEntity.taskFieldEntity;
        QComponentFieldEntity cf = QComponentFieldEntity.componentFieldEntity;
        QTaskTypeFieldEntity ttf = QTaskTypeFieldEntity.taskTypeFieldEntity;
        BooleanBuilder where = new BooleanBuilder();

        where.and(Expressions.allOf(
                meta.dateTo.isNull(),
                division.dateTo.isNull(),
                positionAssignment.dateTo.isNull().or(positionAssignment.isNull()),
                employee.dateTo.isNull().or(employee.isNull()),
                person.dateTo.isNull().or(person.isNull())
        ));

        if (divisionId != null) {
            where.and(division.id.eq(divisionId));
        }
        if (positionName != null) {
            where.and(meta.shortName.like("%" + positionName + "%"));
        }

        QBean<PositionListResponse> projection = Projections.bean(PositionListResponse.class,
            meta.id.as("positionId"),
            meta.shortName.as("positionShortName"),
            meta.divisionId.as("divisionId"),
            division.shortName.as("divisionShortName"),
            employee.id.as("employeeId"),
            person.name.as("name"),
            person.surname.as("surname"),
            person.patronymic.as("patronymic"),
            tf.taskId.as("positionRelationTaskId"),
            person.photo.as("employeePhoto"));

        JPQLQuery<PositionListResponse> query = query()
            .select(projection)
            .from(meta)
            .join(division).on(division.id.eq(meta.divisionId)).fetchJoin()
            .innerJoin(division).on(division.id.eq(meta.divisionId))
            .innerJoin(legalEntity).on(division.legalEntityId.eq(legalEntity.id))
            .leftJoin(positionAssignment).on(positionAssignment.positionId.eq(meta.id))
            .leftJoin(employee).on(positionAssignment.employeeId.eq(employee.id)).fetchJoin()
            .leftJoin(person).on(person.id.eq(employee.personId)).fetchJoin()
            .leftJoin(cf).on(cf.code.eq(ComponentFieldCode.POSITION_PROFILE_POSITION_RELATION_ID))
            .leftJoin(ttf).on(ttf.componentFieldId.eq(cf.id))
            .leftJoin(tf).on(tf.value.eq(meta.id.stringValue()).and(tf.dateTo.isNull()).and(tf.taskTypeFieldId.eq(ttf.id)))
            .where(where);
        JPQLQuery<PositionListResponse> pagedQuery = new Querydsl(em,
            new PathBuilderFactory().create(PositionListResponse.class))
            .applyPagination(pageable, query);
        return new PageImpl<>(pagedQuery.fetch(), pageable, pagedQuery.fetchCount());

    }
}
