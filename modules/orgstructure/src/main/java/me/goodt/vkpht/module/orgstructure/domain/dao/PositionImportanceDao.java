package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionImportanceFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionImportanceEntity;

@Repository
public class PositionImportanceDao extends AbstractDao<PositionImportanceEntity, Integer> {

    private static final QPositionImportanceEntity meta = QPositionImportanceEntity.positionImportanceEntity;

    public PositionImportanceDao(EntityManager em) {
        super(PositionImportanceEntity.class, em);
    }

    public Optional<PositionImportanceEntity> findActualById(Integer id) {
        return Optional.ofNullable(query().selectFrom(meta)
            .where(meta.id.eq(id).and(meta.dateTo.isNull()))
            .fetchFirst());
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public Page<PositionImportanceEntity> find(PositionImportanceFilter filter, Pageable pageable) {
        Predicate exp = toPredicate(filter);
        return findAll(exp, pageable);
    }

    public List<PositionImportanceEntity> findAll(PositionImportanceFilter filter) {
        Predicate exp = toPredicate(filter);
        return findAll(exp);
    }

    private Predicate toPredicate(PositionImportanceFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }
        return where;
    }
}
