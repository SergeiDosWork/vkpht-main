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
public class ReasonDto {
    private Long id;
    private Long typeId;
    private String name;
    private String description;
    private Date dateFrom;
    private Date dateTo;
}
