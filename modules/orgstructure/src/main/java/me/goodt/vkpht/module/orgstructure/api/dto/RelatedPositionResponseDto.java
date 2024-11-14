package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RelatedPositionResponseDto {
    private Long taskId;
    private boolean isEditable;
    private boolean isDeletable;
    private boolean isStatusChangeAllowed;

    private List<RelatedPositionColumnsDto> columns;

    public Optional<RelatedPositionColumnsDto> getColumnByCode(String code) {
        return columns.stream()
            .filter(column -> column.getCode().equals(code))
            .findFirst();
    }

    public boolean isAllColumnsPresent(List<String> codes) {
        return columns.stream()
            .map(RelatedPositionColumnsDto::getCode)
            .collect(Collectors.toSet()).containsAll(codes);
    }
}
