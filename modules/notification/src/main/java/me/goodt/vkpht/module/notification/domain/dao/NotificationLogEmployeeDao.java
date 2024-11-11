package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEmployeeEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationLogEmployeeEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
public class NotificationLogEmployeeDao extends AbstractDao<NotificationLogEmployeeEntity, Long> {

	private static final QNotificationLogEmployeeEntity meta = QNotificationLogEmployeeEntity.notificationLogEmployeeEntity;

	public NotificationLogEmployeeDao(EntityManager em) {
		super(NotificationLogEmployeeEntity.class, em);
	}

	public List<Long> loadEmployeesByNotificationLogId(Long notificationLogId, boolean isCopy) {
		BooleanExpression expression = Expressions.allOf(
			meta.isCopy.eq(isCopy),
			meta.notificationLogId.eq(notificationLogId)
		);

		return query().select(meta.employeeId)
			.from(meta)
			.where(expression)
			.fetch();
	}

}
