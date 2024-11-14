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
public class DivisionTeamFullDto {
    private Long id;
    private Long parentId;
    private Long precursorId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    private Long divisionId;
    private String divisionShortName;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Integer isHead;
    private String externalId;
    private DivisionWithParentDto division;
}