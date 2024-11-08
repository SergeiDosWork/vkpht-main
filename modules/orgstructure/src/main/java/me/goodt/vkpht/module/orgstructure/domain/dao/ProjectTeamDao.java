package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamEntity;

@Repository
public class ProjectTeamDao extends AbstractDao<ProjectTeamEntity, Long> {

    private static final QProjectTeamEntity meta = QProjectTeamEntity.projectTeamEntity;

    public ProjectTeamDao(EntityManager em) {
        super(ProjectTeamEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
