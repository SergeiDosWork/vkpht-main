package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_personnel_document")
public class PersonnelDocumentEntity extends DomainObject implements ArchivableEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private PersonnelDocumentEntity precursor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private PersonnelDocumentTypeEntity type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private PersonnelDocumentFormEntity form;
    @Column(name = "name", length = 128)
    private String name;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "data", columnDefinition = "text")
    private String data;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    public PersonnelDocumentEntity(EmployeeEntity employee, PersonnelDocumentEntity precursor,
                                   PersonnelDocumentTypeEntity type, PersonnelDocumentFormEntity form,
                                   String name, Date date, Date dateFrom, Date dateTo,
                                   String data, String externalId) {
        this.employee = employee;
        this.precursor = precursor;
        this.type = type;
        this.form = form;
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.data = data;
        this.externalId = externalId;
    }
}
