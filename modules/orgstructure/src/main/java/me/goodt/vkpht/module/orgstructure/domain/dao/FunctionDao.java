package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.FunctionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QFunctionEntity;

@Repository
public class FunctionDao extends AbstractArchivableDao<FunctionEntity, Long> {

    private static final QFunctionEntity meta = QFunctionEntity.functionEntity;

    public FunctionDao(EntityManager em) {
        super(FunctionEntity.class, em);
    }

    @Override
    protected JPQLQuery<FunctionEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
