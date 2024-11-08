package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamSuccessorReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionTeamSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionTeamSuccessorReadinessEntity;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class DivisionTeamSuccessorReadinessDao extends AbstractDao<DivisionTeamSuccessorReadinessEntity, Long> {

    private static final QDivisionTeamSuccessorReadinessEntity meta = QDivisionTeamSuccessorReadinessEntity.divisionTeamSuccessorReadinessEntity;

    public DivisionTeamSuccessorReadinessDao(EntityManager em) {
        super(DivisionTeamSuccessorReadinessEntity.class, em);
    }

    public Map<Long, List<DivisionTeamSuccessorReadinessEntity>> findActualByDivisionTeamSuccessorIds(List<Long> divisionTeamSuccessorIds) {
        final BooleanExpression exp = meta.dateTo.isNull()
                .and(meta.divisionTeamSuccessor.id.in(divisionTeamSuccessorIds));

        return query()
                .from(meta)
                .select(meta.divisionTeamSuccessor.id, meta)
                .leftJoin(meta.divisionTeamSuccessor, QDivisionTeamSuccessorEntity.divisionTeamSuccessorEntity).fetchJoin()
                .where(exp)
                .fetch()
                .stream()
                .collect(groupingBy(t -> t.get(meta.divisionTeamSuccessor.id), mapping(t -> t.get(meta), toList())));
    }

    public List<DivisionTeamSuccessorReadinessEntity> findByDivisionTeamSuccessorId(Long divisionTeamSuccessorId) {
        final BooleanExpression exp = Expressions.allOf(
                meta.dateTo.isNull(),
                meta.divisionTeamSuccessor.id.eq(divisionTeamSuccessorId)
        );

        return query().selectFrom(meta)
                .where(exp)
                .fetch();
    }
}
