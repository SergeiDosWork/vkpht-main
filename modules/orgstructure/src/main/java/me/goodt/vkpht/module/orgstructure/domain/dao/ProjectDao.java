package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectEntity;

@Repository
public class ProjectDao extends AbstractDao<ProjectEntity, Long> {

    private static final QProjectEntity meta = QProjectEntity.projectEntity;

    public ProjectDao(EntityManager em) {
        super(ProjectEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }
}
