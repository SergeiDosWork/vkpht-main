package me.goodt.vkpht.module.notification.domain.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "NotificationDeferredSend")
@TypeDef(name = "json", typeClass = JsonType.class)
public class NotificationDeferredSendEntity {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = {@Parameter(name = "property", value = "notificationLog")})
    @Column(name = "notification_log_id")
    private Long id;

    @OneToOne(cascade = {CascadeType.MERGE})
    @PrimaryKeyJoinColumn(name = "notification_log_id", referencedColumnName = "id")
    private NotificationLogEntity notificationLog;

    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Type(json.class)
    @Column(name = "body_json")
    private String noticeBodyJson;
}
