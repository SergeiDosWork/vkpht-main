package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.goodt.vkpht.common.api.ILoggerService;
import me.goodt.vkpht.common.api.annotation.ApiPageable;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractCrudController;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeDeputyDto;
import me.goodt.vkpht.module.orgstructure.application.EmployeeDeputyService;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.EmployeeDeputyAsm;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDeputyEntity;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
@RequestMapping("/api/employee-deputy")
public class EmployeeDeputyController extends AbstractCrudController<Long, EmployeeDeputyDto> {

    private final ILoggerService loggerService;
    @Getter
    private final EmployeeDeputyService service;
    @Getter
    private final EmployeeDeputyAsm asm;

    @Operation(summary = "Получение информации об объектe employee_deputy", description = "Получение информации об объектe employee_deputy", tags = {"employee_deputy"})
    @BadRequestAPIResponses
    @GetMapping("/{id}")
    @Override
    public EmployeeDeputyDto getById(
        @Parameter(name = "id", description = "Идентификатор объекта employee_deputy (таблица employee_deputy).", example = "1")
        @PathVariable(name = "id") Long id) {
        return super.getById(id);
    }

    @ApiPageable
    @Operation(summary = "Получение информации о всех объектах employee_deputy", description = "Получение информации о всехобъектах employee_deputy", tags = {"employee_deputy"})
    @GetMapping
    public Page<EmployeeDeputyDto> getAll(
        @PageableDefault(page = 0, size = 10)
        @SortDefault.SortDefaults({
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
        }) Pageable pageable,
        @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица employee).", example = "1")
        @RequestParam(name = "employee_id", required = false) Long employeeId,
        @Parameter(name = "date", description = "Дата.", example = "2023-08-03")
        @RequestParam(name = "date", required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Page<EmployeeDeputyEntity> employeeDeputy = getService().getAll(employeeId, date, pageable);
        return employeeDeputy.map(asm::toRes);
    }

    @Operation(summary = "Создание объекта employee_deputy", description = "Создание объекта employee_deputy ", tags = {"employee_deputy"})
    @BadRequestAPIResponses
    @PostMapping
    @Override
    public EmployeeDeputyDto create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление employee_deputy (таблица employee_deputy)")
        @RequestBody EmployeeDeputyDto input) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/employee-deputy", null, input);
        return super.create(input);
    }

    @Operation(summary = "Изменение объекта employee_deputy", description = "Изменение объекта employee_deputy", tags = {"employee_deputy"})
    @BadRequestAPIResponses
    @PutMapping("/{id}")
    @Override
    public EmployeeDeputyDto update(
        @Parameter(name = "id", description = "Идентификатор объекта employee_deputy (таблица employee_deputy).", example = "1")
        @PathVariable(name = "id") Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление employee_deputy (таблица employee_deputy)")
        @RequestBody EmployeeDeputyDto input) {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", id);
        loggerService.createLog(hash, String.format("PUT /api/employee-deputy/%d", id), getParams, input);
        EmployeeDeputyEntity entity = getService().update(id, e -> getAsm().update(e, input));
        return asm.toRes(entity);
    }

    @Operation(summary = "Удаление объекта employee_deputy", description = "Удаление объекта employee_deputy", tags = {"employee_deputy"})
    @BadRequestAPIResponses
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(
        @Parameter(name = "id", description = "Идентификатор evaluation_scale", example = "1")
        @PathVariable("id") Long id) {
        return super.delete(id);
    }
}
