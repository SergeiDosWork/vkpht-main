package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.api.dto.request.FindEmployeeRequest;
import me.goodt.vkpht.module.orgstructure.domain.projection.UnitShortInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.domain.entity.orgstructure.projection.UnitShortInfo;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class EmployeeDao extends AbstractDao<EmployeeEntity, Long> {

    private static final QEmployeeEntity meta = QEmployeeEntity.employeeEntity;

    public EmployeeDao(EntityManager em) {
        super(EmployeeEntity.class, em);
    }

    public Long findIdByExternalId(String extId) {
        return query().select(meta.id)
            .from(meta)
            .where(meta.externalId.eq(extId))
            .fetchFirst();
    }

    public List<EmployeeEntity> findAllBySnils(String snils) {
        final QPersonEntity p = QPersonEntity.personEntity;
        return query().selectFrom(meta)
            .innerJoin(meta.person, p).fetchJoin()
            .where(p.snils.eq(snils))
            .fetch();
    }

    public List<EmployeeEntity> findAllByPersonId(Long personId) {
        final QPersonEntity p = QPersonEntity.personEntity;
        return query().selectFrom(meta)
            .innerJoin(meta.person, p).fetchJoin()
            .where(p.id.eq(personId))
            .fetch();
    }

    public Page<EmployeeEntity> findByParams(FindEmployeeRequest request) {
        final QPositionAssignmentEntity posAss = QPositionAssignmentEntity.positionAssignmentEntity;
        final QPositionEntity pos = QPositionEntity.positionEntity;
        final QDivisionEntity div = QDivisionEntity.divisionEntity;
        final QPersonEntity per = QPersonEntity.personEntity;

        List<Long> employeeIds = request.getEmployeeIds();
        List<Long> divisionIds = request.getDivisionIds();
        List<Long> functionIds = request.getFunctionIds();
        Long jobTitleId = request.getJobTitleId();
        String positionShortName = request.getPositionShortName();
        Long legalEntityId = request.getLegalEntityId();
        List<String> employeeNumber = request.getEmployeeNumber();
        List<String> emails = request.getEmails();
        BooleanExpression exp = Expressions.allOf(
            request.isWithClosed() ? null : meta.dateTo.isNull(),
            CollectionUtils.isEmpty(employeeIds) ? null : meta.id.in(employeeIds),
            CollectionUtils.isEmpty(divisionIds) ? null : pos.divisionId.in(divisionIds),
            CollectionUtils.isEmpty(divisionIds) && CollectionUtils.isEmpty(functionIds) && legalEntityId == null ? null : posAss.dateTo.isNull(),
            CollectionUtils.isEmpty(functionIds) ? null : pos.workFunctionId.in(functionIds),
            jobTitleId == null ? null : pos.jobTitleId.eq(jobTitleId),
            StringUtils.isBlank(positionShortName) ? null : pos.shortName.equalsIgnoreCase(positionShortName),
            legalEntityId == null ? null : div.legalEntityId.eq(legalEntityId),
            CollectionUtils.isEmpty(employeeNumber) ? null : meta.number.in(employeeNumber),
            CollectionUtils.isEmpty(emails) ? null : meta.email.in(emails)
        );

        String searchingValue = request.getSearchingValue();
        if (!StringUtils.isEmpty(searchingValue)) {
            String[] searchValue = searchingValue.toLowerCase().split(" ");
            boolean withPatronymic = request.isWithPatronymic();
            for (String value : searchValue) {
                if (exp != null) {
                    exp = exp.and(per.surname.toLowerCase().like("%" + value + "%")
                        .or(per.name.toLowerCase().like("%" + value + "%"))
                        .or(withPatronymic ? per.patronymic.toLowerCase().like("%" + value + "%") : null)
                        .or(meta.number.like("%" + value + "%")));
                } else {
                    exp = per.surname.toLowerCase().like("%" + value + "%")
                        .or(per.name.toLowerCase().like("%" + value + "%"))
                        .or(!withPatronymic ? null : per.patronymic.toLowerCase().like("%" + value + "%"))
                        .or(meta.number.like("%" + value + "%"));
                }
            }
        }

        JPQLQuery<EmployeeEntity> query = query().selectFrom(meta);
        if (legalEntityId != null) {
            query
                .innerJoin(posAss).on(posAss.employee.eq(meta))
                .innerJoin(pos).on(pos.eq(posAss.position))
                .innerJoin(div).on(div.eq(pos.division));
        }

        if (legalEntityId == null && (
            (divisionIds != null && !divisionIds.isEmpty())
                || (functionIds != null && !functionIds.isEmpty())
                || jobTitleId != null
                || StringUtils.isNotBlank(positionShortName)
        )) {
            query
                .innerJoin(posAss).on(posAss.employee.eq(meta))
                .innerJoin(pos).on(pos.eq(posAss.position));
        }

        if (!StringUtils.isEmpty(searchingValue)) {
            query.innerJoin(per).on(per.eq(meta.person));
        }
        query.where(exp);

        if (!CollectionUtils.isEmpty(employeeNumber)) {
            query.orderBy(meta.number.asc());
        }

        Pageable pageable = request.getPageable();
        JPQLQuery<EmployeeEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(EmployeeEntity.class))
            .applyPagination(pageable, query);
        return new PageImpl<>(pagedQuery.fetch(), pageable, query.fetchCount());
    }

    public List<EmployeeEntity> findAllByIds(Set<Long> emplyeIds) {
        return query().selectFrom(meta)
            .leftJoin(meta.person, QPersonEntity.personEntity).fetchJoin()
            .where(meta.id.in(emplyeIds))
            .fetch();
    }

    public List<String> getActualNumbers(List<String> numbers) {
        return query().select(meta.number).from(meta)
            .where(meta.number.in(numbers))
            .distinct()
            .fetch();
    }

    public List<EmployeeEntity> employeeByExternalId(String externalId) {
        return query().selectFrom(meta)
            .where(meta.externalId.eq(externalId))
            .fetch();
    }

    public List<EmployeeEntity> findAllById(Long employeeId) {
        return query().selectFrom(meta)
            .leftJoin(meta.person, QPersonEntity.personEntity).fetchJoin()
            .where(meta.id.eq(employeeId))
            .fetch();
    }

    public EmployeeEntity findActualById(Long employeeId) {
        return query().selectFrom(meta)
            .leftJoin(meta.person, QPersonEntity.personEntity).fetchJoin()
            .where(meta.id.eq(employeeId))
            .fetchOne();
    }

    public List<EmployeeEntity> findByIds(Collection<Long> ids) {
        return query().selectFrom(meta)
            .where(meta.id.in(ids))
            .join(meta.person).fetchJoin()
            .fetch();
    }

    public EmployeeEntity findHeadTeamEmployee(Long divisionTeamId) {
        final QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QRoleEntity r = QRoleEntity.roleEntity;

        final BooleanExpression exp = Expressions.allOf(
            dtr.divisionTeamId.eq(divisionTeamId),
            r.systemRoleId.eq(1)
        );

        return query().selectFrom(meta)
            .join(meta, dta.employee)
            .join(dtr, dta.divisionTeamRole)
            .join(r, dtr.role)
            .where(exp)
            .fetchFirst();
    }

    public List<EmployeeEntity> findBySearchValue(String searchValue) {
        final QPersonEntity p = QPersonEntity.personEntity;

        final BooleanExpression exp = Expressions.anyOf(
            p.surname.containsIgnoreCase(searchValue),
            p.name.containsIgnoreCase(searchValue),
            p.patronymic.containsIgnoreCase(searchValue),
            meta.number.containsIgnoreCase(searchValue)
        );

        return query().selectFrom(meta)
            .join(p, meta.person)
            .where(exp)
            .fetch();
    }

    public List<EmployeeEntity> findByIdAndExternalId(Long id, String externalId) {
        BooleanExpression exp = Expressions.allOf(
            id == null ? null : meta.id.eq(id),
            externalId == null ? null : meta.externalId.eq(externalId)
        );

        return query().selectFrom(meta)
            .leftJoin(meta.person, QPersonEntity.personEntity).fetchJoin()
            .where(exp)
            .fetch();
    }

    public Page<EmployeeEntity> findAll(Pageable pageable) {
        JPQLQuery<EmployeeEntity> select = query().selectFrom(meta);
        JPQLQuery<EmployeeEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(EmployeeEntity.class))
            .applyPagination(pageable, select);
        List<EmployeeEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

    public Optional<EmployeeEntity> findByIdWithFetch(Long id) {
        JPQLQuery<EmployeeEntity> query = query().selectFrom(meta)
            .leftJoin(meta.person, QPersonEntity.personEntity).fetchJoin()
            .where(meta.id.eq(id));
        return Optional.ofNullable(query.fetchOne());
    }

    public Optional<EmployeeEntity> findByExternalId(String extId) {
        return Optional.ofNullable(query().select(meta)
            .from(meta)
            .leftJoin(meta.person, QPersonEntity.personEntity).fetchJoin()
            .where(meta.externalId.eq(extId))
            .fetchFirst());
    }

    public List<EmployeeEntity> findWorkersByDivisionId(Long divisionId) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;
        QDivisionTeamAssignmentEntity divisionTeamAssignment = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamRoleEntity divisionTeamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QPersonEntity person = QPersonEntity.personEntity;

        BooleanExpression filter = Expressions.allOf(
            divisionTeamRole.roleId.eq(2L),
            position.divisionId.eq(divisionId),

            meta.dateTo.isNull(),
            positionAssignment.dateTo.isNull(),
            position.dateTo.isNull(),
            divisionTeamAssignment.dateTo.isNull()
        );

        return query().selectFrom(meta)
            .join(positionAssignment).on(positionAssignment.employeeId.eq(meta.id))
            .join(position).on(positionAssignment.position.id.eq(position.id)).fetchJoin()
            .join(divisionTeamAssignment).on(divisionTeamAssignment.employeeId.eq(meta.id))
            .join(meta.person, person).fetchJoin()
            .join(divisionTeamRole).on(divisionTeamRole.id.eq(divisionTeamAssignment.divisionTeamRoleId))
            .where(filter)
            .fetch();
    }

    public List<EmployeeEntity> findSubdivisionHeadsByDivisionId(Long divisionId) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;
        QDivisionTeamAssignmentEntity divisionTeamAssignment = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamRoleEntity divisionTeamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QPersonEntity person = QPersonEntity.personEntity;
        QDivisionTeamEntity divisionTeam = QDivisionTeamEntity.divisionTeamEntity;


        BooleanExpression filter = Expressions.allOf(
            divisionTeamRole.roleId.eq(1L),
            divisionTeam.parentId.eq(divisionId),

            meta.dateTo.isNull(),
            positionAssignment.dateTo.isNull(),
            position.dateTo.isNull(),
            divisionTeamAssignment.dateTo.isNull(),
            person.dateTo.isNull(),
            divisionTeam.dateTo.isNull()
        );

        return query().selectFrom(meta)
            .join(positionAssignment).on(positionAssignment.employeeId.eq(meta.id))
            .join(position).on(positionAssignment.position.id.eq(position.id)).fetchJoin()
            .join(divisionTeamAssignment).on(divisionTeamAssignment.employeeId.eq(meta.id))
            .join(divisionTeamRole).on(divisionTeamRole.id.eq(divisionTeamAssignment.divisionTeamRoleId))
            .join(divisionTeam).on(divisionTeamRole.divisionTeamId.eq(divisionTeam.id))
            .join(meta.person, person).fetchJoin()
            .where(filter)
            .fetch();
    }

    public List<UnitShortInfo> findAvailableUnits(String externalEmployeeId) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;
        QDivisionEntity division = QDivisionEntity.divisionEntity;
        QLegalEntityEntity legalEntity = QLegalEntityEntity.legalEntityEntity;
        QUnitEntity unit = QUnitEntity.unitEntity;

        BooleanExpression filter = Expressions.allOf(
            positionAssignment.dateTo.isNull(),
            position.dateTo.isNull(),
            division.dateTo.isNull(),
            legalEntity.dateTo.isNull(),
            unit.dateTo.isNull(),
            meta.dateTo.isNull(),
            meta.externalId.eq(externalEmployeeId)
        );

        return query().select(Projections.constructor(UnitShortInfo.class, unit.code, unit.name, unit.description))
            .from(meta)
            .join(positionAssignment).on(positionAssignment.employeeId.eq(meta.id))
            .join(position).on(position.id.eq(positionAssignment.positionId))
            .join(division).on(division.id.eq(position.divisionId))
            .join(legalEntity).on(legalEntity.id.eq(division.legalEntityId))
            .join(unit).on(unit.id.eq(legalEntity.unitCode))
            .where(filter)
            .distinct()
            .fetch();
    }

    public boolean isUnitAvailable(String unitCode, String externalEmployeeId) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;
        QDivisionEntity division = QDivisionEntity.divisionEntity;
        QLegalEntityEntity legalEntity = QLegalEntityEntity.legalEntityEntity;
        QUnitEntity unit = QUnitEntity.unitEntity;

        BooleanExpression filter = Expressions.allOf(
            meta.externalId.eq(externalEmployeeId),
            unit.code.eq(unitCode),
            positionAssignment.dateTo.isNull(),
            position.dateTo.isNull(),
            division.dateTo.isNull(),
            legalEntity.dateTo.isNull(),
            unit.dateTo.isNull(),
            meta.dateTo.isNull()
        );

        return query().selectFrom(meta)
            .join(positionAssignment).on(positionAssignment.employeeId.eq(meta.id))
            .join(position).on(position.id.eq(positionAssignment.positionId))
            .join(division).on(division.id.eq(position.divisionId))
            .join(legalEntity).on(legalEntity.id.eq(division.legalEntityId))
            .join(unit).on(unit.id.eq(legalEntity.unitCode))
            .where(filter)
            .fetchCount() > 0;
    }

    public Tuple getPositionEntity(Long id) {
        QPersonEntity person = QPersonEntity.personEntity;
        QPositionAssignmentEntity assignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;
        return query().select(person, position.shortName)
            .from(meta)
            .join(person).on(person.id.eq(meta.personId))
            .join(assignment).on(assignment.employeeId.eq(meta.id))
            .join(position).on(position.id.eq(assignment.positionId))
            .where(meta.personId.eq(id))
            .distinct()
            .fetchOne();
    }

    public String findUnitByEmployee(Long employeeId) {
        QPositionAssignmentEntity positionAssignment = QPositionAssignmentEntity.positionAssignmentEntity;
        QPositionEntity position = QPositionEntity.positionEntity;
        QDivisionEntity division = QDivisionEntity.divisionEntity;
        QLegalEntityEntity legalEntity = QLegalEntityEntity.legalEntityEntity;
        QUnitEntity unit = QUnitEntity.unitEntity;

        BooleanExpression filter = Expressions.allOf(
            positionAssignment.employeeId.eq(employeeId),
            meta.dateTo.isNull()
        );

        return query().select(unit.code)
            .from(positionAssignment)
            .join(position).on(position.id.eq(positionAssignment.positionId))
            .join(division).on(division.id.eq(position.divisionId))
            .join(legalEntity).on(legalEntity.id.eq(division.legalEntityId))
            .join(unit).on(unit.id.eq(legalEntity.unitCode))
            .join(meta).on(meta.id.eq(employeeId))
            .where(filter)
            .fetchFirst();
    }
}
