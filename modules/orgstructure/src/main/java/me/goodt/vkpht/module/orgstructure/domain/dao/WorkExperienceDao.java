package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkExperienceEntity;

@Repository
public class WorkExperienceDao extends AbstractDao<WorkExperienceEntity, Long> {

    private static final QWorkExperienceEntity meta = QWorkExperienceEntity.workExperienceEntity;

    public WorkExperienceDao(EntityManager em) {
        super(WorkExperienceEntity.class, em);
    }
}
