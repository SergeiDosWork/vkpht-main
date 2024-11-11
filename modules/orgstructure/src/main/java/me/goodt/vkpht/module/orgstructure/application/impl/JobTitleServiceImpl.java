package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.orgstructure.api.JobTitleService;
import me.goodt.vkpht.module.orgstructure.domain.dao.JobTitleDao;
import me.goodt.vkpht.module.orgstructure.domain.projection.EmployeeJobTitleProjection;

@RequiredArgsConstructor
@Service
public class JobTitleServiceImpl implements JobTitleService {

    private final JobTitleDao jobTitleDao;
    private final UnitAccessService unitAccessService;

    @Override
    public Map<Long, String> findTitlesByEmployeeIds(Collection<Long> employeeIds) {
        if (CollectionUtils.isEmpty(employeeIds)) {
            return Map.of();
        }
        return jobTitleDao.findUsingEmployeeIds(employeeIds, unitAccessService.getCurrentUnit())
            .stream()
            .collect(Collectors.toMap(
                EmployeeJobTitleProjection::getEmployeeId,
                EmployeeJobTitleProjection::getJobTitle,
                (existing, replacement) -> String.join(", ", existing, replacement)
            ));
    }
}
