package me.goodt.vkpht.module.orgstructure.domain.entity.composite.key;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionKrLevelEntity;

@Getter
@Embeddable
@NoArgsConstructor
public class PositionPositionKrLevelId implements Serializable {

    public PositionPositionKrLevelId(PositionEntity position, PositionKrLevelEntity positionKrLevel) {
        this.position = position;
        this.positionKrLevel = positionKrLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionPositionKrLevelId that = (PositionPositionKrLevelId) o;
        return Objects.equals(position.getId(), that.position.getId()) && Objects.equals(positionKrLevel.getId(), that.positionKrLevel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(position.getId(), positionKrLevel.getId());
    }

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;

    @ManyToOne
    @JoinColumn(name = "position_kr_level_id", nullable = false)
    private PositionKrLevelEntity positionKrLevel;
}
