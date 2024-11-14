package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStageDto extends ProcessStageShortDto {
    private ProcessDto process;
}
