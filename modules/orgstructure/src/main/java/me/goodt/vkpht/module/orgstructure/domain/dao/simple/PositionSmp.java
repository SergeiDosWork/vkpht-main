package me.goodt.vkpht.module.orgstructure.domain.dao.simple;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
public class PositionSmp {

    private Long id;

    private Long categoryId;

    private Integer positionImportanceId;

    private Date dateFrom;

    private Date dateTo;

    private String fullName;

    private String shortName;
}
