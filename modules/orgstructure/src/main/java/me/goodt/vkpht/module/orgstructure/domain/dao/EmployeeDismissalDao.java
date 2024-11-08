package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.dao.filter.EmployeeDismissalFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDismissalEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QEmployeeDismissalEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QEmployeeEntity;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class EmployeeDismissalDao extends AbstractDao<EmployeeDismissalEntity, Long> {
    private static final QEmployeeDismissalEntity meta = QEmployeeDismissalEntity.employeeDismissalEntity;

    public EmployeeDismissalDao(EntityManager em) {
        super(EmployeeDismissalEntity.class, em);
    }

    public List<EmployeeDismissalEntity> findAll(EmployeeDismissalFilter filter) {
        return toQuery(filter)
            .fetch();
    }

    public List<EmployeeDismissalEntity> findAllByEmployeeId(List<Long> employeeIds) {
        QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
        return query().selectFrom(meta)
            .where(meta.employee.id.in(employeeIds))
            .join(meta.employee, employee).fetchJoin()
            .fetch();
    }

    private JPQLQuery<EmployeeDismissalEntity> toQuery(EmployeeDismissalFilter filter) {
        JPQLQuery<EmployeeDismissalEntity> query = query().selectFrom(meta);
        BooleanBuilder where = new BooleanBuilder();
        QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getEmployeeId() != null) {
            where.and(meta.employee.id.eq(filter.getEmployeeId()));
            query = query.innerJoin(meta.employee, employee);
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }
        return query
            .where(where);
    }
}
