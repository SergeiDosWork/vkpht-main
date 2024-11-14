package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

@Getter
@Setter
public class ImportanceCriteriaGroupDto extends AbstractRes<ImportanceCriteriaGroupDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    private Long typeId;
    @NotNull
    @Size(max = 128)
    private String name;
    @NotNull
    @Size(max = 1024)
    private String description;
    @NotNull
    private Boolean isEditable;
}
