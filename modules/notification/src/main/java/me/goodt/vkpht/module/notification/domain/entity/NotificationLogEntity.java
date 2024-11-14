package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

/**
 * Журнал уведомлений (отправленные уведомления)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification_log")
public class NotificationLogEntity extends DomainObject {

    /**
     * Шаблон, на основе которого сформировано уведомление
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_template_content_id")
    private NotificationTemplateContentEntity notificationTemplateContent;

    /**
     * Дата и время измениения уведомления
     */
    @Column(name = "date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    /**
     * Статус отправки уведомления
     */
    @Column(name = "status")
    private String status;

    /**
     * Тема уведомления
     */
    @Column(name = "subject")
    private String subject;

    /**
     * Текст уведомления
     */
    @Column(name = "message")
    private String message;

    /**
     * Текст ошибки отправки уведомления
     */
    @Column(name = "error_message")
    private String errorMessage;
}
