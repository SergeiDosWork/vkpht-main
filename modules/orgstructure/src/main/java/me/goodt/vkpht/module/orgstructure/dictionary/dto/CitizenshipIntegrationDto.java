package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

@Getter
@Setter
@Accessors(chain = true)
public class CitizenshipIntegrationDto extends AbstractRes<CitizenshipIntegrationDto> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    @Size(max = 256)
    private String name;
    @Size(max = 128)
    private String abbreviation;
    @Size(max = 256)
    private String designation;
    @Size(max = 256)
    private String inEnglish;
    @Size(max = 256)
    private String responsible;
    @Size(max = 512)
    private String basis;
    private Date dateIntroduction;
    private Date dateExpiration;
    @Size(max = 256)
    private String lastModified;
    @Size(max = 512)
    private String reasonForChange;
}
