package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.goodt.drive.rtcore.api.orgstructure.filter.UnitFilter;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QUnitEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.UnitEntity;

@Repository
public class UnitDao extends AbstractDao<UnitEntity, String> {

    private static final QUnitEntity meta = QUnitEntity.unitEntity;

    public UnitDao(EntityManager em) {
        super(UnitEntity.class, em);
    }

    public List<UnitEntity> find(UnitFilter filter) {
        BooleanBuilder where = toPredicate(filter);

        return findAll(where);
    }

    public boolean exists(UnitFilter filter) {
        BooleanBuilder where = toPredicate(filter);

        return exists(where);
    }

    private BooleanBuilder toPredicate(UnitFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (CollectionUtils.isNotEmpty(filter.getCodes())) {
            where.and(meta.code.in(filter.getCodes()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }

        return where;
    }

}
