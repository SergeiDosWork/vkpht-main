package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipIntegrationEntity;

@Repository
public class CitizenshipIntegrationDao extends AbstractDao<CitizenshipIntegrationEntity, Long> {

    public CitizenshipIntegrationDao(EntityManager em) {
        super(CitizenshipIntegrationEntity.class, em);
    }
}
