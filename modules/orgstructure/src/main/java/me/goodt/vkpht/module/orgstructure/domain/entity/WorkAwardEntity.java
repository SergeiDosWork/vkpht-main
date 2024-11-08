package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Table(name = "org_work_award")
public class WorkAwardEntity extends DomainObject {

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
    @Column(name = "date_start", nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "decree_number", length = 128, nullable = false)
    private String decreeNumber;
    @Column(name = "decree_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date decreeDate;

    public WorkAwardEntity(Date dateFrom, Date dateTo, WorkExperienceTypeEntity type, EmployeeEntity employee,
                           Date dateStart, String decreeNumber, Date decreeDate) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
        this.employee = employee;
        this.dateStart = dateStart;
        this.decreeNumber = decreeNumber;
        this.decreeDate = decreeDate;
    }
}
