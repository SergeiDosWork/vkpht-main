package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkAwardTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkAwardTypeEntity;

@Repository
public class WorkAwardTypeDao extends AbstractDao<WorkAwardTypeEntity, Long> {

    private static final QWorkAwardTypeEntity meta = QWorkAwardTypeEntity.workAwardTypeEntity;

    public WorkAwardTypeDao(EntityManager em) {
        super(WorkAwardTypeEntity.class, em);
    }

    public Page<WorkAwardTypeEntity> findAllByUnitCode(String unitCode, Pageable pageable) {
        var where = meta.unitCode.eq(unitCode);
        return findAll(where, pageable);
    }
}
