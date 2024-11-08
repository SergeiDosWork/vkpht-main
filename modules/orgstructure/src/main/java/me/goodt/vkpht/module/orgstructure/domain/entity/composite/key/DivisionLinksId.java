package me.goodt.vkpht.module.orgstructure.domain.entity.composite.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DivisionLinksId implements Serializable {
    private Long parentId;
    private Long childId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DivisionLinksId that = (DivisionLinksId) o;
        return Objects.equals(parentId, that.parentId) && Objects.equals(childId, that.childId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, childId);
    }
}
