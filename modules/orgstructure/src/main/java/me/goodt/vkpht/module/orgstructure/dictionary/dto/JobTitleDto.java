package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class JobTitleDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    @Size(max = 128)
    private String abbreviation;
    @NotNull
    @Size(max = 32)
    private String code;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateFrom;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateTo;
    @NotNull
    @Size(max = 512)
    private String fullName;
    @NotNull
    @Size(max = 64)
    private String hash;
    @NotNull
    private Boolean isRequiredCertificate;
    @NotNull
    @Size(max = 256)
    private String shortName;
    private Long clusterId;
    private Long precursorId;
    @Size(max = 128)
    private String externalId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorEmployeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long updateEmployeeId;
}
