package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionGroupEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionGroupEntity;

@Repository
public class DivisionGroupDao extends AbstractArchivableDao<DivisionGroupEntity, Long> {

    private static final QDivisionGroupEntity meta = QDivisionGroupEntity.divisionGroupEntity;

    public DivisionGroupDao(EntityManager em) {
        super(DivisionGroupEntity.class, em);
    }

    public Long findIdByExternalId(String externalId) {
        return query().from(meta)
                .select(meta.id)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }

    public List<DivisionGroupEntity> findActualByIds(List<Long> ids) {
        return query().selectFrom(meta)
                .where(meta.id.in(ids).and(meta.dateTo.isNull()))
                .fetch();
    }

    @Override
    protected JPQLQuery<DivisionGroupEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }
}
