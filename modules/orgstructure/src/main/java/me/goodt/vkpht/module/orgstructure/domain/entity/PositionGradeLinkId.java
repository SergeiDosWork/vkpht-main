package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PositionGradeLinkId implements Serializable {

    private Long positionId;

    private Long positionGradeId;
}
