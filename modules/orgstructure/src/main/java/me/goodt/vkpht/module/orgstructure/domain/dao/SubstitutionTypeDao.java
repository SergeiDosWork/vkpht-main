package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.SubstitutionTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.QSubstitutionTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.SubstitutionTypeEntity;

@Repository
public class SubstitutionTypeDao extends AbstractDao<SubstitutionTypeEntity, Integer> {

    private static final QSubstitutionTypeEntity meta = QSubstitutionTypeEntity.substitutionTypeEntity;

    public SubstitutionTypeDao(EntityManager em) {
        super(SubstitutionTypeEntity.class, em);
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<SubstitutionTypeEntity> find(SubstitutionTypeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);
        return findAll(where, pageable);
    }

    private Predicate toPredicate(SubstitutionTypeFilter filter) {
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
