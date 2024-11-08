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
@Table(name = "org_location")
public class LocationEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "group_id")
    private LocationGroupEntity group;
    @Column(name = "index", nullable = false, length = 16)
    private String index;
    @Column(name = "longitude", nullable = false, precision = 7, scale = 4)
    private float longitude;
    @Column(name = "latitude", nullable = false, precision = 7, scale = 4)
    private float latitude;
    @Column(name = "number", nullable = false, length = 8)
    private String number;
    @Column(name = "building", length = 8)
    private String building;
    @Column(name = "housing", length = 8)
    private String housing;
    @Column(name = "street", nullable = false, length = 128)
    private String street;
    @Column(name = "district", length = 64)
    private String district;
    @Column(name = "city", length = 64)
    private String city;
    @Column(name = "area", length = 64)
    private String area;
    @Column(name = "region", nullable = false, length = 64)
    private String region;
    @Column(name = "country", nullable = false, length = 128)
    private String country;
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

    public LocationEntity(LocationGroupEntity group, String index, float longitude, float latitude,
                          String number, String building, String housing, String street, String district,
                          String city, String area, String region, String country) {
        this.group = group;
        this.index = index;
        this.longitude = longitude;
        this.latitude = latitude;
        this.number = number;
        this.building = building;
        this.housing = housing;
        this.street = street;
        this.district = district;
        this.city = city;
        this.area = area;
        this.region = region;
        this.country = country;
    }
}
