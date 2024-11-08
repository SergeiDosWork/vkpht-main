package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CostCenterEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QCostCenterEntity;

@Repository
public class CostCenterDao extends AbstractArchivableDao<CostCenterEntity, Long> {

    private static final QCostCenterEntity meta = QCostCenterEntity.costCenterEntity;

    public CostCenterDao(EntityManager em) {
        super(CostCenterEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    @Override
    protected JPQLQuery<CostCenterEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
