package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.EducationTypeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QEducationTypeEntity;

@Repository
public class EducationTypeDao extends AbstractArchivableDao<EducationTypeEntity, Long> {

    private static final QEducationTypeEntity meta = QEducationTypeEntity.educationTypeEntity;

    public EducationTypeDao(EntityManager em) {
        super(EducationTypeEntity.class, em);
    }

    @Override
    protected JPQLQuery<EducationTypeEntity> createActualQuery() {
        return query().selectFrom(meta)
                .where(meta.dateTo.isNull());
    }

    public Long findIdByExternalId(String externalId) {
        return query().select(meta.id)
                .from(meta)
                .where(meta.externalId.eq(externalId))
                .fetchFirst();
    }
}
