package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;

@Repository
public class CalculationMethodDao extends AbstractDao<CalculationMethodEntity, Long> {
    public CalculationMethodDao(EntityManager em) {
        super(CalculationMethodEntity.class, em);
    }
}
