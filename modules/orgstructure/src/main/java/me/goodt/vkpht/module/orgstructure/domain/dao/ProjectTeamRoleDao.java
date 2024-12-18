package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProjectTeamRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QProjectTeamRoleEntity;

@Repository
public class ProjectTeamRoleDao extends AbstractDao<ProjectTeamRoleEntity, Long> {

    private static final QProjectTeamRoleEntity meta = QProjectTeamRoleEntity.projectTeamRoleEntity;

    public ProjectTeamRoleDao(EntityManager em) {
        super(ProjectTeamRoleEntity.class, em);
    }

    public Long findIdByExternalId(String extId) {
        return query().select(meta.id)
            .from(meta)
            .where(meta.externalId.eq(extId))
            .fetchFirst();
    }
}
