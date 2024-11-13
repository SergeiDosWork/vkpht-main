package me.goodt.vkpht.module.orgstructure.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "org_employee")
public class EmployeeEntity extends DomainObject {

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity person;
    @Column(name = "person_id", insertable = false, updatable = false)
    private Long personId;
    @Column(name = "number", nullable = false, length = 64)
    private String number;
    @Column(name = "phone", length = 64)
    private String phone;
    @Column(name = "email", length = 64)
    private String email;
    @Column(name = "telegram", length = 128)
    private String telegram;
    @Column(name = "fax", length = 64)
    private String fax;
    @Column(name = "is_has_mobile", nullable = false)
    private Integer isHasMobile;
    @Column(name = "is_freelancer", nullable = false)
    private Integer isFreelancer;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "external_id", unique = true)
    private String externalId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public EmployeeEntity(Long id) {
        setId(id);
    }

    public EmployeeEntity(PersonEntity person, String number, String phone, String email,
                          String fax, Integer isHasMobile, Integer isFreelancer,
                          Date dateFrom, Date dateTo, String externalId,
                          Date updateDate, Long authorEmployeeId, Long updateEmployeeId) {
        this.person = person;
        this.number = number;
        this.phone = phone;
        this.email = email;
        this.fax = fax;
        this.isHasMobile = isHasMobile;
        this.isFreelancer = isFreelancer;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.externalId = externalId;
        this.updateDate = updateDate;
        this.authorEmployeeId = authorEmployeeId;
        this.updateEmployeeId = updateEmployeeId;
    }
}
