package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;

import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "notification_template")
public class NotificationTemplateEntity extends DomainObject implements ArchivableEntity {

    public static final String SEQUENCE_BASE_GENERATOR = "base_generator";
    /**
     * Дата создания записи
     */
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    /**
     * Дата окончания записи
     */
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    /**
     * ID пользователя, создавшего запись
     */
    @Column(name = "author_employee_id")
    private Long authorEmployeeId;
    /**
     * ID пользователя, обновившего запись
     */
    @Column(name = "author_update_employee_id")
    private Long authorUpdateEmployeeId;
    /**
     * Дата обновления
     */
    @Column(name = "date_update", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    /**
     * Название события
     */
    @Column(name = "name", length = 256)
    private String name;
    /**
     * Код события
     */
    @Column(name = "code", nullable = false, unique = true, length = 128)
    private String code;
    /**
     * Описание события
     */
    @Column(name = "description", nullable = false, length = 1024)
    private String description;
    /**
     * Состояние активности
     */
    @Column(name = "is_enabled", nullable = false)
    @ColumnDefault("0")
    private Integer isEnabled;

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

    public NotificationTemplateEntity(Date dateFrom, Date dateTo, Long authorEmployeeId, Long authorUpdateEmployeeId,
                                      Date dateUpdate, String name, String code, String description, Integer isEnabled, String unitCode) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.authorEmployeeId = authorEmployeeId;
        this.authorUpdateEmployeeId = authorUpdateEmployeeId;
        this.dateUpdate = dateUpdate;
        this.name = name;
        this.code = code;
        this.description = description;
        this.isEnabled = isEnabled;
        this.unitCode = unitCode;
    }

    @Override
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
