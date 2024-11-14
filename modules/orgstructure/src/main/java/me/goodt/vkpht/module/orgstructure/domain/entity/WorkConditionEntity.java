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
@Table(name = "org_work_condition")
public class WorkConditionEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    private float salary;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_week_type_id")
    private WorkWeekTypeEntity workWeekType;
    @Column(name = "is_has_watch", nullable = false)
    private Integer isHasWatch;
    @Column(name = "is_irregular_hours", nullable = false)
    private Integer isIrregularHours;
    @Column(name = "work_duration_minutes", nullable = false)
    private Integer workDurationMinutes;
    @Column(name = "break_duration_minutes", nullable = false)
    private Integer breakDurationMinutes;
    @Column(name = "shift_count", nullable = false, precision = 2, scale = 1)
    private float shiftCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_document_id")
    private PersonnelDocumentEntity personnelDocument;

    public WorkConditionEntity(EmployeeEntity employee, Date dateFrom, Date dateTo,
                               float salary, WorkWeekTypeEntity workWeekType, Integer isHasWatch,
                               Integer isIrregularHours, Integer workDurationMinutes, Integer breakDurationMinutes,
                               float shiftCount, PersonnelDocumentEntity personnelDocument) {
        this.employee = employee;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.salary = salary;
        this.workWeekType = workWeekType;
        this.isHasWatch = isHasWatch;
        this.isIrregularHours = isIrregularHours;
        this.workDurationMinutes = workDurationMinutes;
        this.breakDurationMinutes = breakDurationMinutes;
        this.shiftCount = shiftCount;
        this.personnelDocument = personnelDocument;
    }
}
