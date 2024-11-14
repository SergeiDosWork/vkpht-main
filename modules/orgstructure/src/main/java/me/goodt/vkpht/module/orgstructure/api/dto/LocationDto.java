package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LocationDto {

    private Long id;
    private Long groupId;
    private String index;
    private Float longitude;
    private Float latitude;
    private String number;
    private String building;
    private String housing;
    private String street;
    private String district;
    private String city;
    private String area;
    private String region;
    private String country;
    private Date dateFrom;
    private Date dateTo;
}
