package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.goodt.drive.rtcore.constants.RoleId;
import com.goodt.drive.rtcore.GlobalDefs;
import me.goodt.vkpht.module.orgstructure.domain.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class DivisionTeamDao extends AbstractDao<DivisionTeamEntity, Long> {

    private static final QDivisionTeamEntity meta = QDivisionTeamEntity.divisionTeamEntity;

    public DivisionTeamDao(EntityManager em) {
        super(DivisionTeamEntity.class, em);
    }

    public List<DivisionTeamEntity> findByDivisionId(@Param("divisionId") Long divisionId) {
        BooleanExpression eq = meta.divisionId.eq(divisionId)
                .and(actual());
        return query().selectFrom(meta)
                .where(eq)
                .fetch();
    }

    public List<Long> findIdsByParentId(Long parentId) {
        return query().from(meta)
                .where(meta.parentId.eq(parentId))
                .select(meta.id)
                .fetch();
    }

    public List<Long> findActualIdsByParentId(Long parentId) {
        return query().from(meta)
                .where(actual().and(meta.parentId.eq(parentId)))
                .select(meta.id)
                .fetch();
    }

    public List<DivisionTeamEntity> findNearestChildren(Long divisionTeamId) {
        return query().selectFrom(meta)
                .where(meta.parentId.eq(divisionTeamId))
                .fetch();
    }

    public Long countNearestChildren(@Param("divisionTeamId") Long divisionTeamId) {
        BooleanExpression exp = meta.parentId.eq(divisionTeamId)
                .and(actual());
        return query().from(meta)
                .where(exp)
                .select(Expressions.ONE)
                .fetchCount();
    }

    private BooleanExpression actual() {
        return meta.dateTo.isNull();
    }

    public Map<Long, List<DivisionTeamEntity>> findActualByDivisionIds(List<Long> divisionIds) {
        final BooleanExpression exp = actual()
                .and(meta.divisionId.in(divisionIds));

        return query()
                .from(meta)
                .select(meta.divisionId, meta)
                .where(exp)
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(t -> t.get(meta.divisionId), Collectors.mapping(t -> t.get(meta), Collectors.toList())));
    }

    public Long findParentIdById(Long id) {
        return query().select(meta.parentId)
                .from(meta)
                .where(meta.id.eq(id))
                .fetchOne();
    }

    public List<Long> findIdsByParentIds(List<Long> parentIds) {
        return query().select(meta.id)
                .from(meta)
                .where(meta.parent.id.in(parentIds))
                .fetch();
    }

    public List<DivisionTeamEntity> findActual() {
        return query().selectFrom(meta)
                .where(actual())
                .fetch();
    }

    public JPQLQuery<?> findAllByLegalEntityId(Long legalEntityId) {
        QDivisionEntity qdv = QDivisionEntity.divisionEntity;
        BooleanExpression exp = qdv.legalEntityId.eq(legalEntityId);
        return query().from(meta)
                .join(meta.division, qdv)
                .where(exp);
    }

    public List<Long> findAllByEmployee(Long employeeId) {
        final QDivisionTeamAssignmentEntity dta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        final QDivisionTeamRoleEntity dtr = new QDivisionTeamRoleEntity("divisionTeamRole");
        final QDivisionTeamEntity dt = new QDivisionTeamEntity("divisionTeam");
        final BooleanExpression exp = Expressions.allOf(
                dta.dateTo.isNull(),
                dt.dateTo.isNull(),
                dta.employeeId.eq(employeeId));

        return query().from(dta)
                .innerJoin(dtr).on(dtr.id.eq(dta.divisionTeamRoleId))
                .innerJoin(dt).on(dt.id.eq(dtr.divisionTeamId))
                .where(exp)
                .select(dt.id)
                .fetch();
    }

    public DivisionTeamEntity findActualById(Long id) {
        return query().selectFrom(meta)
                .where(meta.id.eq(id).and(meta.dateTo.isNull()))
                .fetchFirst();
    }

	public Long findIdByExternalId(String externalId) {
		return query().from(meta)
			.select(meta.id)
			.where(meta.externalId.eq(externalId))
			.fetchFirst();
	}

    public DivisionTeamEntity findByHeadEmployeeId(Long employeeId) {
        QDivisionTeamAssignmentEntity divisionTeamAssignment = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamRoleEntity divisionTeamRole = QDivisionTeamRoleEntity.divisionTeamRoleEntity;

        BooleanExpression filter = Expressions.allOf(
            divisionTeamAssignment.employeeId.eq(employeeId),
            divisionTeamRole.roleId.eq(RoleId.HEAD_TEAM),
            meta.dateTo.isNull(),
            divisionTeamAssignment.dateTo.isNull()
        );

        return query().selectFrom(meta)
            .join(divisionTeamRole).on(divisionTeamRole.divisionTeamId.eq(meta.id))
            .join(divisionTeamAssignment).on(divisionTeamAssignment.divisionTeamRole.id.eq(divisionTeamRole.id))
            .where(filter)
            .fetchOne();
    }

    public List<Long> findAllChildrenIds(Collection<Long> parentIds) {
        return query().select(meta.id)
                .from(meta)
                .distinct()
                .where(meta.parentId.in(parentIds))
                .fetch();
    }
}
