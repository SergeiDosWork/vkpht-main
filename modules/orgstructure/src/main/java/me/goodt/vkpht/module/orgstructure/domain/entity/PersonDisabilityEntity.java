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
@Table(name = "org_person_disability")
public class PersonDisabilityEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "person_id")
    private PersonEntity person;
    @Column(name = "disability_name", nullable = false, length = 64)
    private String disabilityName;
    @Column(name = "disability_description", nullable = false, length = 64)
    private String disabilityDescription;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "disability_category_id")
    private DisabilityCategoryEntity disabilityCategory;
    @Column(name = "date_start", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "date_end", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public PersonDisabilityEntity(PersonEntity person, String disabilityName, String disabilityDescription,
                                  Date dateFrom, Date dateTo, DisabilityCategoryEntity disabilityCategory,
                                  Date dateStart, Date dateEnd) {
        this.person = person;
        this.disabilityName = disabilityName;
        this.disabilityDescription = disabilityDescription;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.disabilityCategory = disabilityCategory;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
}
