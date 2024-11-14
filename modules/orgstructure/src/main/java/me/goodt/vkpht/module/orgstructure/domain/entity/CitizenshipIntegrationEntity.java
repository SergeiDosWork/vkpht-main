package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_citizenship_integration")
public class CitizenshipIntegrationEntity extends DomainObject {

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "abbreviation", length = 128)
    private String abbreviation;

    @Column(name = "designation", length = 256)
    private String designation;

    @Column(name = "in_english", length = 256)
    private String inEnglish;

    @Column(name = "responsible", length = 256)
    private String responsible;

    @Column(name = "basis", length = 512)
    private String basis;

    @Column(name = "date_introduction", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIntroduction;

    @Column(name = "date_expiration", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExpiration;

    @Column(name = "last_modified", length = 256)
    private String lastModified;

    @Column(name = "reason_for_change", length = 512)
    private String reasonForChange;
}
