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
@Table(name = "notification_template_content_recipient")
public class NotificationTemplateContentRecipientLinkEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_template_content_id")
    private NotificationTemplateContentEntity notificationTemplateContent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_recipient_id")
    private NotificationRecipientEntity notificationRecipient;

    @Column(name = "is_copy", nullable = false)
    private Boolean isCopy;

    /**
     * Признак системного уведомления
     */
    @Column(name = "is_system")
    private Boolean isSystem = false;

}
