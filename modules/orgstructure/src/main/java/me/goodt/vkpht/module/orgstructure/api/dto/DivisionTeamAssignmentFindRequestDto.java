package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DivisionTeamAssignmentFindRequestDto {
    private List<Long> legalEntityIds;
    private List<Long> divisionIds;

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(legalEntityIds) && CollectionUtils.isEmpty(divisionIds);
    }
}
