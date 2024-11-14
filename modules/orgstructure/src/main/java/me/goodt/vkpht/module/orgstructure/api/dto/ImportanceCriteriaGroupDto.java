
package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportanceCriteriaGroupDto {
    private Long id;
    private Long typeId;
    private String name;
    private String description;
    private short isEditable;
}
