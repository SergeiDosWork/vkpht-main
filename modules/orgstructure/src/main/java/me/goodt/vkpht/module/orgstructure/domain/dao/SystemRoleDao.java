package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QSystemRoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.SystemRoleEntity;

@Repository
public class SystemRoleDao extends AbstractArchivableDao<SystemRoleEntity, Integer> {

    private static final QSystemRoleEntity meta = QSystemRoleEntity.systemRoleEntity;

    public SystemRoleDao(EntityManager em) {
        super(SystemRoleEntity.class, em);
    }

    public boolean existsByName(String name) {
        return query().selectFrom(meta)
            .where(meta.name.eq(name))
            .fetchCount() > 0;
    }

    public Integer findIdByExternalId(String externalId) {
        return query().from(meta)
            .select(meta.id)
            .where(meta.externalId.eq(externalId))
            .fetchFirst();
    }

    @Override
    protected JPQLQuery<SystemRoleEntity> createActualQuery() {
        return query().selectFrom(meta)
            .where(meta.dateTo.isNull());
    }
}
