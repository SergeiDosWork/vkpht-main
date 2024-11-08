package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionProfstandardEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPositionProfstandardEntity;

@Repository
public class PositionProfstandardDao extends AbstractDao<PositionProfstandardEntity, Long> {

    private static final QPositionProfstandardEntity meta = QPositionProfstandardEntity.positionProfstandardEntity;

    public PositionProfstandardDao(EntityManager em) {
        super(PositionProfstandardEntity.class, em);
    }
}
