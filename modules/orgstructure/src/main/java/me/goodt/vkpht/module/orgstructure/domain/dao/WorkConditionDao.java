package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkConditionEntity;

@Repository
public class WorkConditionDao extends AbstractDao<WorkConditionEntity, Long> {

    public WorkConditionDao(EntityManager em) {
        super(WorkConditionEntity.class, em);
    }
}
