package me.goodt.vkpht.common.dictionary.core.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class CustomQueryDslJpaRepositoryImpl<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID> implements CustomQueryDslJpaRepository<T, ID> {

    //All instance variables are available in super, but they are private
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;
    protected final EntityManager em;
    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;
    @Autowired
    private JPQLQueryFactory query;

    public CustomQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInfo, EntityManager em) {
        this(entityInfo, em, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public CustomQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInfo, EntityManager em, EntityPathResolver resolver) {
        super(entityInfo, em);
        this.path = resolver.createPath(entityInfo.getJavaType());
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.em = em;
    }

    @Override
    @Transactional
    public long delete(Predicate predicate) {
        return query().delete(path).where(predicate).execute();
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(FactoryExpression<T> factoryExpression, Predicate predicate) {
        return createQuery(predicate).select(factoryExpression).fetchFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate) {
        final JPQLQuery<?> queryItems = createQuery(predicate);
        return queryItems.select(factoryExpression).fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, OrderSpecifier<?> orderSpecifier) {
        final JPQLQuery<?> queryItems = createQuery(predicate);
        queryItems.orderBy(orderSpecifier);
        return queryItems.select(factoryExpression).fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable) {
        JPQLQuery<?> countQuery = createQuery(predicate);
        JPQLQuery<?> q = querydsl.applyPagination(pageable, createQuery(predicate));

        long total = countQuery.fetchCount();
        List<T> content = total > pageable.getOffset() ? q.select(factoryExpression).fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public JPQLQueryFactory query() {
        return this.query;
    }
}
