package me.goodt.vkpht.module.orgstructure.domain.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "org_position_position_grade")
public class PositionGradeLink extends AbstractEntity<PositionGradeLinkId> {

    @Id
    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Id
    @Column(name = "position_grade_id", nullable = false)
    private Long positionGradeId;

    @Column(name = "date_from", nullable = false)
    private Date dateFrom = new Date();

    @Column(name = "date_to")
    private Date dateTo;

    @Override
    public PositionGradeLinkId getId() {
        return new PositionGradeLinkId(positionId, positionGradeId);
    }
}
