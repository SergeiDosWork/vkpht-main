package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.WorkAwardEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkAwardEntity;

@Repository
public class WorkAwardDao extends AbstractDao<WorkAwardEntity, Long> {

    private static final QWorkAwardEntity meta = QWorkAwardEntity.workAwardEntity;

    public WorkAwardDao(EntityManager em) {
        super(WorkAwardEntity.class, em);
    }
}
