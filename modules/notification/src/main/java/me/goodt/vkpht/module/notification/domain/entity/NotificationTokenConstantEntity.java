package me.goodt.vkpht.module.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Entity
@Table(name = "notification_token_constant")
public class NotificationTokenConstantEntity extends DomainObject {

    @Column(name = "token_full_name", unique = true, nullable = false, length = 256)
    private String tokenFullName;
    @Column(name = "constant", nullable = false, length = 128)
    private String constant;
    @Column(name = "value", nullable = false)
    private Long value;

    public NotificationTokenConstantEntity() {
    }

    public NotificationTokenConstantEntity(String tokenFullName, String constant, Long value) {
        this.tokenFullName = tokenFullName;
        this.constant = constant;
        this.value = value;
    }

    public String getTokenFullName() {
        return tokenFullName;
    }

    public void setTokenFullName(String tokenFullName) {
        this.tokenFullName = tokenFullName;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
