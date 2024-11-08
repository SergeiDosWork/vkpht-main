package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.orgstructure.domain.entity.*;

import static java.util.stream.Collectors.*;

@Repository
public class DivisionTeamSuccessorDao extends AbstractDao<DivisionTeamSuccessorEntity, Long> {

    private static final QDivisionTeamSuccessorEntity meta = QDivisionTeamSuccessorEntity.divisionTeamSuccessorEntity;

    public DivisionTeamSuccessorDao(EntityManager em) {
        super(DivisionTeamSuccessorEntity.class, em);
    }

    public List<DivisionTeamSuccessorEntity> getDivisionTeamSuccessorsByEmployeeId(Long employeeId) {
        return query().selectFrom(meta)
                .where(meta.employee.id.eq(employeeId))
                .fetch();
    }

    public List<DivisionTeamSuccessorEntity> getDivisionTeamSuccessorsByDivisionTeamRoleId(Long divisionTeamRoleId) {
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.divisionTeamRole.id.eq(divisionTeamRoleId)
        );

        return query().selectFrom(meta)
                .leftJoin(meta.employee, QEmployeeEntity.employeeEntity).fetchJoin()
                .leftJoin(QEmployeeEntity.employeeEntity.person, QPersonEntity.personEntity).fetchJoin()
                .where(exp)
                .fetch();
    }

    public Map<Long, List<DivisionTeamSuccessorEntity>> getDivisionTeamSuccessorsByDivisionTeamRoleIds(List<Long> divisionTeamRoleIds) {
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
                .where(exp)
                .fetch()
                .stream()
                .collect(groupingBy(t -> t.get(meta.divisionTeamRoleId), mapping(t -> t.get(meta), toList())));
    }

    public List<DivisionTeamSuccessorEntity> getAllDivisionTeamSuccessorsByDivisionTeamRoleId(Long divisionTeamRoleId) {
        return query().selectFrom(meta)
                .where(meta.divisionTeamRole.id.eq(divisionTeamRoleId))
                .fetch();
    }

    public List<DivisionTeamSuccessorEntity> getDivisionTeamSuccessorsForDeletion() {
        final QDivisionTeamRoleEntity dtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        final QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                dta.dateTo.isNull(),
                meta.employee.id.eq(dta.employeeId)
        );

        return query().selectFrom(meta)
                .innerJoin(dtr).on(dtr.id.eq(meta.divisionTeamRole.id))
                .innerJoin(dta).on(dta.divisionTeamRoleId.eq(dtr.id))
                .where(exp)
                .fetch();
    }
}
