package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeDismissalFilter {
    private String unitCode;

    private Long employeeId;

    @Builder.Default
    private Boolean actual = true;
}
