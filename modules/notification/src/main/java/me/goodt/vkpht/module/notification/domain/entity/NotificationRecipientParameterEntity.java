package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "notification_recipient_parameters")
public class NotificationRecipientParameterEntity extends DomainObject {

    @Column(name = "value", nullable = false, length = 256)
    private Long value;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_recipient_id")
    private NotificationRecipientEntity parent;
    @Column(name = "is_system")
    private Boolean isSystem = false;

    public NotificationRecipientParameterEntity(Long value, NotificationRecipientEntity parent) {
        this.value = value;
        this.parent = parent;
    }
}
