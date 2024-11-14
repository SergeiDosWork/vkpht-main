package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeConditionEntity;

@Repository
public class EmployeeConditionDao extends AbstractDao<EmployeeConditionEntity, Long> {

    public EmployeeConditionDao(EntityManager em) {
        super(EmployeeConditionEntity.class, em);
    }
}
