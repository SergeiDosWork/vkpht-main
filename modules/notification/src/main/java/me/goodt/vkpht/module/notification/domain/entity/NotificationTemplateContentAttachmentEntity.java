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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notification_template_content_attachment")
public class NotificationTemplateContentAttachmentEntity extends DomainObject {

    /**
     * Шаблон уведомления, которому принадлежит текущая запись вложения.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_template_content_id")
    private NotificationTemplateContentEntity notificationTemplateContent;

    /**
     * Имя или путь к файлу для получения его содержимого из файлового хранилища.
     *
     * <p>Формат содержимого зависит от используемой реализации или внешнего сервиса,
     * выступающего в виде файлового хранилища.
     */
    @Column(name = "storage_file_path", nullable = false)
    private String storageFilePath;

    /**
     * Исходное имя загруженного файла.
     */
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * Тип файла (MIME Type).
     *
     * <p>Требуется для указания типа файла вложения в MimeMessage отправляемого письма.
     * <p>Может быть значение <code>null</code>, если тип не был явно указан при загрузке файла.
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * Признак системного уведомления
     */
    @Column(name = "is_system")
    private Boolean isSystem = false;
}
