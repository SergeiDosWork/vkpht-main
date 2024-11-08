package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.ProductStatusEntity;

@Repository
public class ProductStatusDao extends AbstractDao<ProductStatusEntity, Integer> {

    public ProductStatusDao(EntityManager em) {
        super(ProductStatusEntity.class, em);
    }
}
