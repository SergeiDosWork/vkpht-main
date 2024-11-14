package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonDisabilityEntity;

@Repository
public class PersonDisabilityDao extends AbstractDao<PersonDisabilityEntity, Long> {

    public PersonDisabilityDao(EntityManager em) {
        super(PersonDisabilityEntity.class, em);
    }
}
