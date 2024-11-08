package me.goodt.vkpht.module.orgstructure.domain.dao.simple;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
public class PositionAssignmentSmp extends BaseDynamicObject {

    private Long id;

    private Integer typeId;

    private Long employeeId;

    private Date dateFrom;

    private Date dateTo;

    private String fullName;

    private String shortName;

    private PositionSmp position;

    private DivisionSmp division;

    private JobTitleSmp jobTitle;

    private DivisionTeamSmp divisionTeam;
}
