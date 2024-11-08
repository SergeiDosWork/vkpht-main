package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.common.domain.dao.AbstractArchivableDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QCitizenshipEntity;

@Repository
public class CitizenshipDao extends AbstractArchivableDao<CitizenshipEntity, Long> {

	private static final QCitizenshipEntity meta = QCitizenshipEntity.citizenshipEntity;

    public CitizenshipDao(EntityManager em) {
        super(CitizenshipEntity.class, em);
    }

	public Long findIdByExternalId(String externalId) {
		return query().from(meta)
			.select(meta.id)
			.where(meta.externalId.eq(externalId))
			.fetchFirst();
	}

	@Override
	protected JPQLQuery<CitizenshipEntity> createActualQuery() {
		return query().selectFrom(meta)
			.where(meta.dateTo.isNull());
	}
}
