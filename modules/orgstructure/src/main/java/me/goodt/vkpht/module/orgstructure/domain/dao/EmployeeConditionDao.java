package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeConditionEntity;

@Repository
public class EmployeeConditionDao extends AbstractDao<EmployeeConditionEntity, Long> {

    public EmployeeConditionDao(EntityManager em) {
        super(EmployeeConditionEntity.class, em);
    }
}
