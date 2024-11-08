package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DivisionGroupDto extends SimpleIdNameDto {
    public DivisionGroupDto(Long id, String name, Date dateFrom, Date dateTo) {
        super(id, name);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    private Date dateFrom;
    private Date dateTo;
}
