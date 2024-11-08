package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;

/** @author iGurkin - 24.08.2022 */
@Repository
public class RoleDao extends AbstractArchivableDao<RoleEntity, Long> {

    private static final QRoleEntity meta = QRoleEntity.roleEntity;

    public RoleDao(EntityManager em) {
        super(RoleEntity.class, em);
    }

    public List<RoleEntity> findActualByAssignableTrue() {
        QSystemRoleEntity qRole = QSystemRoleEntity.systemRoleEntity;
        return query().selectFrom(meta)
                .join(meta.systemRole, qRole).fetchJoin()
                .where(meta.dateTo.isNull().and(qRole.isAssignable.eq(1)))
                .fetch();
    }

    public List<RoleEntity> findByCodes(List<String> codes) {
        return query().selectFrom(meta)
                .where(meta.code.in(codes))
                .leftJoin(QRoleEntity.roleEntity.systemRole, QSystemRoleEntity.systemRoleEntity).fetchJoin()
                .fetch();
    }

    public List<RoleEntity> findByCode(String code) {
        return query().selectFrom(meta)
                .where(meta.code.eq(code))
                .leftJoin(QRoleEntity.roleEntity.systemRole, QSystemRoleEntity.systemRoleEntity).fetchJoin()
                .fetch();
    }

    // @Query(value = "SELECT r.system_role_id FROM org_role AS r " +
            // "INNER JOIN org_legal_entity_team_assignment AS leta ON leta.role_id = r.id " +
            // "INNER JOIN org_legal_entity_team AS let ON leta.legal_entity_team_id = let.id " +
            // "WHERE (leta.date_to IS NULL OR CURRENT_TIMESTAMP BETWEEN leta.date_from AND leta.date_to) " +
            // "AND let.legal_entity_id = :legalEntityId AND leta.employee_id = :employeeId", nativeQuery = true)
    public List<Integer> findSystemRoleIdsByEmployeeIdAndLegalEntityId(Long employeeId, Long legalEntityId) {
        QLegalEntityTeamAssignmentEntity leta = QLegalEntityTeamAssignmentEntity.legalEntityTeamAssignmentEntity;
        QLegalEntityTeamEntity let = QLegalEntityTeamEntity.legalEntityTeamEntity;
        BooleanExpression exp = let.legalEntityId.eq(legalEntityId)
            .and(leta.employeeId.eq(employeeId))
            .and(leta.dateTo.isNull());
        return query().select(meta.systemRoleId).from(meta)
            .join(leta).on(leta.role.eq(meta))
            .join(leta.legalEntityTeamEntity, let)
            .where(exp)
            .fetch();
    }

    public boolean existForEmployee(Long employeeId, Integer sysRoleId) {
        final QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;

        final BooleanExpression exp = Expressions.allOf(
                dta.dateTo.isNull(),
                dt.dateTo.isNull(),
                dta.employeeId.eq(employeeId),
                meta.systemRoleId.eq(sysRoleId));

        return query().from(dta)
                .join(dtr).on(dtr.id.eq(dta.divisionTeamRoleId))
                .join(dt).on(dt.id.eq(dtr.divisionTeamId))
                .join(dtr.role, meta)
                .where(exp)
                .select(Expressions.ONE)
                .fetchCount() > 0;
    }

    public Optional<RoleEntity> findActualById(Long id) {
        return Optional.ofNullable(query().selectFrom(meta)
                .where(meta.id.eq(id).and(meta.dateTo.isNull()))
                .fetchFirst());
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    @Override
    protected JPQLQuery<RoleEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
