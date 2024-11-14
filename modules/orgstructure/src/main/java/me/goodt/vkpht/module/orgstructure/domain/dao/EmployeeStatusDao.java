package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.EmployeeStatusFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QEmployeeStatusEntity;

@Repository
public class EmployeeStatusDao extends AbstractDao<EmployeeStatusEntity, Long> {

    private static final QEmployeeStatusEntity meta = QEmployeeStatusEntity.employeeStatusEntity;

    public EmployeeStatusDao(EntityManager em) {
        super(EmployeeStatusEntity.class, em);
    }

    public Long findIdByExternalId(String extId) {
        return query().select(meta.id)
            .from(meta)
            .where(meta.externalId.eq(extId))
            .fetchFirst();
    }

    public Page<EmployeeStatusEntity> find(EmployeeStatusFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(EmployeeStatusFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
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
