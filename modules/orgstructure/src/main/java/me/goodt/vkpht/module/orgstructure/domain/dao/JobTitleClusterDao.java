package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleClusterEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QJobTitleClusterEntity;

@Repository
public class JobTitleClusterDao extends AbstractDao<JobTitleClusterEntity, Long> {

    private static final QJobTitleClusterEntity meta = QJobTitleClusterEntity.jobTitleClusterEntity;

    public JobTitleClusterDao(EntityManager em) {
        super(JobTitleClusterEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
