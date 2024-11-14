package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.Map;

public interface JobTitleService {
    Map<Long, String> findTitlesByEmployeeIds(Collection<Long> employeeIds);
}
