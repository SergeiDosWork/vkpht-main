package me.goodt.vkpht.module.orgstructure.api.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class FindEmployeeRequest {

    private List<Long> employeeIds;
    private List<Long> divisionIds;
    private List<Long> functionIds;
    private Long jobTitleId;
    private String positionShortName;
    private Long legalEntityId;
    private String searchingValue;
    private boolean withPatronymic = true;
    private boolean withClosed = true;
    private List<String> employeeNumber;
    private List<String> emails;
    private boolean hasPositionAssignment;
    private Pageable pageable = Pageable.unpaged();
}
