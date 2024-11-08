package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.PersonDisabilityEntity;

@Repository
public class PersonDisabilityDao extends AbstractDao<PersonDisabilityEntity, Long> {

    public PersonDisabilityDao(EntityManager em) {
        super(PersonDisabilityEntity.class, em);
    }
}
