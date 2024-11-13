package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.domain.dao.filter.LegalEntityFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class LegalEntityDao extends AbstractDao<LegalEntityEntity, Long> {

    private static final QLegalEntityEntity meta = QLegalEntityEntity.legalEntityEntity;

    public LegalEntityDao(EntityManager em) {
        super(LegalEntityEntity.class, em);
    }

    public List<LegalEntityEntity> find(LegalEntityFilter filter) {
        Predicate where = toPredicate(filter);
        List<LegalEntityEntity> rsl = query().selectFrom(meta)
            .where(where)
            .fetch();
        return rsl;
    }

    private Predicate toPredicate(LegalEntityFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getExternalId() != null) {
            where.and(meta.externalId.eq(filter.getExternalId()));
        }
        if (filter.getIds() != null && !filter.getIds().isEmpty()) {
            where.and(meta.id.in(filter.getIds()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(actual());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }
        return where;
    }

    private BooleanExpression actual() {
        return meta.dateTo.isNull();
    }

    public List<LegalEntityEntity> findByDivisionIdsAndDivisionGroupIds(List<Long> divisionIds, List<Long> divisionGroupIds, String unitCode) {
        final QDivisionEntity div = QDivisionEntity.divisionEntity;

        final BooleanExpression exp = Expressions.allOf(
            divisionIds != null && !divisionIds.isEmpty() ? div.id.in(divisionIds) : null,
            divisionGroupIds != null && !divisionGroupIds.isEmpty() ? div.groupId.in(divisionGroupIds) : null,
            meta.unitCode.eq(unitCode)
        );

        if ((divisionIds != null && !divisionIds.isEmpty()) || (divisionGroupIds != null && !divisionGroupIds.isEmpty())) {
            return query().selectFrom(meta)
                .innerJoin(div).on(meta.id.eq(div.legalEntityEntity.id))
                .where(exp)
                .distinct()
                .fetch();
        }

        return find(LegalEntityFilter.builder()
            .unitCode(unitCode)
            .build());
    }

    public List<LegalEntityEntity> findByDivisionIds(Collection<Long> divisionIds, String unitCode) {
        final QDivisionEntity div = QDivisionEntity.divisionEntity;

        final BooleanExpression exp = Expressions.allOf(
            actual(),
            divisionIds != null && !divisionIds.isEmpty() ? div.id.in(divisionIds) : null,
            meta.unitCode.eq(unitCode)
        );

        if ((divisionIds != null && !divisionIds.isEmpty())) {
            return query().selectFrom(meta)
                .innerJoin(div).on(meta.id.eq(div.legalEntityEntity.id))
                .where(exp)
                .distinct()
                .fetch();
        }

        return find(LegalEntityFilter.builder()
            .unitCode(unitCode)
            .build());
    }

    public Long findIdFirstByDivisionTeamAssignment(Long dtaId, String unitCode) {
        QDivisionTeamAssignmentEntity qdta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamRoleEntity qrl = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamEntity qdte = QDivisionTeamEntity.divisionTeamEntity;
        QDivisionEntity qdv = QDivisionEntity.divisionEntity;

        final BooleanExpression exp = qdta.id.eq(dtaId)
            .and(meta.unitCode.eq(unitCode));
        return query().from(qdta)
            .join(qrl).on(qdta.divisionTeamRoleId.eq(qrl.id))
            .join(qrl.divisionTeam, qdte)
            .join(qdte.division, qdv)
            .select(qdv.legalEntityId)
            .where(exp)
            .fetchOne();
    }

    public Long findIdByDivisionId(Long divisionId, String unitCode) {
        QDivisionEntity qd = QDivisionEntity.divisionEntity;
        final BooleanExpression exp = qd.id.eq(divisionId)
            .and(meta.unitCode.eq(unitCode));
        return query().from(qd)
            .where(exp)
            .select(qd.legalEntityId)
            .fetchOne();
    }

    public List<LegalEntityEntity> findByIds(List<Long> divisionIds, String unitCode) {
        QDivisionEntity qd = QDivisionEntity.divisionEntity;
        final BooleanExpression exp = qd.id.in(divisionIds)
            .and(meta.unitCode.eq(unitCode));
        return query().from(qd)
            .join(qd.legalEntityEntity, meta)
            .select(meta)
            .where(exp)
            .fetch();
    }

    public List<Long> findAllByEmployeeId(Long employeeId, String unitCode) {
        QDivisionEntity d = QDivisionEntity.divisionEntity;
        QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QEmployeeEntity e = QEmployeeEntity.employeeEntity;
        BooleanExpression exp = Expressions.allOf(
            dta.dateTo.isNull(),
            dt.dateTo.isNull(),
            meta.dateTo.isNull(),
            e.id.eq(employeeId),
            meta.unitCode.eq(unitCode)
        );

        return query().from(meta)
            .join(d).on(d.legalEntityId.eq(meta.id))
            .join(dt).on(dt.divisionId.eq(d.id))
            .join(dtr).on(dtr.divisionTeam.id.eq(dt.id))
            .join(dta).on(dta.divisionTeamRoleId.eq(dtr.id))
            .join(dta.employee, e)
            .select(meta.id)
            .where(exp)
            .orderBy(meta.id.asc())
            .fetch();
    }

    public Long findIdByDivisionTeam(Long teamId, String unitCode) {
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        final QDivisionEntity dv = QDivisionEntity.divisionEntity;
        final BooleanExpression exp = dt.id.eq(teamId)
            .and(meta.unitCode.eq(unitCode));
        return query().from(dt)
            .join(dt.division, dv)
            .where(exp)
            .select(dv.legalEntityId)
            .fetchFirst();
    }

    public List<Long> findAllByEmployeeRole(Long employeeId, Set<Integer> roles, String unitCode) {
        final QLegalEntityTeamAssignmentEntity leta = QLegalEntityTeamAssignmentEntity.legalEntityTeamAssignmentEntity;
        final QRoleEntity re = QRoleEntity.roleEntity;
        final QLegalEntityTeamEntity le = QLegalEntityTeamEntity.legalEntityTeamEntity;
        final BooleanExpression exp = leta.employeeId.eq(employeeId)
            .and(re.systemRoleId.in(roles))
            .and(meta.unitCode.eq(unitCode));
        return query().from(leta)
            .join(leta.role, re)
            .join(leta.legalEntityTeamEntity, le)
            .where(exp)
            .select(le.legalEntityId)
            .fetch();
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }
}
