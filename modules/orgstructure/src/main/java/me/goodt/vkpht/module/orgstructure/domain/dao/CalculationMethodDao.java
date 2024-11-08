package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;

@Repository
public class CalculationMethodDao extends AbstractDao<CalculationMethodEntity, Long> {
    public CalculationMethodDao(EntityManager em) {
        super(CalculationMethodEntity.class, em);
    }
}
