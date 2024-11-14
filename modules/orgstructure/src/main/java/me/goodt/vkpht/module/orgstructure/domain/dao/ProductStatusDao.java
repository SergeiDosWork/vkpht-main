package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ProductStatusEntity;

@Repository
public class ProductStatusDao extends AbstractDao<ProductStatusEntity, Integer> {

    public ProductStatusDao(EntityManager em) {
        super(ProductStatusEntity.class, em);
    }
}
