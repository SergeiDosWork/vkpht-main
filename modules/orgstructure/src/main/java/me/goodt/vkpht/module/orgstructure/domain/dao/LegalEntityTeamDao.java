package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityTeamEntity;

@Repository
public class LegalEntityTeamDao extends AbstractDao<LegalEntityTeamEntity, Long> {

    private static final QLegalEntityTeamEntity meta = QLegalEntityTeamEntity.legalEntityTeamEntity;

    public LegalEntityTeamDao(EntityManager em) {
        super(LegalEntityTeamEntity.class, em);
    }

    public List<Tuple> findIdsByDtaId(Collection<Long> divisionTeamAssignmentIds) {
        final QLegalEntityEntity le = QLegalEntityEntity.legalEntityEntity;
        final QDivisionEntity div = QDivisionEntity.divisionEntity;
        final QDivisionTeamEntity divTeam = QDivisionTeamEntity.divisionTeamEntity;
        final QDivisionTeamRoleEntity divTeamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                le.dateTo.isNull(),
                div.dateTo.isNull(),
                divTeam.dateTo.isNull(),
                dta.dateTo.isNull(),
                dta.id.in(divisionTeamAssignmentIds)
        );

        return query().select(dta.id, meta.id)
                .from(meta)
                .join(le).on(le.id.eq(meta.legalEntityId))
                .join(div).on(div.legalEntityId.eq(le.id))
                .join(divTeam).on(divTeam.divisionId.eq(div.id))
                .join(divTeamRole).on(divTeamRole.divisionTeamId.eq(divTeam.id))
                .join(dta).on(dta.divisionTeamRoleId.eq(divTeamRole.id))
                .where(exp)
                .fetch();
    }

    public LegalEntityTeamEntity findByLegalEntityIdAndTypeId(Long legalEntityId, Integer typeId) {
        BooleanExpression exp = meta.type.id.eq(typeId)
            .and(meta.legalEntityId.eq(legalEntityId));
        return query().selectFrom(meta)
            .where(exp)
            .fetchFirst();
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
