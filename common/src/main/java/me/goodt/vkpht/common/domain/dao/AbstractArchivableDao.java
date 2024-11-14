package me.goodt.vkpht.common.domain.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;

import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import java.io.Serializable;

public abstract class AbstractArchivableDao<T extends AbstractEntity<I> & ArchivableEntity,
        I extends Serializable> extends AbstractDao<T, I> {

    protected AbstractArchivableDao(Class<T> entityClass, EntityManager em) {
        super(entityClass, em);
    }

    public Page<T> findAllActual(Pageable paging) {
        JPQLQuery<T> actualQuery = createActualQuery();
        if (paging.isPaged()) {
            actualQuery.offset(paging.getOffset())
                    .limit(paging.getPageSize());
        }
        QueryResults<T> fetchResults = actualQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), paging, fetchResults.getTotal());
    }

    protected abstract JPQLQuery<T> createActualQuery();
}
