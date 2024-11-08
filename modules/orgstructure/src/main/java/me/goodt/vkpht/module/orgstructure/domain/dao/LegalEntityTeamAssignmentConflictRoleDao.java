package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamAssignmentConflictRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QLegalEntityTeamAssignmentConflictRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QRoleEntity;

@Repository
public class LegalEntityTeamAssignmentConflictRoleDao extends AbstractDao<LegalEntityTeamAssignmentConflictRoleEntity, Long> {

    private static final QLegalEntityTeamAssignmentConflictRoleEntity meta = QLegalEntityTeamAssignmentConflictRoleEntity.legalEntityTeamAssignmentConflictRoleEntity;

    public LegalEntityTeamAssignmentConflictRoleDao(EntityManager em) {
        super(LegalEntityTeamAssignmentConflictRoleEntity.class, em);
    }

    public boolean existConflictRole(Set<Long> legalEntityTeamAssignmentIds, Set<Long> divisionTeamAssignmentIds, Long roleConflictedId) {
        return query().selectFrom(meta)
                .where(createConflictRoleExpression(legalEntityTeamAssignmentIds, divisionTeamAssignmentIds, roleConflictedId))
                .fetchCount() > 0;
    }

    public Integer findConflictRole(Set<Long> legalEntityTeamAssignmentIds, Set<Long> divisionTeamAssignmentIds, Long roleConflictedId) {
        return query().select(meta.legalEntityTeamAssignmentRoleIdAssigned.systemRole.id)
                .from(meta)
                .where(createConflictRoleExpression(legalEntityTeamAssignmentIds, divisionTeamAssignmentIds, roleConflictedId))
                .fetchFirst();
    }

    public boolean existsByRoleIds(Long divisionTeamAssignmentRoleIdAssigned,
                                   Long legalEntityTeamAssignmentRoleIdAssigned,
                                   Long legalEntityTeamAssignmentRoleIdConflicted) {
        BooleanExpression expression =
                meta.legalEntityTeamAssignmentRoleIdConflicted.id.eq(legalEntityTeamAssignmentRoleIdConflicted);
        expression = divisionTeamAssignmentRoleIdAssigned == null ?
                expression.and(meta.divisionTeamAssignmentRoleIdAssigned.isNull()) :
                expression.and(meta.divisionTeamAssignmentRoleIdAssigned.id.eq(divisionTeamAssignmentRoleIdAssigned));
        expression = legalEntityTeamAssignmentRoleIdAssigned == null ?
                expression.and(meta.legalEntityTeamAssignmentRoleIdAssigned.isNull()) :
                expression.and(meta.legalEntityTeamAssignmentRoleIdAssigned.id.eq(legalEntityTeamAssignmentRoleIdAssigned));
        return query().selectFrom(meta)
                .where(expression)
                .fetchCount() > 0;
    }

    private static BooleanExpression createConflictRoleExpression(Set<Long> legalEntityTeamAssignmentIds, Set<Long> divisionTeamAssignmentIds, Long roleConflictedId) {
        QRoleEntity legalAssign = meta.legalEntityTeamAssignmentRoleIdAssigned;
        QRoleEntity divisionAssign = meta.divisionTeamAssignmentRoleIdAssigned;

        return meta.legalEntityTeamAssignmentRoleIdConflicted.id.eq(roleConflictedId)
                .and(legalAssign.isNull().or(legalAssign.id.in(legalEntityTeamAssignmentIds)))
                .and(divisionAssign.isNull().or(divisionAssign.id.in(divisionTeamAssignmentIds)))
                .and(legalAssign.isNotNull().or(divisionAssign.isNotNull()));
    }
}
