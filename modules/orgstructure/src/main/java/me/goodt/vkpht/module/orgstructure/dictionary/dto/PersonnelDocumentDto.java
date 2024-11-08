package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelDocumentDto extends AbstractRes<PersonnelDocumentDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long employeeId;
    private Long precursorId;
    private Integer typeId;
    private Integer formId;
    @NotNull
    @Size(max = 128)
    private String name;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateFrom;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date dateTo;
    private String data;
    @Size(max = 128)
    private String externalId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long authorEmployeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long updateEmployeeId;
}
