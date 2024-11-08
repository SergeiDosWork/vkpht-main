package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.WorkConditionEntity;

@Repository
public class WorkConditionDao extends AbstractDao<WorkConditionEntity, Long> {

    public WorkConditionDao(EntityManager em) {
        super(WorkConditionEntity.class, em);
    }
}
