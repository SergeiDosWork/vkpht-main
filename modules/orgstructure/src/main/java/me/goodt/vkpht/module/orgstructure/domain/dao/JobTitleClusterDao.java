package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

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
