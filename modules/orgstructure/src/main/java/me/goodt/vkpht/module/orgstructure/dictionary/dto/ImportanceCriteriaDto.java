package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class ImportanceCriteriaDto extends AbstractRes<ImportanceCriteriaDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    private Long groupId;
    @NotNull
    @Size(max = 256)
    private String name;
    @NotNull
    @Size(max = 1024)
    private String description;
    @NotNull
    @DecimalMax(value = "9999.99")
    private Float weight;
    @NotNull
    private Boolean isEnabled;
    private Long calculationMethodId;
}
