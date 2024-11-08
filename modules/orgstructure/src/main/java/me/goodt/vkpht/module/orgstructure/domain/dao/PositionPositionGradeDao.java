package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionPositionGradeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.PositionPositionGradeId;

@Repository
public class PositionPositionGradeDao extends AbstractDao<PositionPositionGradeEntity, PositionPositionGradeId> {

    public PositionPositionGradeDao(EntityManager em) {
        super(PositionPositionGradeEntity.class, em);
    }
}
