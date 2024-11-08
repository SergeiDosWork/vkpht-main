package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class CostCenterDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateFrom;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateTo;
    @Size(max = 128)
    private String code;
    @NotNull
    @Size(max = 256)
    private String fullName;
    @NotNull
    @Size(max = 128)
    private String shortName;
    @NotNull
    @Size(max = 64)
    private String abbreviation;
    @Size(max = 128)
    private String externalId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorEmployeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long updateEmployeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;
}
