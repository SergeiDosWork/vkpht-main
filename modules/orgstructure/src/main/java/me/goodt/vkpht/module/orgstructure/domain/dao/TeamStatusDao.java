package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.QTeamStatusEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.TeamStatusEntity;

@Repository
public class TeamStatusDao extends AbstractArchivableDao<TeamStatusEntity, Integer> {

    private static final QTeamStatusEntity meta = QTeamStatusEntity.teamStatusEntity;

    public TeamStatusDao(EntityManager em) {
        super(TeamStatusEntity.class, em);
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
    protected JPQLQuery<TeamStatusEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
