package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_token")
public class NotificationTokenEntity extends DomainObject {

    public NotificationTokenEntity(String name, String description, String shortName, String groupName) {
        this.name = name;
        this.description = description;
        this.shortName = shortName;
        this.groupName = groupName;
    }

    @Column(name = "name", length = 256, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "unit_code")
    private String unitCode;
}
