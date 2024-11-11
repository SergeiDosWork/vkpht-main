package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.dto.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionProfstandardDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionProfstandardInputDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionProfstandardFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionProfstandardEntity;
import me.goodt.vkpht.module.orgstructure.api.PositionProfstandardService;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class PositionProfstandardController {

    private final PositionProfstandardService positionProfstandardService;

    @Operation(summary = "Получение информации о всех cвязях должности с профстандартами", description = "Получение информации о всех cвязях должности с профстандартами", tags = {"org_position_profstandard"})
    @GetMapping("/api/position-profstandard/all")
    public List<PositionProfstandardDto> getAll() {
        List<PositionProfstandardEntity> entities = positionProfstandardService.getAll();
        return entities.stream().map(PositionProfstandardFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации о cвязи должности с профстандартами", description = "Получение информации о cвязи должности с профстандартами", tags = {"org_position_profstandard"})
    @GetMapping("/api/position-profstandard/{id}")
    public PositionProfstandardDto getById(@PathVariable(name = "id") Long id) throws NotFoundException {
        PositionProfstandardEntity entity = positionProfstandardService.getById(id);
        return PositionProfstandardFactory.create(entity);
    }

    @Operation(summary = "Создание cвязи должности с профстандартами", description = "Создание cвязи должности с профстандартами", tags = {"org_position_profstandard"})
    @BadRequestAPIResponses
    @PostMapping("/api/position-profstandard/create")
    public PositionProfstandardDto createPositionType(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление cвязи должности с профстандартами (таблица org_position_profstandard)")
            @RequestBody PositionProfstandardInputDto dto) throws NotFoundException {
        return PositionProfstandardFactory.create(positionProfstandardService.create(dto));
    }

    @Operation(summary = "Обновление cвязи должности с профстандартами", description = "Обновление cвязи должности с профстандартами", tags = {"org_position_profstandard"})
    @BadRequestAPIResponses
    @PutMapping("/api/position-profstandard/update/{id}")
    public PositionProfstandardDto updatePositionType(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление cвязи должности с профстандартами (таблица org_position_profstandard)")
            @RequestBody PositionProfstandardInputDto dto,
            @PathVariable(name = "id") Long id) throws NotFoundException {
        return PositionProfstandardFactory.create(positionProfstandardService.update(dto, id));
    }

    @Operation(summary = "Удаление cвязи должности с профстандартами", description = "Удаление cвязи должности с профстандартами", tags = {"org_position_profstandard"})
    @BadRequestAPIResponses
    @DeleteMapping("/api/position-profstandard/delete/{id}")
    public OperationResult deletePositionType(@PathVariable(name = "id") Long id) {
        try {
            positionProfstandardService.delete(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_type with id = " + id);
        }
    }
}
