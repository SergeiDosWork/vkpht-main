package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.api.dto.DivisionShortInfo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionPathData {
    private DivisionInfo current;
    private List<DivisionShortInfo> parents;
}
