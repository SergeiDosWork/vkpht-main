package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QTeamTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.TeamTypeEntity;

@Repository
public class TeamTypeDao extends AbstractArchivableDao<TeamTypeEntity, Integer> {

    private static final QTeamTypeEntity meta = QTeamTypeEntity.teamTypeEntity;

    public TeamTypeDao(EntityManager em) {
        super(TeamTypeEntity.class, em);
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
    protected JPQLQuery<TeamTypeEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
