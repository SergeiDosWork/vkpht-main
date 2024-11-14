package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateFilter;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTemplateEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class NotificationTemplateDao extends AbstractDao<NotificationTemplateEntity, Long> {

    private static final QNotificationTemplateEntity meta = QNotificationTemplateEntity.notificationTemplateEntity;

    public NotificationTemplateDao(EntityManager em) {
        super(NotificationTemplateEntity.class, em);
    }

    public Page<NotificationTemplateEntity> find(NotificationTemplateFilter filter, Pageable pageable) {
        Predicate where = toPredicate(filter);

        return findAll(where, pageable);
    }

    private Predicate toPredicate(NotificationTemplateFilter filter) {
        BooleanBuilder where = new BooleanBuilder();
        if (filter.getUnitCode() != null) {
            where.and(meta.unitCode.eq(filter.getUnitCode()));
        }
        if (filter.getActual() != null) {
            if (filter.getActual()) {
                where.and(meta.dateTo.isNull());
            } else {
                where.and(meta.dateTo.isNotNull());
            }
        }

        return where;
    }

    public NotificationTemplateEntity findByCode(String code, String unitCode) {
        return query().selectFrom(meta)
            .where(
                meta.code.eq(code)
                    .and(meta.isEnabled.eq(1))
                    .and(meta.unitCode.eq(unitCode))
                    .and(meta.dateTo.isNull().or(Expressions.currentTimestamp().between(meta.dateFrom, meta.dateTo))))
            .fetchOne();
    }
}
