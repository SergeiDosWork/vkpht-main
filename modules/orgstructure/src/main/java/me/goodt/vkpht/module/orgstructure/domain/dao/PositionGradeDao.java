package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionGradeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionGradeLink;

@Repository
public class PositionGradeDao extends AbstractDao<PositionGradeEntity, Long> {

    private static final QPositionGradeEntity meta = QPositionGradeEntity.positionGradeEntity;

    public PositionGradeDao(EntityManager em) {
        super(PositionGradeEntity.class, em);
    }

    // SELECT pg.* FROM org_position_grade AS pg
    // INNER JOIN org_position_position_grade AS ppg ON pg.id = ppg.position_grade_id
    // WHERE ppg.position_id = :positionId AND (ppg.date_to IS NULL OR CURRENT_TIMESTAMP BETWEEN ppg.date_from AND ppg.date_to)
    public List<PositionGradeEntity> findActualByPosition(Long positionId) {
        final QPositionGradeLink pgl = QPositionGradeLink.positionGradeLink;
        BooleanExpression exp = pgl.positionId.eq(positionId)
                .and(pgl.dateTo.isNull());
        return query().selectFrom(meta)
                .join(pgl).on(pgl.positionGradeId.eq(meta.id))
                .where(exp)
                .fetch();
    }

    public Page<PositionGradeEntity> find(PositionGradeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    public List<PositionGradeEntity> find(PositionGradeFilter filter) {
        Predicate where = toPredicate(filter);

        return findAll(where);
    }

    private Predicate toPredicate(PositionGradeFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }

        return where;
    }
}
