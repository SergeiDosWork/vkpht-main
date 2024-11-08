package me.goodt.vkpht.module.orgstructure.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {
    private Long id;
    private Long precursorId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    private Long divisionId;
    private Long jobTitleId;
    private Long workplaceId;
    private Long workFunctionId;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Long categoryId;
    private Long rankId;
    private Integer statusId;
    private Long positionTypeId;
    private Float stake;
    private Integer isKey;
    private Integer isVariable;
    private Integer positionImportanceId;
    private Integer isKeyManagement;
    private JobTitleDto jobTitle;
    private String externalId;
    private String divisionShortName;
    private Long costCenterId;
    private DivisionDto division;
}
