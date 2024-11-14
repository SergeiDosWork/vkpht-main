package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "org_division_function")
public class DivisionFunctionEntity extends DomainObject {

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "division_id", insertable = false, updatable = false)
    private DivisionEntity division;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "function_id", insertable = false, updatable = false)
    private FunctionEntity function;
    @Column(name = "division_id")
    private Long divisionId;
    @Column(name = "function_id")
    private Long functionId;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public DivisionFunctionEntity(DivisionEntity division, FunctionEntity function) {
        this.division = division;
        this.function = function;
        this.divisionId = division.getId();
        this.functionId = function.getId();
    }
}
