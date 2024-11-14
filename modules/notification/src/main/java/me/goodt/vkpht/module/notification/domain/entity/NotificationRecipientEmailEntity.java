package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_recipient_email")
public class NotificationRecipientEmailEntity extends DomainObject {

    @Column(name = "email")
    private String email;

    @Column(name = "notification_recipient_id", insertable = false, updatable = false)
    private Long notificationRecipientId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_recipient_id")
    private NotificationRecipientEntity notificationRecipient;

    @Column(name = "is_system")
    private Boolean isSystem = false;
}
