package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.LegalEntityTeamAssignmentCompactProjection;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentEntity;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class LegalEntityTeamAssignmentDao extends AbstractDao<LegalEntityTeamAssignmentEntity, Long> {

    private static final QLegalEntityTeamAssignmentEntity meta = QLegalEntityTeamAssignmentEntity.legalEntityTeamAssignmentEntity;

    public LegalEntityTeamAssignmentDao(EntityManager em) {
        super(LegalEntityTeamAssignmentEntity.class, em);
    }

    private BooleanExpression actual() {
        return meta.dateTo.isNull();
    }

    public List<LegalEntityTeamAssignmentEntity> findByLegalEntityIdsAndRoleId(Collection<Long> legalEntityIds, Collection<Long> roleIds, Collection<Long> employeeIds) {
        final QLegalEntityTeamEntity let = QLegalEntityTeamEntity.legalEntityTeamEntity;

        final BooleanExpression exp = meta.dateTo.isNull()
            .and(legalEntityIds != null && !legalEntityIds.isEmpty() ? let.legalEntityId.in(legalEntityIds) : null)
            .and(roleIds != null && !roleIds.isEmpty() ? meta.roleId.in(roleIds) : null)
            .and(employeeIds != null && !employeeIds.isEmpty() ? meta.employeeId.in(employeeIds) : null);

        return query().selectFrom(meta)
            .innerJoin(meta.legalEntityTeamEntity, let)
            .where(exp)
            .fetch();
    }

    public List<LegalEntityTeamAssignmentEntity> findByEmployeeId(Long employeeId) {
        final BooleanExpression exp = Expressions.allOf(
            actual(),
            meta.employeeId.eq(employeeId)
        );
        JPQLQuery<LegalEntityTeamAssignmentEntity> query = query().selectFrom(meta)
            .where(exp);
        addBaseFetch(query);
        return query.fetch();
    }

    public Optional<LegalEntityTeamAssignmentEntity> findByIdWithFetch(Long id) {
        final BooleanExpression exp = Expressions.allOf(
            actual(),
            meta.id.eq(id)
        );
        JPQLQuery<LegalEntityTeamAssignmentEntity> query = query().selectFrom(meta)
            .where(exp);
        addBaseFetch(query);
        return Optional.ofNullable(query.fetchOne());
    }

    private void addBaseFetch(JPQLQuery<LegalEntityTeamAssignmentEntity> query) {
        query.leftJoin(meta.employee, QEmployeeEntity.employeeEntity).fetchJoin()
            .leftJoin(QEmployeeEntity.employeeEntity.person, QPersonEntity.personEntity).fetchJoin()
            .leftJoin(meta.legalEntityTeamEntity, QLegalEntityTeamEntity.legalEntityTeamEntity).fetchJoin()
            .leftJoin(meta.role, QRoleEntity.roleEntity).fetchJoin()
            .leftJoin(QRoleEntity.roleEntity.systemRole, QSystemRoleEntity.systemRoleEntity).fetchJoin()
            .leftJoin(meta.status, QAssignmentStatusEntity.assignmentStatusEntity).fetchJoin();
    }

    // @Query(value = "SELECT leta.* FROM org_legal_entity_team_assignment AS leta " +
    //      "INNER JOIN org_legal_entity_team AS let ON leta.legal_entity_team_id = let.id " +
    //      "WHERE (leta.date_to IS NULL OR CURRENT_TIMESTAMP BETWEEN leta.date_from AND leta.date_to) " +
    //      "AND let.legal_entity_id = :legalEntityId AND leta.employee_id = :employeeId", nativeQuery = true)
    // List<LegalEntityTeamAssignment> findByEmployeeIdAndLegalEntityId(@Param("employeeId") Long employeeId, @Param("legalEntityId") Long legalEntityId);
    public JPQLQuery<?> findAllByEmployeeIdAndLegalEntityId(Long employeeId, Long legalEntityId) {
        QLegalEntityTeamEntity qlet = QLegalEntityTeamEntity.legalEntityTeamEntity;
        BooleanExpression exp = meta.employeeId.eq(employeeId)
            .and(qlet.legalEntityId.eq(legalEntityId))
            .and(meta.dateTo.isNull());
        return query().from(meta)
            .join(meta.legalEntityTeamEntity, qlet)
            .where(exp);
    }

    public List<LegalEntityTeamAssignmentEntity> findByLegalEntityIdAndRoleId(Long legalEntityId, Collection<Long> roleIds, Collection<Long> employeeIds) {
        QLegalEntityTeamEntity let = QLegalEntityTeamEntity.legalEntityTeamEntity;
        BooleanExpression exp = Expressions.allOf(
            meta.dateTo.isNull(),
            legalEntityId != null ? let.legalEntityId.eq(legalEntityId) : null,
            roleIds != null && !roleIds.isEmpty() ? meta.roleId.in(roleIds) : null,
            employeeIds != null && !employeeIds.isEmpty() ? meta.employeeId.in(employeeIds) : null
        );

        return query().selectFrom(meta)
            .join(meta.legalEntityTeamEntity, let)
            .where(exp)
            .fetch();
    }

    public List<LegalEntityTeamAssignmentEntity> findByLegalEntityIdAndSystemRoleIds(Long legalEntityId, Collection<Integer> systemRoleIds) {
        QLegalEntityTeamEntity let = QLegalEntityTeamEntity.legalEntityTeamEntity;
        BooleanExpression exp = Expressions.allOf(
            meta.dateTo.isNull(),
            legalEntityId != null ? let.legalEntityId.eq(legalEntityId) : null,
            systemRoleIds != null && !systemRoleIds.isEmpty() ? meta.role.systemRoleId.in(systemRoleIds) : null

        );

        return query().selectFrom(meta)
            .join(meta.legalEntityTeamEntity, let)
            .where(exp)
            .fetch();
    }

    public Optional<LegalEntityTeamAssignmentEntity> findByLegalEntityIdRoleIdEmployeeId(Long legalEntityId, Long roleId, Long employeeId) {
        QLegalEntityTeamEntity let = QLegalEntityTeamEntity.legalEntityTeamEntity;
        BooleanExpression exp = Expressions.allOf(
            let.legalEntityId.eq(legalEntityId),
            meta.roleId.eq(roleId),
            meta.employeeId.eq(employeeId),
            meta.dateTo.isNull()
        );

        return Optional.ofNullable(query().from(meta)
                                       .join(meta.legalEntityTeamEntity, let)
                                       .where(exp)
                                       .select(meta)
                                       .fetchFirst());
    }

    public List<LegalEntityTeamAssignmentEntity> findByEmployeeIdAndLegalEntityId(Long employeeId, Long legalEntityId) {
        QLegalEntityTeamEntity let = QLegalEntityTeamEntity.legalEntityTeamEntity;
        BooleanExpression exp = Expressions.allOf(
            meta.dateTo.isNull(),
            let.legalEntityId.eq(legalEntityId),
            meta.employeeId.eq(employeeId)
        );

        return query().from(meta)
            .join(meta.legalEntityTeamEntity, let)
            .where(exp)
            .select(meta)
                .fetch();
    }

    public List<LegalEntityTeamAssignmentEntity> findByLegalEntityTeamId(@Param("legalEntityTeamId") Long legalEntityTeamId) {
        return query().selectFrom(meta)
            .where(meta.legalEntityTeamEntity.id.eq(legalEntityTeamId))
            .fetch();
    }

    public List<LegalEntityTeamAssignmentEntity> findByRoleId(@Param("roleId") Long roleId) {
        return query().selectFrom(meta)
            .where(meta.roleId.eq(roleId))
            .fetch();
    }

    public List<LegalEntityTeamAssignmentCompactProjection> findCompactByEmployeeId(Long employeeId) {
        final QLegalEntityTeamEntity legalTeam = QLegalEntityTeamEntity.legalEntityTeamEntity;
        final QRoleEntity role = QRoleEntity.roleEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                legalTeam.dateTo.isNull(),
                meta.employeeId.eq(employeeId)
        );

        QBean<LegalEntityTeamAssignmentCompactProjection> proj = Projections.bean(LegalEntityTeamAssignmentCompactProjection.class,
                                                                                  meta.id.as("id"),
                                                                                  meta.legalEntityTeamId.as("legalEntityTeamId"),
                                                                                  role.systemRoleId.as("systemRoleId"),
                                                                                  meta.fullName.as("fullName"),
                                                                                  meta.shortName.as("shortName")
        );

        return query().select(proj)
                .from(meta)
                .join(legalTeam).on(legalTeam.id.eq(meta.legalEntityTeamId))
                .join(role).on(role.id.eq(meta.roleId))
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
