package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkAwardEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkAwardEntity;

@Repository
public class WorkAwardDao extends AbstractDao<WorkAwardEntity, Long> {

    private static final QWorkAwardEntity meta = QWorkAwardEntity.workAwardEntity;

    public WorkAwardDao(EntityManager em) {
        super(WorkAwardEntity.class, em);
    }
}
