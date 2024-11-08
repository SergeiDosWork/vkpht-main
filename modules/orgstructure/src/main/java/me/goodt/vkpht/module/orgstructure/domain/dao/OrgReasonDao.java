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
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.ReasonFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QOrgReasonEntity;

@Repository
public class OrgReasonDao extends AbstractDao<OrgReasonEntity, Long> {

    public static final QOrgReasonEntity meta = QOrgReasonEntity.orgReasonEntity;

    public OrgReasonDao(EntityManager em) {
        super(OrgReasonEntity.class, em);
    }

    public Page<OrgReasonEntity> find(ReasonFilter filter, Pageable pageable) {
        Predicate exp = toPredicate(filter);
        return findAll(exp, pageable);
    }

    public List<OrgReasonEntity> findAll(ReasonFilter filter) {
        Predicate exp = toPredicate(filter);
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public Optional<OrgReasonEntity> findOne(Long id, ReasonFilter filter) {
        Predicate exp = toPredicate(filter);
        return Optional.ofNullable(query().selectFrom(meta)
                .where(meta.id.eq(id).and(exp))
                .fetchFirst());
    }

    private Predicate toPredicate(ReasonFilter filter) {
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
