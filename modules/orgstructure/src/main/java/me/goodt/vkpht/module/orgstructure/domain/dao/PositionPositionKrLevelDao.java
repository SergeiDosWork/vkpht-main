package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionKrLevelEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.PositionPositionKrLevelId;

@Repository
public class PositionPositionKrLevelDao extends AbstractDao<PositionPositionKrLevelEntity, PositionPositionKrLevelId> {

    public PositionPositionKrLevelDao(EntityManager em) {
        super(PositionPositionKrLevelEntity.class, em);
    }
}
