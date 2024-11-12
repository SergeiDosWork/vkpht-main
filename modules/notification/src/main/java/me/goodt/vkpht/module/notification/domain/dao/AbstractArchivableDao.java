package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

import me.goodt.vkpht.module.notification.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.micro.core.entity.AbstractEntity;

public abstract class AbstractArchivableDao<T extends AbstractEntity<I> & ArchivableEntity,
	I extends Serializable> extends AbstractDao<T, I> {

	public AbstractArchivableDao(Class<T> entityClass, EntityManager em) {
		super(entityClass, em);
	}

	public Page<T> findAllActual(Pageable paging) {
		QueryResults<T> fetchResults = createActualQuery()
			.offset(paging.getOffset())
			.limit(paging.getPageSize())
			.fetchResults();

		return new PageImpl<>(fetchResults.getResults(), paging, fetchResults.getTotal());
	}

	protected abstract JPQLQuery<T> createActualQuery();
}
