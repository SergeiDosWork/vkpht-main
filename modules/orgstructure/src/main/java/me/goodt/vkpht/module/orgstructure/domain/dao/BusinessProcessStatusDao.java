package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.BusinessProcessStatusEntity;

@Repository
public class BusinessProcessStatusDao extends AbstractDao<BusinessProcessStatusEntity, Integer> {

    public BusinessProcessStatusDao(EntityManager em) {
        super(BusinessProcessStatusEntity.class, em);
    }
}
