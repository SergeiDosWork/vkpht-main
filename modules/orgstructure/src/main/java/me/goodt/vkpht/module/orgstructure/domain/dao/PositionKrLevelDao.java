package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionKrLevelFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionPositionKrLevelEntity;

@Repository
public class PositionKrLevelDao extends AbstractDao<PositionKrLevelEntity, Long> {

    private static final QPositionKrLevelEntity meta = QPositionKrLevelEntity.positionKrLevelEntity;

    public PositionKrLevelDao(EntityManager em) {
        super(PositionKrLevelEntity.class, em);
    }

    public List<PositionKrLevelEntity> getActualPositionKrLevelByUnitCodeAndPositionId(String unitCode, Long positionId) {
        QPositionPositionKrLevelEntity ppkl = QPositionPositionKrLevelEntity.positionPositionKrLevelEntity;
        BooleanExpression exp = ppkl.id.position.id.eq(positionId)
            .and(meta.unitCode.eq(unitCode))
            .and(ppkl.dateTo.isNull());
        return query().selectFrom(meta)
            .join(ppkl).on(meta.id.eq(ppkl.id.positionKrLevel.id))
            .where(exp)
            .fetch();
    }

    public List<PositionKrLevelEntity> findAllByUnitCode(String unitCode) {
        BooleanExpression exp = meta.unitCode.eq(unitCode);
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public Page<PositionKrLevelEntity> find(PositionKrLevelFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    public Page<PositionKrLevelEntity> findAllByUnitCode(String unitCode, Pageable pageable) {
        var where = meta.unitCode.eq(unitCode);
        return findAll(where, pageable);
    }

    private Predicate toPredicate(PositionKrLevelFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        return where;
    }
}
