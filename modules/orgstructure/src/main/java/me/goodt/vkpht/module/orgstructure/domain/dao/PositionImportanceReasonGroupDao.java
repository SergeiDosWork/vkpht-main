package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceReasonGroupEntity;

@Repository
public class PositionImportanceReasonGroupDao extends AbstractDao<PositionImportanceReasonGroupEntity, Long> {

    public PositionImportanceReasonGroupDao(EntityManager em) {
        super(PositionImportanceReasonGroupEntity.class, em);
    }
}
