package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.domain.entity.*;

/** Предназначен для формирования запросов {@link DivisionTeamRoleEntity} с различным количеством условий. */
@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class DivisionTeamRoleDao extends AbstractDao<DivisionTeamRoleEntity, Long> {
    private static final QDivisionTeamRoleEntity divisionTeamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
    private static final QDivisionTeamSuccessorEntity divisionTeamSuccessor = QDivisionTeamSuccessorEntity.divisionTeamSuccessorEntity;
    private static final QDivisionTeamSuccessorReadinessEntity divisionTeamSuccessorReadiness = QDivisionTeamSuccessorReadinessEntity.divisionTeamSuccessorReadinessEntity;
    private static final QDivisionTeamAssignmentRotationEntity divisionTeamAssignmentRotation = QDivisionTeamAssignmentRotationEntity.divisionTeamAssignmentRotationEntity;
    private static final QDivisionTeamEntity divisionTeam = QDivisionTeamEntity.divisionTeamEntity;
    private static final QDivisionTeamAssignmentEntity divisionTeamAssignment = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;

    public DivisionTeamRoleDao(EntityManager em) {
        super(DivisionTeamRoleEntity.class, em);
    }

    /**
     * Формирует и выполняет запрос в зависимости от переданных параметров.
     * Различные параметры могут быть null, в таком случае они просто не будут учитываться в запросе.
     * inner join-ы добавляются в зависимости от не-null переметров.
     *
     * @return Список {@link DivisionTeamRoleEntity}.
     * Если ничего не найдено, возвращает пустой список, а не null.
     * Если все параметры null, вернет все записи без ограничений.
     */
    public List<DivisionTeamRoleEntity> find(Set<Long> employeeIds,
                                             List<Long> divisionTeamRoleId,
                                             List<Long> divisionTeamId,
                                             Date successorReadinessDateFromStart,
                                             Date successorReadinessDateFromEnd,
                                             Date assignmentRotationDateFromStart,
                                             Date assignmentRotationDateFromEnd,
                                             Date successorDateFrom,
                                             Boolean successorReadinessDateFromPlusYear,
                                             Long divisionId,
                                             Long employeeSuccessorId,
                                             Integer page,
                                             Integer size) {
        List<Long> ids = findIdsByCriteria(employeeIds, divisionTeamRoleId, divisionTeamId, successorReadinessDateFromStart, successorReadinessDateFromEnd, assignmentRotationDateFromStart, assignmentRotationDateFromEnd, successorDateFrom, successorReadinessDateFromPlusYear, divisionId, employeeSuccessorId, page, size);
        return getDivisionTeamRoleEntities(ids);
    }

    private List<DivisionTeamRoleEntity> getDivisionTeamRoleEntities(List<Long> ids) {
        JPQLQuery<DivisionTeamRoleEntity> main = query()
                .selectFrom(divisionTeamRole)
                .leftJoin(divisionTeamRole.divisionTeam, divisionTeam).fetchJoin()
                .leftJoin(divisionTeam.division, QDivisionEntity.divisionEntity).fetchJoin()
                .leftJoin(divisionTeam.type, QTeamTypeEntity.teamTypeEntity).fetchJoin()
                .leftJoin(divisionTeam.status, QTeamStatusEntity.teamStatusEntity).fetchJoin()
                .leftJoin(QDivisionEntity.divisionEntity.costCenter, QCostCenterEntity.costCenterEntity).fetchJoin()
                .leftJoin(QDivisionEntity.divisionEntity.status, QDivisionStatusEntity.divisionStatusEntity).fetchJoin()
                .leftJoin(QDivisionEntity.divisionEntity.structure, QStructureEntity.structureEntity).fetchJoin()
                .leftJoin(divisionTeamRole.role, QRoleEntity.roleEntity).fetchJoin()
                .leftJoin(QRoleEntity.roleEntity.systemRole, QSystemRoleEntity.systemRoleEntity).fetchJoin()
                .where(divisionTeamRole.id.in(ids));
        return main.fetch();
    }

    private List<Long> findIdsByCriteria(Set<Long> employeeIds, List<Long> divisionTeamRoleId, List<Long> divisionTeamId, Date successorReadinessDateFromStart, Date successorReadinessDateFromEnd, Date assignmentRotationDateFromStart, Date assignmentRotationDateFromEnd, Date successorDateFrom, Boolean successorReadinessDateFromPlusYear, Long divisionId, Long employeeSuccessorId, Integer page, Integer size) {
        JPQLQuery<Long> jpqlQuery = query().from(divisionTeamRole).select(divisionTeamRole.id);
        addPagination(jpqlQuery, page, size);
        addJoins(jpqlQuery,
                 employeeIds,
                 successorReadinessDateFromStart,
                 successorReadinessDateFromEnd,
                 assignmentRotationDateFromStart,
                 assignmentRotationDateFromEnd,
                 successorDateFrom,
                 successorReadinessDateFromPlusYear,
                 divisionId,
                 employeeSuccessorId);
        addGroupBy(jpqlQuery, successorReadinessDateFromStart,
                   successorReadinessDateFromEnd,
                   assignmentRotationDateFromStart,
                   assignmentRotationDateFromEnd,
                   successorDateFrom,
                   successorReadinessDateFromPlusYear,
                   employeeSuccessorId);
        return jpqlQuery
                .where(
                        prepareEmployeeIdsExpr(employeeIds),
                        prepareDivisionTeamRoleIdExpr(divisionTeamRoleId),
                        prepareDivisionTeamIdExpr(divisionTeamId),
                        prepareSuccessorReadinessDateFromStartExpr(successorReadinessDateFromStart),
                        prepareSuccessorReadinessDateFromEndExpr(successorReadinessDateFromEnd),
                        prepareAssignmentRotationDateFromStartExpr(assignmentRotationDateFromStart),
                        prepareAssignmentRotationDateFromEndExpr(assignmentRotationDateFromEnd),
                        prepareSuccessorDateFromExpr(successorDateFrom),
                        prepareSuccessorReadinessDateFromPlusYearExpr(successorReadinessDateFromPlusYear),
                        prepareDivisionIdExpr(divisionId),
                        prepareEmployeeSuccessorIdExpr(employeeSuccessorId))
                .distinct()
                .fetch();
    }

    /**
     * Дополнительный конструктор для более удобного вызова конструктора
     * {@link #find(Set, List, List, Date, Date, Date, Date, Date, Boolean, Long, Long, Integer, Integer)}
     */
    public List<DivisionTeamRoleEntity> find(FindQueryBuilder builder) {
        return find(builder.employeeIds,
                builder.divisionTeamRoleId,
                builder.divisionTeamId,
                builder.successorReadinessDateFromStart,
                builder.successorReadinessDateFromEnd,
                builder.assignmentRotationDateFromStart,
                builder.assignmentRotationDateFromEnd,
                builder.successorDateFrom,
                builder.successorReadinessDateFromPlusYear,
                builder.divisionId,
                builder.employeeSuccessorId,
                builder.page,
                builder.size);
    }

    @Override
    public Optional<DivisionTeamRoleEntity> findById(Long id) {
        return Optional.ofNullable(query().selectFrom(divisionTeamRole)
                                           .where(divisionTeamRole.id.eq(id))
                                           .fetchFirst());
    }

    @Override
    public List<DivisionTeamRoleEntity> findAll() {
        return query().selectFrom(divisionTeamRole)
                .fetch();
    }

    public List<DivisionTeamRoleEntity> findAllByDivisionTeamsAndSystemRole(List<Long> divisionTeamsIds, Integer systemRole) {
        return query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeam).on(divisionTeam.id.eq(divisionTeamRole.divisionTeamId))
                .where(divisionTeamRole.role.systemRole.id.eq(systemRole).and(divisionTeam.id.in(divisionTeamsIds)))
                .fetch();
    }

    public List<DivisionTeamRoleEntity> findAllByDivisionTeam(Long divisionTeamId) {
        return query().selectFrom(divisionTeamRole)
                .where(divisionTeamRole.divisionTeamId.eq(divisionTeamId))
                .fetch();
    }

    public Page<DivisionTeamRoleEntity> findByIds(List<Long> ids, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .where(divisionTeamRole.id.in(ids));
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllByDivisionTeams(List<Long> divisionTeamIds, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .where(divisionTeamRole.divisionTeamId.in(divisionTeamIds));
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllByIdsAndDivisionTeams(List<Long> ids, List<Long> divisionTeamIds, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .where(divisionTeamRole.id.in(ids).and(divisionTeamRole.divisionTeamId.in(divisionTeamIds)));
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public DivisionTeamRoleEntity findByEmployeeIdAndDivisionTeamId(Long employeeId, Long divisionTeamId) {
        final BooleanExpression exp = Expressions.allOf(
                divisionTeamAssignment.dateTo.isNull(),
                divisionTeamAssignment.employeeId.eq(employeeId),
                divisionTeamRole.divisionTeamId.eq(divisionTeamId)
        );

        return query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamAssignment).on(divisionTeamAssignment.divisionTeamRoleId.eq(divisionTeamRole.id))
                .where(exp)
                .fetchFirst();
    }

    public Page<DivisionTeamRoleEntity> findAllBySuccessorReadinessDateFromStart(Date dateFromStart, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamSuccessor).on(divisionTeamSuccessor.divisionTeamRole.id.eq(divisionTeamRole.id).and(divisionTeamSuccessor.dateTo.isNull()))
                .innerJoin(divisionTeamSuccessorReadiness).on(divisionTeamSuccessorReadiness.divisionTeamSuccessor.id.eq(divisionTeamSuccessor.id).and(divisionTeamSuccessorReadiness.dateTo.isNull()))
                .where(Expressions.dateTimeOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamSuccessorReadiness.dateFrom).after(dateFromStart))
                .groupBy(divisionTeamRole.id);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllBySuccessorReadinessDateFromEnd(Date dateFromEnd, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamSuccessor).on(divisionTeamSuccessor.divisionTeamRole.id.eq(divisionTeamRole.id).and(divisionTeamSuccessor.dateTo.isNull()))
                .innerJoin(divisionTeamSuccessorReadiness).on(divisionTeamSuccessorReadiness.divisionTeamSuccessor.id.eq(divisionTeamSuccessor.id).and(divisionTeamSuccessorReadiness.dateTo.isNull()))
                .where(Expressions.dateTimeOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamSuccessorReadiness.dateFrom).before(dateFromEnd).or(Expressions.dateTimeOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamSuccessorReadiness.dateFrom).eq(dateFromEnd)))
                .groupBy(divisionTeamRole.id);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllByAssignmentRotationDateFromStart(Date dateFromStart, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamAssignment).on(divisionTeamAssignment.divisionTeamRole.id.eq(divisionTeamRole.id).and(divisionTeamAssignment.dateTo.isNull()))
                .innerJoin(divisionTeamAssignmentRotation).on(divisionTeamAssignmentRotation.assignmentId.eq(divisionTeamAssignment.id).and(divisionTeamAssignmentRotation.dateTo.isNull()))
                .where(Expressions.dateTimeOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamAssignmentRotation.dateFrom).after(dateFromStart))
                .groupBy(divisionTeamRole.id);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllByAssignmentRotationDateFromEnd(Date dateFromEnd, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamAssignment).on(divisionTeamAssignment.divisionTeamRole.id.eq(divisionTeamRole.id).and(divisionTeamAssignment.dateTo.isNull()))
                .innerJoin(divisionTeamAssignmentRotation).on(divisionTeamAssignmentRotation.assignmentId.eq(divisionTeamAssignment.id).and(divisionTeamAssignmentRotation.dateTo.isNull()))
                .where(Expressions.dateTimeOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamAssignmentRotation.dateFrom).before(dateFromEnd))
                .groupBy(divisionTeamRole.id);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllBySuccessorDateFrom(Date dateFrom, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().from(divisionTeamRole)
                .innerJoin(divisionTeamSuccessor).on(divisionTeamSuccessor.divisionTeamRole.id.eq(divisionTeamRole.id).and(divisionTeamSuccessor.dateTo.isNull()))
                .where(Expressions.dateTimeOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamSuccessor.dateFrom).eq(dateFrom))
                .groupBy(divisionTeamRole.id)
                .select(divisionTeamRole);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllByDivision(Long divisionId, Pageable pageable) {
        final QDivisionEntity division = QDivisionEntity.divisionEntity;

        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeam).on(divisionTeamRole.divisionTeamId.eq(divisionTeam.id))
                .innerJoin(division).on(divisionTeam.divisionId.eq(division.id))
                .where(divisionTeam.divisionId.eq(divisionId));
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findByEmployeeIds(Set<Long> employeeId, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamAssignment).on(divisionTeamAssignment.divisionTeamRoleId.eq(divisionTeamRole.id))
                .where(divisionTeamAssignment.employeeId.in(employeeId));
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllByEmployeeSuccessorId(Long employeeSuccessorId, Pageable pageable) {
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamSuccessor).on(divisionTeamSuccessor.divisionTeamRole.id.eq(divisionTeamRole.id))
                .where(divisionTeamSuccessor.employee.id.eq(employeeSuccessorId))
                .groupBy(divisionTeamRole.id);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Page<DivisionTeamRoleEntity> findAllBySuccessorReadinessDateFromPlusOneYear(Pageable pageable) {
        final Date dateMinusYear = Date.from(LocalDate.now().atStartOfDay().minusYears(1).atZone(ZoneId.systemDefault()).toInstant());
        JPQLQuery<DivisionTeamRoleEntity> select = query().selectFrom(divisionTeamRole)
                .innerJoin(divisionTeamSuccessor).on(divisionTeamSuccessor.divisionTeamRole.id.eq(divisionTeamRole.id).and(divisionTeamSuccessor.dateTo.isNull()))
                .innerJoin(divisionTeamSuccessorReadiness).on(divisionTeamSuccessorReadiness.divisionTeamSuccessor.id.eq(divisionTeamSuccessor.id).and(divisionTeamSuccessor.dateTo.isNull()))
                .where(Expressions.dateOperation(
                        Date.class, Ops.DateTimeOps.DATE,
                        divisionTeamSuccessorReadiness.dateFrom).eq(dateMinusYear))
                .groupBy(divisionTeamRole.id);
        JPQLQuery<DivisionTeamRoleEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamRoleEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamRoleEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    /** Добавляет паджинацию, если нужно исходя из параметров */
    private void addPagination(JPQLQuery<?> jpqlQuery, Integer page, Integer size) {
        if (size != null && size > 0) {
            jpqlQuery.limit(size);
        }
        if (size != null && size > 0 && page != null && page >= 0) {
            jpqlQuery.offset((long) size * page);
        }
    }

    /** В зависимости от параметров, добавляет нужные join-ы */
    private void addJoins(JPQLQuery<?> jpqlQuery,
                          Set<Long> employeeIds,
                          Date successorReadinessDateFromStart,
                          Date successorReadinessDateFromEnd,
                          Date assignmentRotationDateFromStart,
                          Date assignmentRotationDateFromEnd,
                          Date successorDateFrom,
                          Boolean successorReadinessDateFromPlusYear,
                          Long divisionId,
                          Long employeeSuccessorId) {
        if (successorReadinessDateFromStart != null
                || successorReadinessDateFromEnd != null
                || successorDateFrom != null
                || successorReadinessDateFromPlusYear != null
                || employeeSuccessorId != null) {
            jpqlQuery
                    .innerJoin(divisionTeamSuccessor).on(divisionTeamSuccessor.divisionTeamRole.id.eq(divisionTeamRole.id));
        }
        if (successorReadinessDateFromStart != null
                || successorReadinessDateFromEnd != null
                || successorReadinessDateFromPlusYear != null) {
            jpqlQuery
                    .innerJoin(divisionTeamSuccessorReadiness).on(divisionTeamSuccessorReadiness.divisionTeamSuccessor.id.eq(divisionTeamSuccessor.id));
        }
        if (assignmentRotationDateFromStart != null || assignmentRotationDateFromEnd != null || employeeIds != null) {
            jpqlQuery
                    .innerJoin(divisionTeamAssignment).on(divisionTeamAssignment.divisionTeamRoleId.eq(divisionTeamRole.id));
        }
        if (assignmentRotationDateFromStart != null || assignmentRotationDateFromEnd != null) {
            jpqlQuery
                    .innerJoin(divisionTeamAssignmentRotation).on(divisionTeamAssignmentRotation.assignmentId.eq(divisionTeamAssignment.id));
        }
        if (divisionId != null) {
            jpqlQuery
                    .innerJoin(divisionTeam).on(divisionTeam.id.eq(divisionTeamRole.divisionTeamId))
                    .innerJoin(QDivisionEntity.divisionEntity).on(QDivisionEntity.divisionEntity.id.eq(divisionTeam.divisionId));
        }
    }

    /** Добавляет группирвоку, если нужно исходя из параметров */
    private void addGroupBy(JPQLQuery<?> jpqlQuery,
                            Date successorReadinessDateFromStart,
                            Date successorReadinessDateFromEnd,
                            Date assignmentRotationDateFromStart,
                            Date assignmentRotationDateFromEnd,
                            Date successorDateFrom,
                            Boolean successorReadinessDateFromPlusYear,
                            Long employeeSuccessorId) {
        if (successorReadinessDateFromStart != null
                || successorReadinessDateFromEnd != null
                || assignmentRotationDateFromStart != null
                || assignmentRotationDateFromEnd != null
                || successorDateFrom != null
                || successorReadinessDateFromPlusYear != null
                || employeeSuccessorId != null) {
            jpqlQuery.groupBy(divisionTeamRole.id);
        }
    }

    public JPQLQuery<?> findAllByIds(List<Long> dtrIds) {
        final QDivisionTeamRoleShort qdtrs = QDivisionTeamRoleShort.divisionTeamRoleShort;
        final QDivisionTeamShort qdts = QDivisionTeamShort.divisionTeamShort;
        return query().from(qdtrs)
                .join(qdts).on(qdtrs.divisionTeamId.eq(qdts.id))
                .where(qdtrs.id.in(dtrIds));
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareEmployeeIdsExpr(Set<Long> employeeIds) {
        return CollectionUtils.isNotEmpty(employeeIds) ? divisionTeamAssignment.employeeId.in(employeeIds) : null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareDivisionTeamRoleIdExpr(List<Long> divisionTeamRoleId) {
        return CollectionUtils.isNotEmpty(divisionTeamRoleId) ? divisionTeamRole.id.in(divisionTeamRoleId) : null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareDivisionTeamIdExpr(List<Long> divisionTeamId) {
        return CollectionUtils.isNotEmpty(divisionTeamId) ? divisionTeamRole.divisionTeamId.in(divisionTeamId) : null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareSuccessorReadinessDateFromStartExpr(Date successorReadinessDateFromStart) {
        if (successorReadinessDateFromStart != null) {
            return Expressions.dateTimeOperation(
                            Date.class, Ops.DateTimeOps.DATE,
                            divisionTeamSuccessorReadiness.dateFrom).after(successorReadinessDateFromStart)
                    .and(divisionTeamSuccessor.dateTo.isNull())
                    .and(divisionTeamSuccessorReadiness.dateTo.isNull());
        }
        return null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareSuccessorReadinessDateFromEndExpr(Date successorReadinessDateFromEnd) {
        if (successorReadinessDateFromEnd != null) {
            return Expressions.dateTimeOperation(
                            Date.class, Ops.DateTimeOps.DATE,
                            divisionTeamSuccessorReadiness.dateFrom).loe(successorReadinessDateFromEnd)
                    .and(divisionTeamSuccessor.dateTo.isNull())
                    .and(divisionTeamSuccessorReadiness.dateTo.isNull());
        }
        return null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareAssignmentRotationDateFromStartExpr(Date assignmentRotationDateFromStart) {
        if (assignmentRotationDateFromStart != null) {
            return Expressions.dateTimeOperation(
                            Date.class, Ops.DateTimeOps.DATE,
                            divisionTeamAssignmentRotation.dateFrom).after(assignmentRotationDateFromStart)
                    .and(divisionTeamAssignmentRotation.dateTo.isNull())
                    .and(divisionTeamAssignment.dateTo.isNull());
        }
        return null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareAssignmentRotationDateFromEndExpr(Date assignmentRotationDateFromEnd) {
        if (assignmentRotationDateFromEnd != null) {
            return Expressions.dateTimeOperation(
                            Date.class, Ops.DateTimeOps.DATE,
                            divisionTeamAssignmentRotation.dateFrom).before(assignmentRotationDateFromEnd)
                    .and(divisionTeamAssignmentRotation.dateTo.isNull())
                    .and(divisionTeamAssignment.dateTo.isNull());
        }
        return null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareSuccessorDateFromExpr(Date successorDateFrom) {
        if (successorDateFrom != null) {
            return Expressions.dateTimeOperation(
                            Date.class, Ops.DateTimeOps.DATE,
                            divisionTeamSuccessor.dateFrom).eq(successorDateFrom)
                    .and(divisionTeamSuccessor.dateTo.isNull());
        }
        return null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareSuccessorReadinessDateFromPlusYearExpr(Boolean successorReadinessDateFromPlusYear) {
        if (successorReadinessDateFromPlusYear != null) {
            Date datePlusYear = Date.from(LocalDate.now().atStartOfDay().plusYears(1).atZone(ZoneId.systemDefault()).toInstant());
            return Expressions.dateTimeOperation(
                            Date.class, Ops.DateTimeOps.DATE,
                            divisionTeamSuccessorReadiness.dateFrom).eq(datePlusYear)
                    .and(divisionTeamSuccessor.dateTo.isNull())
                    .and(divisionTeamSuccessorReadiness.dateTo.isNull());
        }
        return null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareDivisionIdExpr(Long divisionId) {
        return divisionId != null ? divisionTeam.divisionId.eq(divisionId) : null;
    }

    /** @return Возвращает нужный предикат, если параметр валиден */
    private BooleanExpression prepareEmployeeSuccessorIdExpr(Long employeeSuccessorId) {
        return employeeSuccessorId != null ? divisionTeamSuccessor.employeeId.eq(employeeSuccessorId) : null;
    }

    /**
     * Билдер для более удобной задачи параметров в методе
     *
     * @link #find(Set, List, List, Date, Date, Date, Date, Date, Boolean, Long, Long, Integer, Integer)}
     */
    public static class FindQueryBuilder {
        private final Integer page;
        private final Integer size;
        private Set<Long> employeeIds;
        private List<Long> divisionTeamRoleId;
        private List<Long> divisionTeamId;
        private Date successorReadinessDateFromStart;
        private Date successorReadinessDateFromEnd;
        private Date assignmentRotationDateFromStart;
        private Date assignmentRotationDateFromEnd;
        private Date successorDateFrom;
        private Boolean successorReadinessDateFromPlusYear;
        private Long divisionId;
        private Long employeeSuccessorId;

        public FindQueryBuilder(Integer page, Integer size) {
            this.page = page;
            this.size = size;
        }

        public static FindQueryBuilder newInstance(Integer page, Integer size) {
            return new FindQueryBuilder(page, size);
        }

        public FindQueryBuilder employeeIds(Set<Long> employeeIds) {
            this.employeeIds = employeeIds;
            return this;
        }

        public FindQueryBuilder divisionTeamRoleId(List<Long> divisionTeamRoleId) {
            this.divisionTeamRoleId = divisionTeamRoleId;
            return this;
        }

        public FindQueryBuilder divisionTeamId(List<Long> divisionTeamId) {
            this.divisionTeamId = divisionTeamId;
            return this;
        }

        public FindQueryBuilder successorReadinessDateFromStart(Date successorReadinessDateFromStart) {
            this.successorReadinessDateFromStart = successorReadinessDateFromStart;
            return this;
        }

        public FindQueryBuilder successorReadinessDateFromEnd(Date successorReadinessDateFromEnd) {
            this.successorReadinessDateFromEnd = successorReadinessDateFromEnd;
            return this;
        }

        public FindQueryBuilder assignmentRotationDateFromStart(Date assignmentRotationDateFromStart) {
            this.assignmentRotationDateFromStart = assignmentRotationDateFromStart;
            return this;
        }

        public FindQueryBuilder assignmentRotationDateFromEnd(Date assignmentRotationDateFromEnd) {
            this.assignmentRotationDateFromEnd = assignmentRotationDateFromEnd;
            return this;
        }

        public FindQueryBuilder successorDateFrom(Date successorDateFrom) {
            this.successorDateFrom = successorDateFrom;
            return this;
        }

        public FindQueryBuilder successorReadinessDateFromPlusYear(Boolean successorReadinessDateFromPlusYear) {
            this.successorReadinessDateFromPlusYear = successorReadinessDateFromPlusYear;
            return this;
        }

        public FindQueryBuilder divisionId(Long divisionId) {
            this.divisionId = divisionId;
            return this;
        }

        public FindQueryBuilder employeeSuccessorId(Long employeeSuccessorId) {
            this.employeeSuccessorId = employeeSuccessorId;
            return this;
        }
    }

    public Long findIdByExternalId(String extId) {
        return query().select(divisionTeamRole.id)
            .from(divisionTeamRole)
            .where(divisionTeamRole.externalId.eq(extId))
            .fetchFirst();
    }
}

