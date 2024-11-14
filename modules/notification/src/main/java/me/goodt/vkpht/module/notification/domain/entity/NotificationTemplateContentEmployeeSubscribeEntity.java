package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.domain.entity.DomainObject;

/**
 * Подписки сотрудников на Типы уведомлений (включение/отключение)
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "notification_template_content_employee_subscribe")
public class NotificationTemplateContentEmployeeSubscribeEntity extends DomainObject {

    /**
     * Шаблон уведомления
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_template_content_id")
    NotificationTemplateContentEntity notificationTemplateContent;
    /**
     * id сотрудника в orgstructure.Employee
     */
    @Column(name = "employee_id")
    private Long employeeId;
    /**
     * Включение/Отключение подписки (true - включена / false - отключена)
     */
    @Column(name = "is_enabled")
    private Boolean isEnabled;

    /**
     * Признак системного уведомления
     */
    @Column(name = "is_system")
    private Boolean isSystem = false;

    public NotificationTemplateContentEmployeeSubscribeEntity(Long employeeId, NotificationTemplateContentEntity notificationTemplateContent, boolean isEnabled) {
        this.employeeId = employeeId;
        this.notificationTemplateContent = notificationTemplateContent;
        this.isEnabled = isEnabled;
    }
}
