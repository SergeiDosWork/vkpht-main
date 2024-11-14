package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualEducationDto {

    private Long id;

    private String name;
    private String externalId;
    private Date dateFrom;
    private Date dateTo;
}

