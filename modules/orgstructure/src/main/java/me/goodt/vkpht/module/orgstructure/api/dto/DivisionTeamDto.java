package me.goodt.vkpht.module.orgstructure.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DivisionTeamDto {

    public DivisionTeamDto(Long id, Long parentId, Long precursorId, Date dateFrom, Date dateTo,
                           Long divisionId, String divisionShortName, String fullName, String shortName,
                           String abbreviation, Integer isHead, String externalId) {
        this.id = id;
        this.parentId = parentId;
        this.precursorId = precursorId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.divisionId = divisionId;
        this.divisionShortName = divisionShortName;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.isHead = isHead;
        this.externalId = externalId;
    }

    private Long id;
    private Long parentId;
    private Long precursorId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    private Long divisionId;
    private String divisionShortName;
    private Long teamTypeId;
    private Long teamStatusId;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Integer isHead;
    private String externalId;
}
