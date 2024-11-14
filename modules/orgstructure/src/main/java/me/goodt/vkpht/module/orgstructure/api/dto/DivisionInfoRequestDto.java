package me.goodt.vkpht.module.orgstructure.api.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionInfoRequestDto {

    @Parameter(description = "Идентификаторы подразделения (таблица division)")
    private List<Long> divisionIds;

    @Parameter(description = "Идентификатор родительского подразделения (таблица division)")
    private Long parentId;

    @Parameter(description = "Идентификатор организации (таблица division)")
    private Long legalEntityId;

    @Parameter(description = "Необходимо ли получать подчиненных от подчиненных по parent_id (таблица division)")
    private boolean withChilds;

    @Parameter(description = "Идентификаторы группы (таблица group)")
    private List<Long> groupIds;
}
