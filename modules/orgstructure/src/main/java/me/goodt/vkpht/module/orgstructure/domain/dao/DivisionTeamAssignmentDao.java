package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.CompactAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionAssignmentRoleDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeTeamInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.DivisionTeamAssignmentCompactProjection;
import me.goodt.vkpht.module.orgstructure.domain.entity.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class DivisionTeamAssignmentDao extends AbstractDao<DivisionTeamAssignmentEntity, Long> {

    private static final QDivisionTeamAssignmentEntity meta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;

    public DivisionTeamAssignmentDao(EntityManager em) {
        super(DivisionTeamAssignmentEntity.class, em);
    }

    public Page<DivisionTeamAssignmentEntity> findAllByParams(List<Long> legalEntityIds, List<Long> divisionIds, Pageable pageable) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        BooleanExpression exp = meta.dateTo.isNull()
                .and(dt.dateTo.isNull())
                .and(divisionIds != null && !divisionIds.isEmpty() ? dt.divisionId.in(divisionIds) : null)
                .and(legalEntityIds != null && !legalEntityIds.isEmpty() ? dt.division.legalEntityId.in(legalEntityIds) : null);
        JPQLQuery<DivisionTeamAssignmentEntity> select = query().selectFrom(meta)
                .join(dtr).on(meta.divisionTeamRoleId.eq(dtr.id))
                .join(dt).on(dtr.divisionTeam.eq(dt))
                .where(exp);
        JPQLQuery<DivisionTeamAssignmentEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(DivisionTeamAssignmentEntity.class))
                .applyPagination(pageable, select);
        List<DivisionTeamAssignmentEntity> content = pagedQuery.fetch();
        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public List<DivisionTeamAssignmentEntity> findAllByParams(List<Long> legalEntityIds, List<Long> divisionIds) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                divisionIds != null && !divisionIds.isEmpty() ? dt.divisionId.in(divisionIds) : null,
                legalEntityIds != null && !legalEntityIds.isEmpty() ? dt.division.legalEntityId.in(legalEntityIds) : null
        );
        return query().selectFrom(meta)
                .join(dtr).on(meta.divisionTeamRoleId.eq(dtr.id))
                .join(dt).on(dtr.divisionTeam.id.eq(dt.id))
                .where(exp)
                .fetch();
    }

    public List<Long> findAllIdsByParams(List<Long> legalEntityIds, List<Long> divisionIds) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                divisionIds != null && !divisionIds.isEmpty() ? dt.divisionId.in(divisionIds) : null,
                legalEntityIds != null && !legalEntityIds.isEmpty() ? dt.division.legalEntityId.in(legalEntityIds) : null
        );
        return query().select(meta.id)
                .from(meta)
                .join(dtr).on(meta.divisionTeamRoleId.eq(dtr.id))
                .join(dt).on(dtr.divisionTeam.id.eq(dt.id))
                .where(exp)
                .orderBy(meta.id.asc())
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> getDivisionTeamAssignments(List<Long> ids, List<Long> employeeIds, Long divisionTeamId, Boolean withClosed) {
        final BooleanExpression exp = Expressions.allOf(
                ids != null && !ids.isEmpty() ? meta.id.in(ids) : null,
                employeeIds != null && !employeeIds.isEmpty() ? meta.employeeId.in(employeeIds) : null,
                divisionTeamId != null ? QDivisionTeamRoleEntity.divisionTeamRoleEntity.divisionTeamId.eq(divisionTeamId) : null,
                withClosed != null && withClosed ? null : meta.dateTo.isNull()
        );

        StopWatch sw = StopWatch.createStarted();
        JPQLQuery<DivisionTeamAssignmentEntity> query = query().selectFrom(meta);
        addBaseFetch(query);
        List<DivisionTeamAssignmentEntity> rez = query.where(exp).fetch();
        sw.stop();
        log.info("dao getDivisionTeamAssignments fetch {}, count {}", sw.getTime(), rez.size());
        return rez;
    }

    public List<DivisionTeamAssignmentCompactProjection> getDivisionTeamAssignmentCompactList(Collection<Long> ids, Collection<Long> employeeIds) {
        QEmployeeEntity emp = QEmployeeEntity.employeeEntity;
        QPersonEntity person = QPersonEntity.personEntity;
        QDivisionTeamRoleEntity teamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamEntity team = QDivisionTeamEntity.divisionTeamEntity;
        QDivisionEntity div = QDivisionEntity.divisionEntity;
        QRoleEntity role = QRoleEntity.roleEntity;

        BooleanExpression exp = Expressions.allOf(
                CollectionUtils.isEmpty(ids) ? null : meta.id.in(ids),
                CollectionUtils.isEmpty(employeeIds) ? null : meta.employeeId.in(employeeIds),
                meta.dateTo.isNull(),
                emp.dateTo.isNull(),
                team.dateTo.isNull(),
                div.dateTo.isNull()
        );

        QBean<DivisionTeamAssignmentCompactProjection> proj = Projections.bean(
            DivisionTeamAssignmentCompactProjection.class,
            meta.id.as("id"),
            meta.employeeId.as("employeeId"),
            person.surname.as("personLastName"),
            person.name.as("personFirstName"),
            person.patronymic.as("personMiddleName"),
            teamRole.divisionTeamId.as("divisionTeamId"),
            team.divisionId.as("divisionId"),
            div.legalEntityId.as("legalEntityId"),
            teamRole.roleId.as("roleId"),
            role.systemRoleId.as("systemRoleId")
        );

        StopWatch sw = StopWatch.createStarted();

        List<DivisionTeamAssignmentCompactProjection> result = query()
                .select(proj)
                .from(meta)
                .join(emp).on(emp.id.eq(meta.employeeId))
                .join(person).on(person.id.eq(emp.personId))
                .join(teamRole).on(teamRole.id.eq(meta.divisionTeamRoleId))
                .join(team).on(team.id.eq(teamRole.divisionTeamId))
                .join(div).on(div.id.eq(team.divisionId))
                .join(role).on(role.id.eq(teamRole.roleId))
                .where(exp)
                .orderBy(role.systemRoleId.asc())
                .fetch();

        sw.stop();
        log.info("dao getDivisionTeamAssignmentCompactList fetch {}, count {}", sw.getTime(), result.size());
        return result;
    }

    private void addBaseFetch(JPQLQuery<DivisionTeamAssignmentEntity> query) {
        query.leftJoin(meta.employee, QEmployeeEntity.employeeEntity).fetchJoin()
            .leftJoin(QEmployeeEntity.employeeEntity.person, QPersonEntity.personEntity).fetchJoin()
            .leftJoin(QPersonEntity.personEntity.familyStatus, QFamilyStatusEntity.familyStatusEntity).fetchJoin()
            .leftJoin(meta.type, QAssignmentTypeEntity.assignmentTypeEntity).fetchJoin()
            .leftJoin(meta.status, QAssignmentStatusEntity.assignmentStatusEntity).fetchJoin()
            .leftJoin(meta.divisionTeamRole, QDivisionTeamRoleEntity.divisionTeamRoleEntity).fetchJoin()
            .leftJoin(QDivisionTeamRoleEntity.divisionTeamRoleEntity.divisionTeam, QDivisionTeamEntity.divisionTeamEntity).fetchJoin()
            .leftJoin(QDivisionTeamEntity.divisionTeamEntity.division, QDivisionEntity.divisionEntity).fetchJoin()
            .leftJoin(QDivisionEntity.divisionEntity.legalEntityEntity, QLegalEntityEntity.legalEntityEntity).fetchJoin()
                .leftJoin(QDivisionTeamEntity.divisionTeamEntity.type, QTeamTypeEntity.teamTypeEntity).fetchJoin()
                .leftJoin(QDivisionTeamEntity.divisionTeamEntity.division, QDivisionEntity.divisionEntity).fetchJoin()
                .leftJoin(QDivisionTeamRoleEntity.divisionTeamRoleEntity.positionImportance, QPositionImportanceEntity.positionImportanceEntity).fetchJoin()
                .leftJoin(QDivisionTeamRoleEntity.divisionTeamRoleEntity.role, QRoleEntity.roleEntity).fetchJoin()
                .leftJoin(QRoleEntity.roleEntity.systemRole, QSystemRoleEntity.systemRoleEntity).fetchJoin();
    }

    public DivisionTeamAssignmentEntity getDivisionTeamAssignmentFirstByRole(Long employeeId) {
        JPQLQuery<DivisionTeamAssignmentEntity> query = query().selectFrom(meta);
        addBaseFetch(query);

        return query.where(meta.employeeId.eq(employeeId))
                .orderBy(QSystemRoleEntity.systemRoleEntity.id.asc())
                .fetchFirst();
    }

    public CompactAssignmentDto findFirstAssignmentByExtEmployeeId(String extEmployeeId) {
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QRoleEntity r = QRoleEntity.roleEntity;
        final QSystemRoleEntity sr = QSystemRoleEntity.systemRoleEntity;
        final QEmployeeEntity qem = QEmployeeEntity.employeeEntity;
        final QDivisionTeamEntity qdt = QDivisionTeamEntity.divisionTeamEntity;
        QBean<CompactAssignmentDto> proj = Projections.bean(CompactAssignmentDto.class,
                                                            meta.employeeId.as("employeeId"), qdt.divisionId.as("divisionId"), meta.id.as("id"), sr.id.as("systemRoleId"));
        final BooleanExpression exp = meta.dateTo.isNull().
                and(qem.externalId.eq(extEmployeeId));
        return query().from(meta)
                .join(dtr).on(meta.divisionTeamRoleId.eq(dtr.id))
                .join(dtr.divisionTeam, qdt)
                .join(dtr.role, r)
                .join(r.systemRole, sr)
                .join(meta.employee, qem)
                .where(exp)
                .orderBy(sr.id.asc())
                .select(proj)
                .fetchFirst();
    }

    public Map<Long, List<DivisionTeamAssignmentEntity>> findHeadByDivisionIds(List<Long> divisionIds, Integer isHead, Integer systemRoleId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        BooleanExpression exp = meta.dateTo.isNull()
                .and(dtr.divisionTeam.dateTo.isNull())
                .and(dtr.divisionTeam.divisionId.in(divisionIds))
                .and(dtr.role.systemRole.id.eq(systemRoleId));

        if (isHead != null) {
            exp = exp.and(dtr.divisionTeam.isHead.eq(isHead));
        }

        return query()
                .from(meta)
                .select(dtr.divisionTeam.divisionId, meta)
                .where(exp)
                .fetch()
                .stream()
                .collect(groupingBy(t -> t.get(dtr.divisionTeam.divisionId), mapping(t -> t.get(meta), toList())));
    }

    public DivisionTeamAssignmentEntity findByEmployeeIdAnDivisionTeamId(Long employeeId, Long divisionTeamId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.employeeId.eq(employeeId),
                dtr.divisionTeamId.eq(divisionTeamId)
        );
        return query().selectFrom(meta)
                .where(exp)
                .fetchOne();
    }

    public JPQLQuery<?> findQueryEmployeeIdAnDivisionTeamId(Long employeeId, Long divTeamId) {
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QRoleEntity r = QRoleEntity.roleEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.employeeId.eq(employeeId),
                dtr.divisionTeamId.eq(divTeamId)
        );

        return query().from(meta)
                .innerJoin(meta.divisionTeamRole, dtr)
                .innerJoin(dtr.divisionTeam, dt)
                .innerJoin(dtr.role, r)
                .where(exp);
    }

    public List<DivisionAssignmentRoleDto> findByDivisionTeamParentId(Long divisionTeamParentId) {
        QDivisionTeamAssignmentShort qdtas = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QRoleEntity qrl = QRoleEntity.roleEntity;
        final BooleanExpression exp = Expressions.allOf(
                qdtas.dateTo.isNull(),
                dt.dateTo.isNull(),
                dt.parentId.eq(divisionTeamParentId)
        );
        final QBean<DivisionAssignmentRoleDto> proj = Projections.bean(DivisionAssignmentRoleDto.class,
                                                                       dt.id.as("divisionTeamId"), qrl.systemRoleId.as("systemRoleId"), qdtas.as("assignment"));
        return query().from(qdtas)
                .innerJoin(dtr).on(dtr.id.eq(qdtas.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .leftJoin(dtr.role, qrl)
                .where(exp)
                .select(proj)
                .fetch();
    }

    public JPQLQuery<?> findQueryEmployeeIdAndSystemRole(Long employeeId, Integer roleId) {
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QRoleEntity r = QRoleEntity.roleEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                meta.employeeId.eq(employeeId),
                r.systemRole.id.eq(roleId)
        );

        return query().from(meta)
                .innerJoin(meta.divisionTeamRole, dtr)
                .innerJoin(dtr.divisionTeam, dt)
                .innerJoin(dtr.role, r)
                .where(exp);
    }

    public JPQLQuery<?> findQueryEmployeeIdsAndSystemRole(List<Long> employeeIds, Integer roleId) {
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QRoleEntity r = QRoleEntity.roleEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                meta.employeeId.in(employeeIds),
                r.systemRole.id.eq(roleId)
        );

        return query().from(meta)
                .innerJoin(meta.divisionTeamRole, dtr)
                .innerJoin(dtr.divisionTeam, dt)
                .innerJoin(dtr.role, r)
                .where(exp);
    }

    public List<DivisionTeamAssignmentEntity> fetchByDivisionTeamIdAndSystemRoleId(Long divisionTeamId, Integer systemRoleId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QRoleEntity r = new QRoleEntity("role");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dtr.divisionTeam.id.eq(divisionTeamId),
                r.systemRole.id.eq(systemRoleId)
        );
        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(r).on(dtr.role.id.eq(r.id))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentShort> fetchShortByDivTeamAndSysRole(List<Long> divisionTeamIds, Integer systemRoleId) {
        final QDivisionTeamAssignmentShort qdtas = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QRoleEntity r = new QRoleEntity("role");
        final BooleanExpression exp = Expressions.allOf(
                qdtas.dateTo.isNull(),
                dtr.divisionTeam.id.in(divisionTeamIds),
                r.systemRole.id.eq(systemRoleId));

        return query().selectFrom(qdtas)
                .innerJoin(dtr).on(dtr.id.eq(qdtas.divisionTeamRoleId))
                .innerJoin(r).on(dtr.role.id.eq(r.id))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> fetchByNotSystemRoleId(Long divisionTeamId, Integer systemRoleId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QRoleEntity r = new QRoleEntity("role");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dtr.divisionTeam.id.eq(divisionTeamId),
                r.systemRole.id.notIn(systemRoleId)
        );
        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(r).on(dtr.role.id.eq(r.id))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentShort> fetchShortByNotSystemRoleId(List<Long> divisionTeamIds, Integer systemRoleId) {
        final QDivisionTeamAssignmentShort qdtas = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QRoleEntity r = new QRoleEntity("role");
        final BooleanExpression exp = Expressions.allOf(
                qdtas.dateTo.isNull(),
                dtr.divisionTeam.id.in(divisionTeamIds),
                r.systemRole.id.notIn(systemRoleId));

        return query().selectFrom(qdtas)
                .innerJoin(dtr).on(dtr.id.eq(qdtas.divisionTeamRoleId))
                .innerJoin(r).on(dtr.role.id.eq(r.id))
                .where(exp)
                .fetch();
    }

    public List<DivisionAssignmentRoleDto> findShortByParentDivisionTeams(List<Long> parentDivTeams) {
        final QDivisionTeamAssignmentShort qdta = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QRoleEntity qrl = QRoleEntity.roleEntity;
        final BooleanExpression exp = Expressions.allOf(
                qdta.dateTo.isNull(),
                dt.dateTo.isNull(),
                dt.parentId.in(parentDivTeams));
        final QBean<DivisionAssignmentRoleDto> proj = Projections.bean(DivisionAssignmentRoleDto.class,
                                                                       dt.id.as("divisionTeamId"), qrl.systemRoleId.as("systemRoleId"), qdta.as("assignment"));
        return query().from(qdta)
                .innerJoin(dtr).on(dtr.id.eq(qdta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .leftJoin(dtr.role, qrl)
                .where(exp)
                .select(proj)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> getAssignmentByPositionCategory(Long positionCategoryId) {
        final QEmployeeEntity emp = QEmployeeEntity.employeeEntity;
        final QPositionAssignmentEntity posAss = QPositionAssignmentEntity.positionAssignmentEntity;
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                emp.dateTo.isNull(),
                posAss.dateTo.isNull(),
                pos.dateTo.isNull(),
                pos.category.id.eq(positionCategoryId)
        );
        return query().selectFrom(meta)
                .innerJoin(emp).on(emp.id.eq(meta.employeeId))
                .innerJoin(posAss).on(posAss.employeeId.eq(emp.id))
                .innerJoin(pos).on(pos.id.eq(posAss.position.id))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> findByEmployeeId(Long employeeId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                meta.employeeId.eq(employeeId)
        );

        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .where(exp)
                .fetch();
    }

    public Set<Long> getDivisionIdsByEmployeeIdAndSystemRoleId(Long employeeId, Integer systemRoleId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final QRoleEntity r = new QRoleEntity("role");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                meta.employeeId.eq(employeeId),
                r.systemRoleId.eq(systemRoleId)
        );

        return new HashSet<>(query().from(meta)
                                     .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                                     .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                                     .innerJoin(r).on(r.id.eq(dtr.role.id))
                                     .where(exp)
                                     .select(dt.divisionId)
                                     .fetch());
    }

    public List<DivisionTeamAssignmentEntity> findByDivisionTeamId(Long divisionTeamId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("division_team_role");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dtr.divisionTeamId.eq(divisionTeamId)
        );

        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> findAllByEmployeeIdAnDivisionTeamIds(Long employeeId, Collection<Long> divisionTeamIds) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.employeeId.eq(employeeId),
                dtr.divisionTeamId.in(divisionTeamIds)
        );

        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .where(exp)
                .fetch();
    }

    @Override
    public Optional<DivisionTeamAssignmentEntity> findById(Long id) {
        return Optional.ofNullable(query().selectFrom(meta)
                                           .where(meta.id.eq(id))
                                           .fetchOne());
    }

    public Optional<EmployeeTeamInfoDto> findTeamInfoByAssignmentId(Long id) {
        QEmployeeEntity em = QEmployeeEntity.employeeEntity;
        QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;

        QBean<EmployeeTeamInfoDto> projection = Projections.bean(EmployeeTeamInfoDto.class, meta.employeeId.as("employeeId"), dtr.divisionTeamId.as("teamId"));
        return Optional.ofNullable(query()
                                           .from(meta)
                                           .innerJoin(em).on(em.id.eq(meta.employeeId))
                                           .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                                           .where(meta.id.eq(id))
                                           .select(projection)
                                           .fetchOne());
    }

    public EmployeeTeamInfoDto findFirstTeamInfoByExternalEmployeeId(String externalEmployeeId) {
        QEmployeeEntity em = QEmployeeEntity.employeeEntity;
        QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                em.externalId.eq(externalEmployeeId)
        );
        QBean<EmployeeTeamInfoDto> projection = Projections.bean(EmployeeTeamInfoDto.class, meta.employeeId.as("employeeId"), dtr.divisionTeamId.as("teamId"));
        return query()
                .from(meta)
                .innerJoin(em).on(em.id.eq(meta.employeeId))
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .where(exp)
                .select(projection)
                .fetchFirst();
    }

    @Override
    public List<DivisionTeamAssignmentEntity> findAll() {
        return query().selectFrom(meta)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> getHeadDivisionTeamAssignmentByDivisionId(Long divisionId, Integer isHead, Integer systemRoleId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final QRoleEntity r = new QRoleEntity("role");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                dt.divisionId.eq(divisionId),
                dt.isHead.eq(isHead),
                r.systemRoleId.eq(systemRoleId)
        );

        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .innerJoin(r).on(r.id.eq(dtr.role.id))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> getDivisionTeamAssignmentsByLegalEntityIds(List<Long> legalEntityIds, boolean isHead, Integer systemRoleId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final QDivisionEntity d = new QDivisionEntity("division");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                d.legalEntityId.in(legalEntityIds),
                dtr.role.systemRoleId.eq(systemRoleId).or(Expressions.asBoolean(isHead).eq(false))
        );

        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .innerJoin(d).on(d.id.eq(dt.divisionId))
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> getDivisionTeamAssignmentsByDivisionTeamRoleId(Long divisionTeamRoleId) {
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.divisionTeamRoleId.eq(divisionTeamRoleId)
        );

        return query().selectFrom(meta)
                .leftJoin(meta.employee, QEmployeeEntity.employeeEntity).fetchJoin()
                .leftJoin(QEmployeeEntity.employeeEntity.person, QPersonEntity.personEntity).fetchJoin()
                .leftJoin(meta.type, QAssignmentTypeEntity.assignmentTypeEntity).fetchJoin()
                .where(exp)
                .fetch();
    }

    public Map<Long, List<DivisionTeamAssignmentEntity>> getDivisionTeamAssignmentsByDivisionTeamRoleIds(List<Long> divisionTeamRoleIds) {
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.divisionTeamRoleId.in(divisionTeamRoleIds)
        );

        return query()
                .select(meta.divisionTeamRoleId, meta)
                .from(meta)
                .leftJoin(meta.divisionTeamRole, QDivisionTeamRoleEntity.divisionTeamRoleEntity).fetchJoin()
                .leftJoin(meta.employee, QEmployeeEntity.employeeEntity).fetchJoin()
                .leftJoin(QEmployeeEntity.employeeEntity.person, QPersonEntity.personEntity).fetchJoin()
                .leftJoin(meta.type, QAssignmentTypeEntity.assignmentTypeEntity).fetchJoin()
                .where(exp)
                .stream()
                .collect(groupingBy(t -> t.get(meta.divisionTeamRoleId), mapping(t -> t.get(meta), toList())));
    }

    public List<DivisionTeamAssignmentEntity> getCurrentDivisionTeamAssignmentByDivisionTeamRoleId(Long divisionTeamRoleId) {
        final BooleanExpression exp = Expressions.allOf(
                meta.divisionTeamRoleId.eq(divisionTeamRoleId),
                meta.dateTo.isNull()
        );

        return query().selectFrom(meta)
                .where(exp)
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> findByIds(List<Long> ids) {
        return query().selectFrom(meta)
                .where(meta.id.in(ids))
                .fetch();
    }

    public List<DivisionTeamAssignmentEntity> findActualByIds(List<Long> ids) {
        BooleanExpression exp = meta.id.in(ids)
                .and(meta.dateTo.isNull());
        return query().selectFrom(meta)
                .where(exp)
                .fetch();
    }

    public DivisionTeamAssignmentEntity findFirstByExternalEmployeeId(String externalEmployeeId) {
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                QEmployeeEntity.employeeEntity.id.eq(meta.employeeId),
                QEmployeeEntity.employeeEntity.externalId.eq(externalEmployeeId)
        );
        JPQLQuery<DivisionTeamAssignmentEntity> query = query()
                .selectFrom(meta);
        addBaseFetch(query);
        return query.where(exp)
                .fetchFirst();
    }

    /**
     * The method returns all division_team_assignment_id except input assignmentId in input legal_entity_id list
     *
     * @param assignmentId   input division_team_assignment_id that doesn't have to be included into the method result
     * @param legalEntityIds input legal_entity_id list by which need to find division_team_assignment_id list
     * @return list of division_team_assignment_id
     */
    public List<Long> findAllInLegalEntityIdsExceptAssignmentId(Long assignmentId, List<Long> legalEntityIds) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final QDivisionEntity d = new QDivisionEntity("division");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                d.legalEntityId.in(legalEntityIds),
                meta.id.eq(assignmentId).not()
        );

        return query().from(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .innerJoin(d).on(d.id.eq(dt.divisionId))
                .where(exp)
                .orderBy(meta.id.asc())
                .select(meta.id)
                .fetch();
    }

    public Long findIdByEmployeeId(Long employeeId) {
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dt.dateTo.isNull(),
                meta.employeeId.eq(employeeId)
        );

        return query().from(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .where(exp)
                .select(meta.id)
                .fetchFirst();
    }

    public DivisionTeamAssignmentShort findShortById(Long id) {
        QDivisionTeamAssignmentShort q = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        return query().selectFrom(q)
                .where(q.id.eq(id))
                .fetchFirst();
    }

    public Map<String, List<DivisionTeamAssignmentShort>> findDtaMapByNumbers(Collection<String> numbers) {
        QDivisionTeamAssignmentShort q = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        QEmployeeEntity emp = QEmployeeEntity.employeeEntity;

        final BooleanExpression exp = Expressions.allOf(
                q.dateTo.isNull(),
                emp.dateTo.isNull(),
                emp.number.in(numbers)
        );

        return query().select(emp.number, q).from(q)
                .innerJoin(emp).on(q.employeeId.eq(emp.id))
                .where(exp)
                .fetch()
                .stream()
                .collect(groupingBy(t -> t.get(emp.number), mapping(t -> t.get(q), toList())));
    }

    public List<DivisionTeamAssignmentEntity> findAllByDivisionTeamsAndSystemRole(List<Long> divisionTeamsIds, Integer systemRole) {
        QDivisionTeamRoleEntity divisionTeamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamEntity divisionTeam = QDivisionTeamEntity.divisionTeamEntity;

        return query().selectFrom(meta)
                .innerJoin(divisionTeam).on(divisionTeam.id.eq(divisionTeamRole.divisionTeamId))
                .innerJoin(divisionTeamRole).on(divisionTeamRole.id.eq(meta.divisionTeamRole.id))
                .where(divisionTeamRole.role.systemRole.id.eq(systemRole).and(divisionTeam.id.in(divisionTeamsIds))
                               .and(meta.dateTo.isNull()))
                .fetch();
    }

    public Tuple findSystemRoleAndEmployeeIdByAssignmentsId(Long divisionTeamAssignmentId) {
        return query().select(meta.divisionTeamRole.role.systemRoleId, meta.employeeId)
                .from(meta)
                .where(meta.id.eq(divisionTeamAssignmentId).and(meta.dateTo.isNull()))
                .fetchFirst();
    }

    /**
     * Найти всех сотрудников в команде, кроме тех у кого системная роль равна = systemRoleId на входе
     * @param divisionTeamId ИД команды
     * @param systemRoleId ИД системной роли, которую не нужно находить
     * @return массив ИД сотрудников
     */
    public List<Long> findEmployeeIdByDivisionTeamIdAndNotSystemRoleId(Long divisionTeamId, Integer systemRoleId) {
        QDivisionTeamRoleEntity qdtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QRoleEntity qr = QRoleEntity.roleEntity;
        BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                qdtr.divisionTeamId.eq(divisionTeamId),
                qr.systemRoleId.ne(systemRoleId)
        );
        return query().select(meta.employeeId)
                .from(meta)
                .join(meta.divisionTeamRole, qdtr)
                .join(qdtr.role, qr)
                .where(exp)
                .fetch();
    }

    /**
     * Найти в командах сотрудников, у которых системная роль = systemRoleId на входе
     * @param divisionTeamIds массив Ид команд
     * @param systemRoleId ИД системной роли, которую нужно находить
     * @return набор данных: ИД команды <-> ИД сотрудника
     */
    public List<Tuple> findEmployeeIdByDivisionTeamIdsAndSystemRoleId(Collection<Long> divisionTeamIds, Integer systemRoleId) {
        QDivisionTeamRoleEntity qdtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QRoleEntity qr = QRoleEntity.roleEntity;
        BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                qdtr.divisionTeamId.in(divisionTeamIds),
                qr.systemRoleId.eq(systemRoleId)
        );
        return query().select(qdtr.divisionTeamId, meta.employeeId)
                .from(meta)
                .join(meta.divisionTeamRole, qdtr)
                .join(qdtr.role, qr)
                .where(exp)
                .fetch();
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
