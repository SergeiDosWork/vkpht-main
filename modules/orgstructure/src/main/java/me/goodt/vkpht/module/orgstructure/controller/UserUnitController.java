package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

import com.goodt.drive.auth.sur.service.SurIgnore;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.api.dto.UnitDto;
import me.goodt.vkpht.module.orgstructure.api.dto.request.CurrentUnitRequest;
import me.goodt.vkpht.module.orgstructure.api.UserUnitService;

@RestController
@GeneralAPIResponses
@SurIgnore
@RequiredArgsConstructor
public class UserUnitController {

    private final UserUnitService userUnitService;

    @Operation(
        summary = "Получение информации о всех бизнес-единицах (юнитах) текущего пользователю",
        description = "Получение информации о всех бизнес-единицах (юнитах) текущего пользователю",
        tags = {"employee"}
    )
    @GetMapping("/api/employee/units")
    public List<UnitDto> getEmployeeUnits() {
        return userUnitService.getAvailableUnits();
    }

    @Operation(
        summary = "Получение информации о текущей бизнес-единице (юните) текущего пользователя",
        description = "Получение информации о текущей бизнес-единице (юните) текущего пользователя. " +
            "Значение извлекается из сессии пользователя, дополнительно осуществляется проверка " +
            "принадлежности данного пользователя к указанной в сессии бизнес-единице со стороны БД.",
        tags = {"employee"}
    )
    @GetMapping("/api/employee/current-unit")
    public UnitDto getCurrentUnit() {
        return userUnitService.getCurrentUnit();
    }

    @Operation(
        summary = "Установка полученного значения бизнес-единицы в сессию с названием \"unit_code\".",
        description = "Установка полученного значения бизнес-единицы в сессию с названием \"unit_code\".",
        tags = {"employee"}
    )
    @PostMapping("/api/employee/current-unit")
    public void setCurrentUnit(@Valid @RequestBody CurrentUnitRequest request) {
        userUnitService.setCurrentUnit(request);
    }

    @Operation(
        summary = "Проверяет доступность получаемой бизнес-единицы (юнита) " +
            "для пользователя в рамках которого был осуществлен запрос",
        description = "Проверяет доступность получаемой бизнес-единицы (юнита) " +
            "для пользователя в рамках которого был осуществлен запрос",
        tags = {"employee"}
    )
    @GetMapping("/api/employee/units/{unitCode}/exists")
    public Boolean hasAccessToUnit(
        @Parameter(name = "unitCode", description = "Код бизнес-единицы (юнита)", example = "my_business_unit")
        @PathVariable String unitCode) {
        return userUnitService.isUnitAvailable(unitCode);
    }

}
