package me.goodt.vkpht.module.orgstructure.domain.entity.composite.key;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionGradeEntity;

@Getter
@Embeddable
@NoArgsConstructor
public class PositionPositionGradeId implements Serializable {

    public PositionPositionGradeId(PositionEntity position, PositionGradeEntity positionGrade) {
        this.position = position;
        this.positionGrade = positionGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionPositionGradeId that = (PositionPositionGradeId) o;
        return Objects.equals(position.getId(), that.position.getId()) && Objects.equals(positionGrade.getId(), that.positionGrade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(position.getId(), positionGrade.getId());
    }

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;

    @ManyToOne
    @JoinColumn(name = "position_grade_id", nullable = false)
    private PositionGradeEntity positionGrade;
}
