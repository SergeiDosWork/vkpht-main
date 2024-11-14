package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.WhereJoinTable;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "notification_template_content")
public class NotificationTemplateContentEntity extends DomainObject {

    @ManyToOne
    @JoinColumn(name = "notification_template_id", nullable = false)
    private NotificationTemplateEntity notificationTemplate;
    //    @Getter(AccessLevel.NONE)
    //    @Setter(AccessLevel.NONE)
    //    @Column(name = "notification_template_id", insertable = false, updatable = false)
    //    private Long notificationTemplateId;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "notification_template_content_recipient",
        joinColumns = @JoinColumn(name = "notification_template_content_id"),
        inverseJoinColumns = @JoinColumn(name = "notification_recipient_id"))
    @WhereJoinTable(clause = "is_copy=false")
    private Set<NotificationRecipientEntity> notificationRecipient = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "notification_template_content_recipient",
        joinColumns = @JoinColumn(name = "notification_template_content_id"),
        inverseJoinColumns = @JoinColumn(name = "notification_recipient_id"))
    @WhereJoinTable(clause = "is_copy=true")
    private Set<NotificationRecipientEntity> notificationRecipientCopy = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "receiver_system_id", nullable = false)
    private NotificationReceiverSystemEntity receiverSystem;
    //    @Getter(AccessLevel.NONE)
    //    @Setter(AccessLevel.NONE)
    //    @Column(name = "receiver_system_id", insertable = false, updatable = false)
    //    private Long receiverSystemId;
    @Column(name = "is_enabled", nullable = false)
    @ColumnDefault("0")
    private Integer isEnabled;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "body_json", nullable = false, columnDefinition = "jsonb")
    private String bodyJson;
    @ManyToOne
    @JoinColumn(name = "id_to_substitute")
    private NotificationTemplateContentEntity substitute;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "id_to_substitute", insertable = false, updatable = false)
    private Long idSubstitute;
    /**
     * Описание (наименование) шаблона уведомления
     */
    @Column(name = "description")
    private String description;
    /**
     * Признак указания приоритета уведомления
     * true - Высокий
     * false - Обычный
     */
    @Column(name = "priority")
    private Boolean priority;
    /**
     * Дата создания шаблона уведомления
     */
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    /**
     * Дата удаления шаблона уведомления
     */
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    /**
     * Признак системного уведомления
     */
    @Column(name = "is_system")
    private Boolean isSystem = false;

    /**
     * Код модуля (из которого будет отправлено уведомление)
     */
    @Column(name = "code_module")
    private String codeModule;

    public NotificationTemplateContentEntity(NotificationTemplateEntity notificationTemplate,
                                             NotificationReceiverSystemEntity receiverSystem,
                                             Set<NotificationRecipientEntity> notificationRecipient,
                                             Set<NotificationRecipientEntity> notificationRecipientCopy,
                                             Integer isEnabled,
                                             String bodyJson,
                                             NotificationTemplateContentEntity substitute) {
        this.notificationTemplate = notificationTemplate;
        this.receiverSystem = receiverSystem;
        this.notificationRecipient = notificationRecipient;
        this.notificationRecipientCopy = notificationRecipientCopy;
        this.isEnabled = isEnabled;
        this.bodyJson = bodyJson;
        this.substitute = substitute;
    }

    public boolean isEnabled() {
        return isEnabled != null && isEnabled == 1;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled ? 1 : 0;
    }

    public Set<NotificationRecipientEntity> getAllRecipients() {
        Set<NotificationRecipientEntity> recipients = new HashSet<>(this.getNotificationRecipient());
        recipients.addAll(this.getNotificationRecipientCopy());
        return recipients;
    }
}
