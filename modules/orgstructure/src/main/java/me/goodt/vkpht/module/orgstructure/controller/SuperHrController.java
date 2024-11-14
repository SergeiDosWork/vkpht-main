package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.annotation.Performance;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PersonInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.request.PersonInfoRequest;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.module.orgstructure.api.EmployeePassportService;

@Performance
@GeneralAPIResponses
@RestController
@RequiredArgsConstructor
@RequestMapping("api/super-hr")
public class SuperHrController {

    private final LoggerService loggerService;
    private final EmployeePassportService employeePassportService;

    @Operation(
        summary = "Получение информации по физическому лицу (Паспортичка)",
        tags = "super_hr"
    )
    @GetMapping("/person/passport")
    public List<PersonInfoDto> getPersonInfo(@ParameterObject PersonInfoRequest request) {
        loggerService.createLog(UUID.randomUUID(), "api/super-hr/person/passport", null, request);

        Long employeeId = request.getEmployeeId();
        String snils = request.getSnils();
        boolean includeSecondaryInfo = BooleanUtils.isTrue(request.getIncludeSecondaryInfo());

        if (employeeId == null && StringUtils.isEmpty(snils)) {
            throw new BadRequestException(
                "Отсутствует обязательный параметр: employee_id или snils");
        }

        if (StringUtils.isNotEmpty(snils)) {
            return employeePassportService.getPersonInfo(snils, includeSecondaryInfo);
        }

        return List.of(employeePassportService.getPersonInfo(employeeId, includeSecondaryInfo));
    }
}
