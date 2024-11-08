package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkplaceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkplaceEntity;

@Repository
public class WorkplaceDao extends AbstractDao<WorkplaceEntity, Long> {

    private static final QWorkplaceEntity meta = QWorkplaceEntity.workplaceEntity;

    public WorkplaceDao(EntityManager em) {
        super(WorkplaceEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public List<WorkplaceEntity> findActualByIds(List<Long> ids, String unitCode) {
        return query().selectFrom(meta)
            .distinct()
            .where(meta.id.in(ids).and(meta.dateTo.isNull()).and(meta.unitCode.eq(unitCode)))
            .fetch();
    }
}
