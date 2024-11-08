package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionLegalDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.DivisionFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;

@Repository
public class DivisionDao extends AbstractDao<DivisionEntity, Long> {

    private static final QDivisionEntity meta = QDivisionEntity.divisionEntity;
    private static final QLegalEntityEntity l = QLegalEntityEntity.legalEntityEntity;

    public DivisionDao(EntityManager em) {
        super(DivisionEntity.class, em);
    }

    public List<DivisionEntity> findAll(DivisionFilter filter) {
        return toQuery(filter)
            .fetch();
    }

    public DivisionEntity find(DivisionFilter filter) {
        return toQuery(filter)
            .fetchFirst();
    }

    private JPQLQuery<DivisionEntity> toQuery(DivisionFilter filter) {
        QDivisionTeamEntity qdte = QDivisionTeamEntity.divisionTeamEntity;
        QPositionEntity p = QPositionEntity.positionEntity;
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(l.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getDivisionTeamId() != null) {
            where.and(qdte.id.eq(filter.getDivisionTeamId()));
        }
        if (filter.getLegalEntityId() != null) {
            where.and(meta.legalEntityId.eq(filter.getLegalEntityId()));
        }
        if (filter.getParentIds() != null) {
            where.and(meta.parentId.in(filter.getParentIds()));
        }
        if (filter.getPositionId() != null) {
            where.and(p.id.eq(filter.getPositionId()));
        }
        if (filter.getDivisionIds() != null && !filter.getDivisionIds().isEmpty()) {
            where.and(meta.id.in(filter.getDivisionIds()));
        }
        if (filter.getGroupIds() != null && !filter.getGroupIds().isEmpty()) {
            where.and(meta.groupId.in(filter.getGroupIds()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }
        return query().selectFrom(meta)
            .where(where)
            .innerJoin(l).on(meta.legalEntityId.eq(l.id))
            .leftJoin(p).on(p.division.id.eq(meta.id));
    }

    public Long findLegalEntityIdByPositionId(Long positionId, String unitCode) {
        final QPositionEntity p = QPositionEntity.positionEntity;
        BooleanExpression exp = p.id.eq(positionId)
            .and(l.unitCode.eq(unitCode));
        return query()
            .select(meta.legalEntityEntity.id)
                .from(meta)
                .join(p).on(p.division.id.eq(meta.id))
                .innerJoin(l).on(meta.legalEntityId.eq(l.id))
                .where(exp)
                .fetchFirst();
    }

    public Long findByDivisionTeamAssignmentRotation(Long divisionTeamAssignmentRotationId, String unitCode) {
        QDivisionTeamAssignmentRotationEntity qrot = QDivisionTeamAssignmentRotationEntity.divisionTeamAssignmentRotationEntity;
        QDivisionTeamAssignmentShort qta = QDivisionTeamAssignmentShort.divisionTeamAssignmentShort;
        QDivisionTeamRoleEntity qtre = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamEntity qdte = QDivisionTeamEntity.divisionTeamEntity;
        // divisionTeamAssignmentRotation.getAssignment().getDivisionTeamRole().getDivisionTeam().getDivision()
        BooleanExpression exp = qrot.id.eq(divisionTeamAssignmentRotationId)
            .and(l.unitCode.eq(unitCode));
        return query().from(qrot)
                .join(qrot.assignment, qta)
                .join(qtre).on(qta.divisionTeamRoleId.eq(qtre.id))
                .join(qtre.divisionTeam, qdte)
                .innerJoin(l).on(meta.legalEntityId.eq(l.id))
                .select(qdte.divisionId)
                .where(exp)
                .fetchFirst();
    }

    public Map<Long, DivisionEntity> findActualByDivisionTeamAssignments(Collection<Long> dtaIds, String unitCode) {
        QDivisionTeamAssignmentEntity qdta = QDivisionTeamAssignmentEntity.divisionTeamAssignmentEntity;
        QDivisionTeamRoleEntity qdtr = QDivisionTeamRoleEntity.divisionTeamRoleEntity;
        QDivisionTeamEntity qdt = QDivisionTeamEntity.divisionTeamEntity;
        QDivisionEntity qd = QDivisionEntity.divisionEntity;

        return query().from(qdta)
                .join(qdta.divisionTeamRole, qdtr)
                .join(qdtr.divisionTeam, qdt)
                .join(qdt.division, qd)
                .innerJoin(l).on(meta.legalEntityId.eq(l.id))
                .select(qdta.id, qd)
                .where(qdta.id.in(dtaIds)
                    .and(qd.dateTo.isNull())
                    .and(qdt.dateTo.isNull())
                    .and(l.unitCode.eq(unitCode))
                )
                .fetch()
                .stream()
                .collect(Collectors.toMap(t -> t.get(qdta.id), t -> t.get(qd)));
    }

    public List<DivisionLegalDto> findDivisionLegal(List<Long> divisionIds, String unitCode) {
        return query().from(meta)
                .where(meta.id.in(divisionIds).and(l.unitCode.eq(unitCode)))
                .select(Projections.bean(DivisionLegalDto.class, meta.id.as("divisionId"), meta.legalEntityId.as("legalEntityId")))
                .fetch();
    }

    public List<DivisionEntity> findPost(List<Long> ids, List<Long> legalEntityIds, Integer page, Integer size, String searchingValue, Boolean withClosed, String unitCode) {
        JPQLQuery<?> jpqlQuery = query().from(meta);
        if (size != null && size > 0) {
            jpqlQuery.limit(size);
            if (page != null && page >= 0) {
                jpqlQuery.offset((long) size * page);

            }
        }

        BooleanExpression exp = Expressions.allOf(
                ids != null && !ids.isEmpty() ? meta.id.in(ids) : null,
                legalEntityIds != null && !legalEntityIds.isEmpty() ? meta.legalEntityId.in(legalEntityIds) : null,
                withClosed != null && withClosed ? null : meta.dateTo.isNull(),
                l.unitCode.eq(unitCode)
        );

        if (!StringUtils.isEmpty(searchingValue)) {
            String[] searchValue = searchingValue.toLowerCase().split(" ");
            for (String value : searchValue) {
                if (exp != null) {
                    exp = exp.and(meta.fullName.toLowerCase().like("%" + value + "%"));
                } else {
                    exp = meta.fullName.toLowerCase().like("%" + value + "%");
                }
            }
        }

        return jpqlQuery
                .select(meta)
                .where(exp)
                .innerJoin(l).on(meta.legalEntityId.eq(l.id))
                .fetch();
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
