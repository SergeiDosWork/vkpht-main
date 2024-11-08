package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class WorkWeekTypeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotNull
    @Size(max = 64)
    private String name;
}
