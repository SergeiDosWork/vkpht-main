package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkExperienceEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceEntity;

@Repository
public class WorkExperienceDao extends AbstractDao<WorkExperienceEntity, Long> {

    private static final QWorkExperienceEntity meta = QWorkExperienceEntity.workExperienceEntity;

    public WorkExperienceDao(EntityManager em) {
        super(WorkExperienceEntity.class, em);
    }
}
