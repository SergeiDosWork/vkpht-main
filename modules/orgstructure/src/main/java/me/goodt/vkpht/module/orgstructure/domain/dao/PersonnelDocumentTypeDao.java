package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonnelDocumentTypeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPersonnelDocumentTypeEntity;

@Repository
public class PersonnelDocumentTypeDao extends AbstractDao<PersonnelDocumentTypeEntity, Integer> {

    private static final QPersonnelDocumentTypeEntity meta = QPersonnelDocumentTypeEntity.personnelDocumentTypeEntity;

    public PersonnelDocumentTypeDao(EntityManager em) {
        super(PersonnelDocumentTypeEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
            .where(meta.name.eq(name))
            .fetchCount() > 0;
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    public Page<PersonnelDocumentTypeEntity> find(PersonnelDocumentTypeFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(PersonnelDocumentTypeFilter filter) {
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
