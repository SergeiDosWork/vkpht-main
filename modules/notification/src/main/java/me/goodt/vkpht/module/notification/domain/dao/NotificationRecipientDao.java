package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationRecipientEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.*;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class NotificationRecipientDao extends AbstractDao<NotificationRecipientEntity, Long> {

    private static final QNotificationRecipientEntity meta = QNotificationRecipientEntity.notificationRecipientEntity;

    public NotificationRecipientDao(EntityManager em) {
        super(NotificationRecipientEntity.class, em);
    }

    public List<NotificationRecipientEntity> getDynamicRecipients(String unitCode) {
        return query().selectFrom(meta)
            .where(meta.name.notIn(STATIC_EMPLOYEE.getName(),
                                   STATIC_DIVISION.getName(),
                                   STATIC_EMAIL.getName()).and(meta.unitCode.eq(unitCode)))
            .fetch();
    }

    public Page<NotificationRecipientEntity> getDynamicRecipients(Pageable paging) {
        JPQLQuery<NotificationRecipientEntity> dynamicQuery = query().selectFrom(meta)
            .where(meta.name.notIn(STATIC_EMPLOYEE.getName(),
                                   STATIC_DIVISION.getName(),
                                   STATIC_EMAIL.getName()));
        QueryResults<NotificationRecipientEntity> fetchResults = dynamicQuery
            .offset(paging.getOffset())
            .limit(paging.getPageSize())
            .fetchResults();

        return new PageImpl<>(fetchResults.getResults(), paging, fetchResults.getTotal());
    }

    public List<NotificationRecipientEntity> findAll(String unitCode) {
        return query().selectFrom(meta)
            .where(meta.unitCode.eq(unitCode))
            .fetch();
    }
}
