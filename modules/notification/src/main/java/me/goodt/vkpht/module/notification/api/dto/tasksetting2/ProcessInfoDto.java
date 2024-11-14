package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInfoDto {
    private ProcessDto process;
    @JsonSetter(DtoTagConstants.PROCESS_STAGES_TAG)
    private List<ProcessStageShortDto> processStages;
}
