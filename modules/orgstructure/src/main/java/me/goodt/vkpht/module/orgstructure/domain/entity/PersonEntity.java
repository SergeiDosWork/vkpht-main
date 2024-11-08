package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "org_person")
public class PersonEntity extends DomainObject {

    @Column(name = "surname", nullable = false, length = 128)
    private String surname;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "patronymic", length = 128)
    private String patronymic;

    @Column(name = "date_birth", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "family_status_id")
    private FamilyStatusEntity familyStatus;

    @Column(name = "sex", nullable = false, length = 1)
    private String sex;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "parent_id")
    private PersonEntity parent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "spouse_id")
    private PersonEntity spouse;

    @Column(name = "photo", length = 256)
    private String photo;

    @Column(name = "snils", length = 32)
    private String snils;

    @Column(name = "inn", length = 32)
    private String inn;

    @Column(name = "address", length = 128)
    private String address;

    @Column(name = "city", length = 64)
    private String city;

    @Column(name = "postcode", length = 64)
    private String postcode;

    @Column(name = "phone", length = 32)
    private String phone;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "telegram", length = 128)
    private String telegram;

    @Column(name = "external_id", length = 128)
    private String externalId;

    @Column(name = "citizenship_id")
    private Long citizenshipId;

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;

    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
}
