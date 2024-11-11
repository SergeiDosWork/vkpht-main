package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.domain.entity.DomainObject;

/**
 * Отключенные каналы отправки уведомлений для Сотрудников
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification_receiver_system_employee_disabled")
public class NotificationReceiverSystemEmployeeDisabledEntity extends DomainObject {

	/**
	 * id сотрудника в orgstructure.Employee
	 */
	@Column(name = "employee_id")
	private Long employeeId;

	/**
	 * Канал отправки уведомления
	 */
	@ManyToOne
	@JoinColumn(name = "notification_receiver_system_id", referencedColumnName = "id")
	private NotificationReceiverSystemEntity notificationReceiverSystem;

}
