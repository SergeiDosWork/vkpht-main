package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "notification_receiver_system")
public class NotificationReceiverSystemEntity extends DomainObject {

    /**
     * Наименование
     */
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    /**
     * Описание
     */
    @Column(name = "description", length = 1024)
    private String description;

    /**
     * Юнит-код
     */
    @Column(name = "unit_code")
    private String unitCode;

    /**
     * Признак активности записи (отображение на фронте, использование в системе и тд.)
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * Признак указывающий на основание создания процесса (true - процесс поставляется вместе с системой, false - создан на проекте)
     */
    @Column(name = "is_system")
    private Boolean isSystem;

    /**
     * Признак указывающий на доступность редактирования системной записи
     */
    @Column(name = "is_editable_if_system")
    private Boolean isEditableIfSystem;

    public NotificationReceiverSystemEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
