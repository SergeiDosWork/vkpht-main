package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import com.goodt.drive.rtcore.data.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.CitizenshipDto;
import me.goodt.vkpht.module.orgstructure.api.dto.CitizenshipInputDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.CitizenshipFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;
import me.goodt.vkpht.module.orgstructure.api.ICitizenshipService;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class CitizenshipController {

    private final ICitizenshipService citizenshipService;

    @Operation(summary = "Получение информации о гражданстве", description = "Получение информации о гражданстве", tags = {"org_citizenship"})
    @GetMapping("/api/citizenship/{id}")
    public CitizenshipDto getById(@PathVariable(name = "id") Long id) throws NotFoundException {
        CitizenshipEntity entity = citizenshipService.getById(id);
        return CitizenshipFactory.create(entity);
    }

    @Operation(summary = "Получение информации о всех актуальных гражданствах",
        description = "Получение информации о всех актуальных гражданствах",
        tags = {"org_citizenship"})
    @GetMapping("/api/citizenship/all")
    public List<CitizenshipDto> getAll() {
        List<CitizenshipEntity> entities = citizenshipService.getAllActual();
        return entities.stream().map(CitizenshipFactory::create).collect(Collectors.toList());
    }

    @Operation(summary = "Создание cвязи должности с профстандартами", description = "Создание гражданства", tags = {"org_citizenship"})
    @BadRequestAPIResponses
    @PostMapping("/api/citizenship/create")
    public CitizenshipDto createPositionType(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO гражданства (таблица org_citizenship)")
        @RequestBody CitizenshipInputDto dto) throws NotFoundException {
        return CitizenshipFactory.create(citizenshipService.create(dto));
    }

    @Operation(summary = "Обновление cвязи должности с профстандартами", description = "Обновление гражданства", tags = {"org_citizenship"})
    @BadRequestAPIResponses
    @PutMapping("/api/citizenship/update/{id}")
    public CitizenshipDto updatePositionType(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление гражданства (таблица org_citizenship)")
        @RequestBody CitizenshipInputDto dto,
        @PathVariable(name = "id") Long id) throws NotFoundException {
        return CitizenshipFactory.create(citizenshipService.update(dto, id));
    }

    @Operation(summary = "Удаление cвязи должности с профстандартами", description = "Удаление гражданства", tags = {"org_citizenship"})
    @DeleteMapping("/api/citizenship/delete/{id}")
    public OperationResult deletePositionType(@PathVariable(name = "id") Long id) {
        try {
            citizenshipService.delete(id);
            return new OperationResult(true, "");
        } catch (NotFoundException e) {
            return new OperationResult(false, "cannot find any position_type with id = " + id);
        }
    }

}
