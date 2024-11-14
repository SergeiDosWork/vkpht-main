package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "notification_recipient")
public class NotificationRecipientEntity extends DomainObject {

    @Column(name = "name", nullable = false, length = 256)
    private String name;
    @Column(name = "description", length = 1024)
    private String description;
    /**
     * Признак системного уведомления
     */
    @Column(name = "is_system")
    private Boolean isSystem = false;

    /**
     * Код юнита
     */
    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    public NotificationRecipientEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
