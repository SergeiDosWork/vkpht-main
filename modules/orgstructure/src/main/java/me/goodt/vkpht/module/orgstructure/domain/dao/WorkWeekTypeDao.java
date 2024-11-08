package me.goodt.vkpht.module.orgstructure.domain.dao;

import me.goodt.vkpht.module.orgstructure.domain.entity.WorkWeekTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QWorkWeekTypeEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class WorkWeekTypeDao extends AbstractDao<WorkWeekTypeEntity, Integer> {

    private static final QWorkWeekTypeEntity meta = QWorkWeekTypeEntity.workWeekTypeEntity;

    public WorkWeekTypeDao(EntityManager em) {
        super(WorkWeekTypeEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
                .where(meta.name.eq(name))
                .fetchCount() > 0;
    }

    public Page<WorkWeekTypeEntity> findAllByUnitCode(String unitCode, Pageable pageable) {
        var where = meta.unitCode.eq(unitCode);
        return findAll(where, pageable);
    }
}
