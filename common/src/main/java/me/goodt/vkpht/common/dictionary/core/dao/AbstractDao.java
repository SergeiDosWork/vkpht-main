package me.goodt.vkpht.common.dictionary.core.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;

import java.io.Serializable;
import java.util.List;

import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@SuppressWarnings({"unchecked", "SpringJavaInjectionPointsAutowiringInspection"})
public abstract class AbstractDao<T extends AbstractEntity<I>, I extends Serializable> extends CustomQueryDslJpaRepositoryImpl<T, I> {

    public AbstractDao(Class<T> entityClass, EntityManager em) {
        super((JpaEntityInformation<T, I>) JpaEntityInformationSupport.getEntityInformation(entityClass, em), em);
    }

    /**
     * Пагинация результатов запроса, в основном для dto т.к. при возврате dto сортировка не поддерживается
     * <p>
     * выбор поля для сортировки реализуется самостоятельно, или запрос выолняется без нее, тогда результат рандомный
     *
     * @param <K> тип возвращаемой сущности или dto
     * @param pg  настройки страницы
     * @param q   запрос для пагинации
     */
    public <K> PageImpl<K> loadPage(Pageable pg, JPQLQuery<K> q) {
        q = q.limit(pg.getPageSize())
            .offset(pg.getPageNumber() * pg.getPageSize());

        QueryResults<K> rez = q.fetchResults();
        List<K> content = rez.getResults();
        long total = rez.getTotal();
        PageRequest page = PageRequest.of(pg.getPageNumber(), pg.getPageSize());
        return new PageImpl<>(content, page, total);
    }

    /**
     * Пагинация результатов запроса с сортировкой
     * <p>
     * применяется только когда возвращается entity
     *
     * @param <K>   тип возвращаемой сущности
     * @param clazz класс entity
     * @param pg    настройки страницы
     * @param q     запрос для пагинации
     */
    public <K> PageImpl<K> loadSortedPage(Class<K> clazz, Pageable pg, JPQLQuery<K> q) {
        JPQLQuery<K> paged = new Querydsl(em, new PathBuilderFactory().create(clazz)).applyPagination(pg, q);
        List<K> content = paged.fetch();
        long total = q.fetchCount();
        return new PageImpl<>(content, pg, total);
    }
}
