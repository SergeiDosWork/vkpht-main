package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;

@Repository
public class ImportanceCriteriaDao extends AbstractDao<ImportanceCriteriaEntity, Long> {

    private static final QImportanceCriteriaEntity meta = QImportanceCriteriaEntity.importanceCriteriaEntity;

    public ImportanceCriteriaDao(EntityManager em) {
        super(ImportanceCriteriaEntity.class, em);
    }

    public List<ImportanceCriteriaEntity> findAllEnabledWithCalcMethod() {
        return query().selectFrom(meta)
                .where(meta.calculationMethod.isNotNull().and(meta.isEnabled.isTrue()))
                .fetch();
    }

    public Page<ImportanceCriteriaEntity> find(String unitCode, Pageable pageable) {
        final QImportanceCriteriaGroupEntity g = QImportanceCriteriaGroupEntity.importanceCriteriaGroupEntity;
        JPQLQuery<ImportanceCriteriaEntity> select = query().selectFrom(meta)
            .join(g).on(meta.group.id.eq(g.id))
            .where(g.unitCode.eq(unitCode));
        JPQLQuery<ImportanceCriteriaEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(ImportanceCriteriaEntity.class))
            .applyPagination(pageable, select);
        List<ImportanceCriteriaEntity> content = pagedQuery.fetch();

        return new PageImpl<>(content, pageable, select.fetchCount());
    }

}
