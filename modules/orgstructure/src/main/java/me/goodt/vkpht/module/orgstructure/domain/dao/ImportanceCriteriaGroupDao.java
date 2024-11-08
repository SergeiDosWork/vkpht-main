package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.module.orgstructure.domain.entity.QImportanceCriteriaGroupEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupEntity;

@Repository
public class ImportanceCriteriaGroupDao extends AbstractDao<ImportanceCriteriaGroupEntity, Long> {
    private final QImportanceCriteriaGroupEntity meta = QImportanceCriteriaGroupEntity.importanceCriteriaGroupEntity;

    public ImportanceCriteriaGroupDao(EntityManager em) {
        super(ImportanceCriteriaGroupEntity.class, em);
    }

    public Page<ImportanceCriteriaGroupEntity> find(String unitCode, Pageable pageable) {
        BooleanExpression where = meta.unitCode.eq(unitCode);
        return findAll(where, pageable);
    }
}
