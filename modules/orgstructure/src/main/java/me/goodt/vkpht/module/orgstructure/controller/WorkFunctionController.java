package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.dto.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkFunctionDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.WorkFunctionFactory;
import me.goodt.vkpht.common.api.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.WorkFunctionService;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class WorkFunctionController {

    private final WorkFunctionService workFunctionService;
    private final ILoggerService loggerService;

    @Operation(summary = "Получение информации о рабочей функции", description = "Получение информации о рабочей функции", tags = {"work_function"})
    @BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/workfunction/{id}")
    public WorkFunctionDto get(
            @Parameter(name = "id", description = "Идентификатор рабочей функции (таблица work_function)")
            @PathVariable("id") Long id) throws NotFoundException {
        return WorkFunctionFactory.create(workFunctionService.get(id));
    }

    @Operation(summary = "Получение информации о всех актуальных рабочих функциях",
            description = "Получение информации о всех актуальных рабочих функциях",
            tags = {"work_function"})
    @SurProtected(operation = SurOperation.UNIT)
    @GetMapping("/api/workfunction")
    public List<WorkFunctionDto> get() {
        return workFunctionService.get().stream()
                .map(WorkFunctionFactory::create)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Создание рабочей функции", description = "Создание рабочей функции", tags = {"work_function"})
    @SurProtected(operation = SurOperation.UNIT)
    @BadRequestAPIResponses
    @PostMapping("/api/workfunction")
    public WorkFunctionDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление объекта")
            @RequestBody WorkFunctionDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/workfunction", null, dto);
        return WorkFunctionFactory.create(workFunctionService.create(dto));
    }

    @Operation(summary = "Обновление рабочей функции", description = "Обновление рабочей функции", tags = {"work_function"})
    @SurProtected(operation = SurOperation.UNIT)
    @BadRequestAPIResponses
    @PutMapping("/api/workfunction/{id}")
    public WorkFunctionDto update(
            @Parameter(name = "id", description = "Идентификатор рабочей функции (таблица work_function)")
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление объекта")
            @RequestBody WorkFunctionDto dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("PUT /api/workfunction/%d", id), null, dto);
        return WorkFunctionFactory.create(workFunctionService.update(id, dto));
    }

    @Operation(summary = "Удаление рабочей функции", description = "Удаление рабочей функции", tags = {"work_function"})
    @SurProtected(operation = SurOperation.UNIT)
    @BadRequestAPIResponses
    @DeleteMapping("/api/workfunction/{id}")
    public OperationResult delete(
            @Parameter(name = "id", description = "Идентификатор рабочей функции (таблица work_function)")
            @PathVariable("id") Long id) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, String.format("DELETE /api/workfunction/%d", id), null, null);
        try {
            workFunctionService.delete(id);
            return new OperationResult(true, "");
        } catch (RuntimeException ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }
}
