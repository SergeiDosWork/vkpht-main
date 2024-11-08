package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PrivilegeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PrivilegeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPrivilegeEntity;

@Repository
public class PrivilegeDao extends AbstractDao<PrivilegeEntity, Long> {

    private static final QPrivilegeEntity meta = QPrivilegeEntity.privilegeEntity;

    public PrivilegeDao(EntityManager em) {
        super(PrivilegeEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public Page<PrivilegeEntity> find(PrivilegeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(PrivilegeFilter filter) {
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

    public PrivilegeEntity findByIdAndUnitCode(Long id, String unitCode) {
        final BooleanExpression exp = meta.id.eq(id)
            .and(meta.unitCode.eq(unitCode))
            .and(meta.dateTo.isNull());
        return query().from(meta)
            .select(meta)
            .where(exp)
            .fetchOne();
    }
}
