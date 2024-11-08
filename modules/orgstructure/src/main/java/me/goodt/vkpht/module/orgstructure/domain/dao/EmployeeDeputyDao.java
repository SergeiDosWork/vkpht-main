package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDeputyEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QEmployeeDeputyEntity;

@Repository
public class EmployeeDeputyDao extends AbstractDao<EmployeeDeputyEntity, Long> {

    private static final QEmployeeDeputyEntity meta = QEmployeeDeputyEntity.employeeDeputyEntity;

    public EmployeeDeputyDao(EntityManager em) {
        super(EmployeeDeputyEntity.class, em);
    }

    public Long findAllActualByEmployees(Long substituteId, Long viceId) {
        BooleanExpression exp = Expressions.allOf(
                meta.employeeSubstituteId.eq(substituteId),
                meta.employeeViceId.eq(viceId),
                meta.dateTo.isNull()
        );
        return query().selectFrom(meta)
                .where(exp)
                .fetchCount();
    }

    public List<Long> findActualEmployeeViceByEmployees(Long substituteId) {
        BooleanExpression exp = Expressions.allOf(
                meta.employeeSubstituteId.eq(substituteId),
                meta.dateTo.isNull()
        );
        return query().select(meta.employeeViceId)
                .from(meta)
                .where(exp)
                .fetch();
    }

    public Page<EmployeeDeputyEntity> findAllByParams(Long employeeId, Date date, Pageable pageable) {
        BooleanExpression exp = meta.dateTo.isNull();
        if (employeeId != null) {
            exp = exp.and(
                    Expressions.anyOf(
                            meta.employeeSubstituteId.eq(employeeId),
                            meta.employeeViceId.eq(employeeId)
                    )
            );
        }
        if (date != null) {
            exp = exp.and(
                    Expressions.anyOf(
                            Expressions.dateOperation(Date.class, Ops.DateTimeOps.DATE, meta.dateFrom).eq(date),
                            Expressions.dateOperation(Date.class, Ops.DateTimeOps.DATE, meta.dateTo).eq(date)
                    )
            );
        }
        JPQLQuery<EmployeeDeputyEntity> select = query().selectFrom(meta)
                .where(exp);
        JPQLQuery<EmployeeDeputyEntity> pagedQuery = new Querydsl(em, new PathBuilderFactory().create(EmployeeDeputyEntity.class))
                .applyPagination(pageable, select);
        List<EmployeeDeputyEntity> content = pagedQuery.fetch();
        return new PageImpl<>(content, pageable, select.fetchCount());
    }
}
