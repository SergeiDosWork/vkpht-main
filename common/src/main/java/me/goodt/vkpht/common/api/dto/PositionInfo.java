package me.goodt.vkpht.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.goodt.drive.rtcore.model.orgstructure.entities.PositionAssignmentEntity;
import com.goodt.drive.rtcore.model.orgstructure.entities.PositionEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionInfo {
    private PositionEntity position;
    private List<PositionAssignmentEntity> positionAssignments;
}
