package me.goodt.vkpht.common.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/** @author iGurkin - 16.03.2023 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "log_meter")
public class LogMeter extends DomainObject {

    @Column(name = "exec_time", nullable = false)
    private long execTime;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "method_name", nullable = false)
    private String methodName;
}
