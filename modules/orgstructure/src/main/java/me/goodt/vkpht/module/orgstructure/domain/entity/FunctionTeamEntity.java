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
@Table(name = "org_function_team")
public class FunctionTeamEntity extends DomainObject {

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "parent_id")
    private FunctionTeamEntity parent;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private FunctionTeamEntity precursor;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "function_id")
    private FunctionEntity function;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private TeamTypeEntity type;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private TeamStatusEntity status;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public FunctionTeamEntity(FunctionTeamEntity parent, FunctionTeamEntity precursor,
                              Date dateFrom, Date dateTo, FunctionEntity function, TeamTypeEntity type,
                              String fullName, String shortName,
                              String abbreviation, TeamStatusEntity status) {
        this.parent = parent;
        this.precursor = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.function = function;
        this.type = type;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.status = status;
    }
}
