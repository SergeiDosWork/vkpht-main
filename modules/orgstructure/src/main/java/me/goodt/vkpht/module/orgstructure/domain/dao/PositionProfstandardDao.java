package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionProfstandardEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionProfstandardEntity;

@Repository
public class PositionProfstandardDao extends AbstractDao<PositionProfstandardEntity, Long> {

    private static final QPositionProfstandardEntity meta = QPositionProfstandardEntity.positionProfstandardEntity;

    public PositionProfstandardDao(EntityManager em) {
        super(PositionProfstandardEntity.class, em);
    }
}
