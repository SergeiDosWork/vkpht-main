package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.*;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class PositionPositionImportanceDao extends AbstractDao<PositionPositionImportanceEntity, Long> {

    private static final QPositionPositionImportanceEntity meta = QPositionPositionImportanceEntity.positionPositionImportanceEntity;

    public PositionPositionImportanceDao(EntityManager em) {
        super(PositionPositionImportanceEntity.class, em);
    }

    //@Query(value = "SELECT * FROM position_position_importance WHERE (date_to IS NULL OR CURRENT_TIMESTAMP BETWEEN date_from AND date_to) AND position_id = :positionId"
    public List<PositionPositionImportanceEntity> findActualByPositionId(Long positionId, String unitCode) {
        QPositionImportanceEntity pi = QPositionImportanceEntity.positionImportanceEntity;
        final BooleanExpression exp = meta.dateTo.isNull()
                .and(meta.position.id.eq(positionId))
            .and(meta.positionImportance.isNull().or(pi.unitCode.eq(unitCode)));
        return query()
                .selectFrom(meta)
                .leftJoin(pi).on(pi.id.eq(meta.positionImportance.id))
                .where(exp)
                .fetch();
    }

    //"SELECT DISTINCT ppi.* FROM position_position_importance AS ppi " +
    //  "INNER JOIN position AS p ON ppi.position_id = p.id " +
    //  "INNER JOIN position_assignment AS pa ON pa.position_id = p.id " +
    //  "INNER JOIN employee AS em ON  pa.employee_id = em.id " +
    //  "INNER JOIN division_team_assignment AS dta ON dta.employee_id = em.id " +
    //  "INNER JOIN division_team_role AS dtr ON dta.division_team_role_id = dtr.id " +
    //  "INNER JOIN division_team AS dt ON dtr.division_team_id = dt.id " +
    //  "WHERE (dt.id IN (:divisionTeamIds) OR :divisionTeamFlag) AND (dta.id IN (:userIds) OR :usersFlag)"
    public List<PositionPositionImportanceEntity> findAllPositionPositionImportanceByDivisionTeamAndUsers(List<Long> divisionTeamIds, List<Long> userIds, List<Long> positionIds, boolean withClosed, String unitCode) {

        QPositionEntity p = QPositionEntity.positionEntity;
        QDivisionEntity d = QDivisionEntity.divisionEntity;
        QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamEntity dt = QDivisionTeamEntity.divisionTeamEntity;
        QPositionImportanceEntity pi = QPositionImportanceEntity.positionImportanceEntity;

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(meta.positionImportance.isNull().or(pi.unitCode.eq(unitCode)));
        if (!withClosed) {
            predicates.add(meta.dateTo.isNull());
        }
        if (!CollectionUtils.isEmpty(divisionTeamIds)) {
            predicates.add(dt.id.in(divisionTeamIds));
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            predicates.add(dta.id.in(userIds));
        }
        if (!CollectionUtils.isEmpty(positionIds)) {
            predicates.add(p.id.in(positionIds));
        }

        return query()
                .selectFrom(meta)
                .innerJoin(p).on(meta.position.id.eq(p.id))
                .innerJoin(d).on(p.division.id.eq(d.id))
                .innerJoin(dt).on(dt.divisionId.eq(d.id))
                .innerJoin(dtr).on(dtr.divisionTeam.id.eq(dt.id))
                .innerJoin(dta).on(dta.divisionTeamRoleId.eq(dtr.id))
                .leftJoin(pi).on(pi.id.eq(meta.positionImportance.id))
                .where(ExpressionUtils.allOf(predicates))
                .distinct()
                .fetch();
    }
}
