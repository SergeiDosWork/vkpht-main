package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionShortInfoDto {
    private Long id;
    private Long parentId;
    private String fullName;
    private String shortName;
    private String abbreviation;
}
