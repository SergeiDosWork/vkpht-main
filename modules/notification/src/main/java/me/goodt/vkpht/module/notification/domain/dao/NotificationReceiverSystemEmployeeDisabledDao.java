package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEmployeeDisabledEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationReceiverSystemEmployeeDisabledEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationReceiverSystemEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationReceiverSystemEmployeeDisabledDao extends AbstractDao<NotificationReceiverSystemEmployeeDisabledEntity, Long> {

	private static final QNotificationReceiverSystemEmployeeDisabledEntity meta =
		QNotificationReceiverSystemEmployeeDisabledEntity.notificationReceiverSystemEmployeeDisabledEntity;
    private static final QNotificationReceiverSystemEntity metaNotificationReceiverSystem =
        QNotificationReceiverSystemEntity.notificationReceiverSystemEntity;

	public NotificationReceiverSystemEmployeeDisabledDao(EntityManager em) {
		super(NotificationReceiverSystemEmployeeDisabledEntity.class, em);
	}

	public NotificationReceiverSystemEmployeeDisabledEntity findByEmployeeIdAndNotificationReceiverSystemId(Long employeeId, Long notificationReceiverSystemId) {
        BooleanExpression expression = Expressions.allOf(
            meta.employeeId.eq(employeeId),
            metaNotificationReceiverSystem.id.eq(notificationReceiverSystemId)
        );

        return query().selectFrom(meta)
            .join(metaNotificationReceiverSystem).on(metaNotificationReceiverSystem.id.eq(meta.notificationReceiverSystem.id))
            .where(expression)
            .fetchFirst();
	}

	public boolean existsByEmployeeIdAndReceiverSystemName(Long employeeId, String receiverSystemName) {
		BooleanExpression expression = Expressions.allOf(
            meta.employeeId.eq(employeeId),
            metaNotificationReceiverSystem.name.eq(receiverSystemName).and(metaNotificationReceiverSystem.isActive.isTrue())
		);

        return query().select(meta.notificationReceiverSystem.id)
            .from(meta)
            .join(metaNotificationReceiverSystem).on(metaNotificationReceiverSystem.id.eq(meta.notificationReceiverSystem.id))
            .where(expression)
            .fetchCount() > 0;
	}

	public NotificationReceiverSystemEmployeeDisabledEntity findByEmployeeIdAndNotificationReceiverSystemName(Long employeeId, String notificationReceiverSystemName) {

        BooleanExpression expression = Expressions.allOf(
            meta.employeeId.eq(employeeId),
            metaNotificationReceiverSystem.name.eq(notificationReceiverSystemName)
        );

        return query().selectFrom(meta)
            .join(metaNotificationReceiverSystem).on(metaNotificationReceiverSystem.id.eq(meta.notificationReceiverSystem.id))
            .where(expression)
            .fetchFirst();
	}
}
