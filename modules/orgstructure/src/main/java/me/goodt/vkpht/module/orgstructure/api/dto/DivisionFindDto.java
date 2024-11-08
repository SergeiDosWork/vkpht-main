package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionFindDto {
    private List<Long> id;
    private List<Long> legalEntityId;
    Integer page;
    Integer size;
    String searchingValue;
    Boolean withClosed;
}
