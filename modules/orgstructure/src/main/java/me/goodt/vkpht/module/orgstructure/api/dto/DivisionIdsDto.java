package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Объект, содержащий массив идентификаторов подразделения
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionIdsDto {
    List<Long> divisionIds;
}
