package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Table(name = "org_work_experience")
public class WorkExperienceEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private WorkExperienceTypeEntity type;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "duration_years", nullable = false)
    private Long durationYears;
    @Column(name = "duration_months", nullable = false)
    private Long durationMonths;
    @Column(name = "duration_days", nullable = false)
    private Long durationDays;

    @Column(name = "external_id", length = 128, unique = true)
    private String externalId;

    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;

    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
}
