package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.orgstructure.domain.entity.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class DivisionTeamAssignmentRotationDao extends AbstractDao<DivisionTeamAssignmentRotationEntity, Long> {

    private static final QDivisionTeamAssignmentRotationEntity meta = QDivisionTeamAssignmentRotationEntity.divisionTeamAssignmentRotationEntity;

    public DivisionTeamAssignmentRotationDao(EntityManager em) {
        super(DivisionTeamAssignmentRotationEntity.class, em);
    }

    public Map<Long, List<DivisionTeamAssignmentRotationEntity>> findActualByAssignmentIds(List<Long> divisionTeamAssignmentIds) {
        final BooleanExpression exp = meta.assignmentId.in(divisionTeamAssignmentIds)
            .and(meta.dateTo.isNull());

        final List<DivisionTeamAssignmentRotationEntity> list = query()
            .from(meta)
            .select(meta)
            .leftJoin(meta.rotation, QAssignmentRotationEntity.assignmentRotationEntity).fetchJoin()
            .leftJoin(meta.assignment, QDivisionTeamAssignmentShort.divisionTeamAssignmentShort).fetchJoin()
            .where(exp)
            .fetch();

        return list
            .stream()
            .collect(groupingBy(DivisionTeamAssignmentRotationEntity::getAssignmentId, mapping(t -> t, toList())));
    }

    public List<DivisionTeamAssignmentRotationEntity> findByAssignmentId(Long assignmentId) {
        final BooleanExpression exp = meta.assignmentId.eq(assignmentId)
            .and(meta.dateTo.isNull());

        return query()
            .selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public List<DivisionTeamAssignmentRotationEntity> findByAssignmentIdAndRotationId(Long assignmentId, Integer rotationId) {
        final BooleanExpression exp = meta.assignmentId.eq(assignmentId)
            .and(meta.rotation.id.eq(rotationId));

        return query()
            .selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public List<DivisionTeamAssignmentRotationEntity> getAllByDivisionTeamId(Long divisionTeamId) {
        final QDivisionTeamAssignmentEntity dta = new QDivisionTeamAssignmentEntity("divisionTeamAssignment");
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final BooleanExpression exp = dtr.divisionTeamId.eq(divisionTeamId);

        return query().selectFrom(meta)
            .innerJoin(dta).on(dta.id.eq(meta.assignmentId).and(dta.dateTo.isNull().and(meta.dateTo.isNull())))
            .innerJoin(dtr).on(dtr.id.eq(dta.divisionTeamRoleId))
            .where(exp)
            .fetch();
    }
}
