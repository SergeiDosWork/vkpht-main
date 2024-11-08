package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.goodt.drive.rtcore.dto.tasksetting2.input.BffTaskFindRequestFilter;

@Getter
@Setter
public class RelatedPositionFilterDto {
    private String code;
    private BffTaskFindRequestFilter.Operator operator;
    private List<String> value;

    public enum Operator {
        EQ("eq"),
        LIKE("like"),
        IN("in");

        private final String definition;

        Operator(String definition) {
            this.definition = definition;
        }
    }
}
