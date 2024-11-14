package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class RelatedPostionSortDto {
    private String property;
    private Sort.Direction direction;

    public void setDirection(String direction) {
        this.direction = Sort.Direction.fromString(direction);
    }
}
