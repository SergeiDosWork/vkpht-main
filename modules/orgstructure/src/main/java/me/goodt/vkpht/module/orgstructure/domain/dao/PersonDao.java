package me.goodt.vkpht.module.orgstructure.domain.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QEmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPersonEntity;

@Repository
public class PersonDao extends AbstractDao<PersonEntity, Long> {

    private static final QPersonEntity meta = QPersonEntity.personEntity;
    private static final QEmployeeEntity employee = QEmployeeEntity.employeeEntity;

    public PersonDao(EntityManager em) {
        super(PersonEntity.class, em);
    }

    public Map<Long, PersonEntity> findByEmployeeIds(List<Long> employeeIds) {
        return query().from(employee).select(employee.id, meta)
            .join(employee.person, meta)
            .where(employee.id.in(employeeIds))
            .fetch()
            .stream()
            .collect(Collectors.toMap(t -> t.get(employee.id), t -> t.get(meta)));
    }

    public PersonEntity findByEmployeeId(Long employeeId) {
        return query().from(employee).select(meta)
            .join(employee.person, meta)
            .where(employee.id.eq(employeeId))
            .fetchFirst();
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }
}
