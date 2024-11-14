package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.BusinessProcessStatusEntity;

@Repository
public class BusinessProcessStatusDao extends AbstractDao<BusinessProcessStatusEntity, Integer> {

    public BusinessProcessStatusDao(EntityManager em) {
        super(BusinessProcessStatusEntity.class, em);
    }
}
