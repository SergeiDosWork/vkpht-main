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
public class PersonPrivilegeDto extends AbstractRes<PersonPrivilegeDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Size(max = 128)
    private String externalId;
    @NotNull
    private Long privilegeId;
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
