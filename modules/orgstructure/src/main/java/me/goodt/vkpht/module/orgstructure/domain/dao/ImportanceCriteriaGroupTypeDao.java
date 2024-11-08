package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QImportanceCriteriaGroupTypeEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class ImportanceCriteriaGroupTypeDao extends AbstractDao<ImportanceCriteriaGroupTypeEntity, Long> {

    private static final QImportanceCriteriaGroupTypeEntity meta =
            QImportanceCriteriaGroupTypeEntity.importanceCriteriaGroupTypeEntity;

    public ImportanceCriteriaGroupTypeDao(EntityManager em) {
        super(ImportanceCriteriaGroupTypeEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
                .where(meta.name.eq(name))
                .fetchCount() > 0;
    }
}
