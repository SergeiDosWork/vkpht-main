package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEmployeeSubscribeEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationReceiverSystemEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTemplateContentEmployeeSubscribeEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationTemplateContentEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationTemplateContentEmployeeSubscribeDao extends AbstractDao<NotificationTemplateContentEmployeeSubscribeEntity, Long> {

    public static final QNotificationTemplateContentEmployeeSubscribeEntity meta =
        QNotificationTemplateContentEmployeeSubscribeEntity.notificationTemplateContentEmployeeSubscribeEntity;

    public static final QNotificationTemplateContentEntity metaNotificationTemplateContent = QNotificationTemplateContentEntity.notificationTemplateContentEntity;

    public static final QNotificationReceiverSystemEntity metaNotificationReceiverSystem = QNotificationReceiverSystemEntity.notificationReceiverSystemEntity;

    public NotificationTemplateContentEmployeeSubscribeDao(EntityManager em) {
        super(NotificationTemplateContentEmployeeSubscribeEntity.class, em);
    }

    public NotificationTemplateContentEmployeeSubscribeEntity findByEmployeeIdAndNotificationTemplateContentId(Long employeeId, Long notificationTemplateContentId) {
        BooleanExpression expression = Expressions.allOf(
            meta.employeeId.eq(employeeId),
            meta.notificationTemplateContent.id.eq(notificationTemplateContentId)
        );
        return query().selectFrom(meta)
            .where(expression)
            .fetchFirst();
    }

    public NotificationTemplateContentEmployeeSubscribeEntity findByIdAndEmployeeId(Long subscribeId, Long employeeId) {
        BooleanExpression expression = Expressions.allOf(
            meta.id.eq(subscribeId),
            meta.employeeId.eq(employeeId)
        );
        return query().selectFrom(meta)
            .where(expression)
            .fetchFirst();
    }

    public List<NotificationTemplateContentEmployeeSubscribeEntity> findByEmployeeId(Long employeeId, String receiverSystemName) {
        BooleanExpression expression = Expressions.allOf(
            meta.employeeId.eq(employeeId),
            metaNotificationReceiverSystem.name.eq(receiverSystemName).and(metaNotificationReceiverSystem.isActive.isTrue())
        );

        return query().selectFrom(meta)
            .innerJoin(metaNotificationTemplateContent).on(metaNotificationTemplateContent.id.eq(meta.notificationTemplateContent.id))
            .innerJoin(metaNotificationReceiverSystem).on(metaNotificationReceiverSystem.id.eq(metaNotificationTemplateContent.receiverSystem.id))
            .where(expression)
            .orderBy(meta.id.asc())
            .fetch();
    }
}
