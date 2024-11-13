package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

@Getter
@Setter
public class WorkExperienceTypeDto extends AbstractRes<WorkExperienceTypeDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    @Size(max = 128)
    private String name;
    @Size(max = 1024)
    private String description;
    @Size(max = 128)
    private String externalId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateFrom;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateTo;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorEmployeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long updateEmployeeId;
}
