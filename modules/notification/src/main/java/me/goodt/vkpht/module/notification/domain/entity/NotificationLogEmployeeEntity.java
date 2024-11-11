package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.domain.entity.DomainObject;

/**
 * Журнал уведомления - Получатель уведомления
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_log_employee")
public class NotificationLogEmployeeEntity extends DomainObject {

	/**
	 * Запись в журнале уведомлений
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notification_log_id")
	private NotificationLogEntity notificationLog;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Column(name = "notification_log_id", insertable = false, updatable = false)
	private Long notificationLogId;

	/**
	 * Получатель уведомления
	 */
	@Column(name = "employee_id")
	private Long employeeId;

	/**
	 * Признак копии уведомления
	 * (true - получил копию уведомления, false - получил оригинал уведомления)
	 */
	@Column(name = "is_copy")
	private Boolean isCopy;
}
