package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipIntegrationEntity;

@Repository
public class CitizenshipIntegrationDao extends AbstractDao<CitizenshipIntegrationEntity, Long> {

    public CitizenshipIntegrationDao(EntityManager em) {
        super(CitizenshipIntegrationEntity.class, em);
    }
}
