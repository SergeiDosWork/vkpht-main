package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.goodt.drive.rtcore.dto.tasksetting2.DtoValidationConstants;
import com.goodt.drive.rtcore.dto.tasksetting2.input.PageRequest;

@Getter
@Setter
@NoArgsConstructor
public class RelatedPositionRequestDto extends PageRequest {
    @NotNull(message = DtoValidationConstants.NOT_NULL)
    private Long taskId;
    private Long positionId;
    private Long employeeId;
    private Date commitDate;
    private Long taskTypeId;

    private List<RelatedPositionFilterDto> filter = new ArrayList<>();

    private List<RelatedPostionSortDto> sort = new ArrayList<>();


}
