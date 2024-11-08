package me.goodt.vkpht.common.dictionary.core.dao;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CustomQueryDslJpaRepository<T, I extends Serializable> extends JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

    long delete(Predicate predicate);

    T findOne(FactoryExpression<T> factoryExpression, Predicate predicate);

    List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate);

    List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, OrderSpecifier<?> sort);

    Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable);

    JPQLQueryFactory query();
}
