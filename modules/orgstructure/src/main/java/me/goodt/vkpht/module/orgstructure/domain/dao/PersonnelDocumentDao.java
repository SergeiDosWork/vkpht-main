package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonnelDocumentFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPersonnelDocumentEntity;

@Repository
public class PersonnelDocumentDao extends AbstractDao<PersonnelDocumentEntity, Long> {

    private static final QPersonnelDocumentEntity meta = QPersonnelDocumentEntity.personnelDocumentEntity;

    public PersonnelDocumentDao(EntityManager em) {
        super(PersonnelDocumentEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public Page<PersonnelDocumentEntity> find(PersonnelDocumentFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(PersonnelDocumentFilter filter) {
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
