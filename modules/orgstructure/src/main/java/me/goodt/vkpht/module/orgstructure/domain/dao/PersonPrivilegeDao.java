package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PersonPrivilegeFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonPrivilegeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPersonPrivilegeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QPrivilegeEntity;

@Repository
public class PersonPrivilegeDao extends AbstractDao<PersonPrivilegeEntity, Long> {

    private static final QPersonPrivilegeEntity meta = QPersonPrivilegeEntity.personPrivilegeEntity;
    private static final QPrivilegeEntity privilege = QPrivilegeEntity.privilegeEntity;

    public PersonPrivilegeDao(EntityManager em) {
        super(PersonPrivilegeEntity.class, em);
    }

    public Page<PersonPrivilegeEntity> find(PersonPrivilegeFilter filter, Pageable pageable) {
        JPQLQuery<PersonPrivilegeEntity> entityJPQLQuery = query()
            .selectFrom(meta)
            .join(privilege).on(meta.privilege.id.eq(privilege.id))
            .where(privilege.unitCode.eq(filter.getUnitCode()).and(meta.dateTo.isNull()));
        JPQLQuery<PersonPrivilegeEntity> pagedQuery = new Querydsl(em,
            new PathBuilderFactory().create(PersonPrivilegeEntity.class))
            .applyPagination(pageable, entityJPQLQuery);
        List<PersonPrivilegeEntity> content = pagedQuery.fetch();
        return new PageImpl<>(content, pageable, entityJPQLQuery.fetchCount());
    }

    public PersonPrivilegeEntity findByIdAndUnitCode(Long id, String unitCode) {
        final BooleanExpression exp = meta.id.eq(id)
            .and(privilege.unitCode.eq(unitCode))
            .and(meta.dateTo.isNull());
        return query().from(meta)
            .join(privilege).on(meta.privilege.id.eq(privilege.id))
            .select(meta)
            .where(exp)
            .fetchOne();
    }
}