package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.WorkExperienceTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkExperienceTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;

@Repository
public class WorkExperienceTypeDao extends AbstractDao<WorkExperienceTypeEntity, Long> {

    private static final QWorkExperienceTypeEntity meta = QWorkExperienceTypeEntity.workExperienceTypeEntity;

    public WorkExperienceTypeDao(EntityManager em) {
        super(WorkExperienceTypeEntity.class, em);
    }

    public Long findIdByExternalId(String extId) {
        return query().select(meta.id)
            .from(meta)
            .where(meta.externalId.eq(extId))
            .fetchFirst();
    }

    public Page<WorkExperienceTypeEntity> find(WorkExperienceTypeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(WorkExperienceTypeFilter filter) {
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
