package me.goodt.vkpht.module.orgstructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.dto.OperationResult;
import me.goodt.vkpht.module.orgstructure.api.dto.WorkExperienceTypeDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.WorkExperienceTypeFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.WorkExperienceTypeEntity;
import me.goodt.vkpht.common.api.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.WorkExperienceTypeService;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class WorkExperienceTypeController {

	private final WorkExperienceTypeService workExperienceTypeService;
	private final ILoggerService loggerService;

	@Operation(summary = "Получение инфорации о всех типах опыта работы", description = "Получение информации о всех типах опыта работы", tags = {"work_experience_type"})
    @SurProtected(operation = SurOperation.UNIT)
	@GetMapping("/api/workexperiencetype")
	public List<WorkExperienceTypeDto> getAll() {
		return workExperienceTypeService.findAll()
			.stream()
			.map(WorkExperienceTypeFactory::create)
			.collect(Collectors.toList());
	}

	@Operation(summary = "Получение инфорации о типе опыта работы", description = "Получение информации о типе опыта работы", tags = {"work_experience_type"})
	@BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
	@GetMapping("/api/workexperiencetype/{id}")
	public WorkExperienceTypeDto getById(
		@Parameter(name = "id", description = "Идентификатор типа опыта работы (таблица work_experience_type).", example = "1")
		@PathVariable(name = "id") Long id) throws NotFoundException {
		return WorkExperienceTypeFactory.create(workExperienceTypeService.findById(id));
	}

	@Operation(summary = "Создание типа опыта работы", description = "Создание типа опыта работы", tags = {"work_experience_type"})
	@BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
	@PostMapping("/api/workexperiencetype/create")
	public WorkExperienceTypeDto create(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление типа опыта работы (таблица work_experience_type)")
		@RequestBody WorkExperienceTypeDto dto) throws NotFoundException, JsonProcessingException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "POST /api/workexperiencetype", null, dto);
		WorkExperienceTypeEntity entity = workExperienceTypeService.create(dto);
		return WorkExperienceTypeFactory.create(entity);
	}

	@Operation(summary = "Изменение типа опыта работы", description = "Изменение типа опыта работы по идентификатору", tags = {"work_experience_type"})
	@BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
	@PutMapping("/api/workexperiencetype/update/{id}")
	public WorkExperienceTypeDto update(
		@Parameter(name = "id", description = "Идентификатор типа опыта работы (таблица work_experience_type).", example = "1")
		@PathVariable(name = "id") Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление типа опыта работы (таблица work_experience_type)")
		@RequestBody WorkExperienceTypeDto dto) throws NotFoundException, JsonProcessingException {
		UUID hash = UUID.randomUUID();
		Map<String, Object> getParams = new HashMap<>();
		getParams.put("id", id);
		loggerService.createLog(hash, String.format("PUT /api/workexperiencetype/%d", id), getParams, dto);
		dto.setId(id);
		WorkExperienceTypeEntity entity = workExperienceTypeService.update(dto);
		return WorkExperienceTypeFactory.create(entity);
	}

	@Operation(summary = "Удаление типа опыта работы", description = "Удаление типа опыта работы", tags = {"work_experience_type"})
	@BadRequestAPIResponses
    @SurProtected(operation = SurOperation.UNIT)
	@DeleteMapping("/api/workexperiencetype/delete/{id}")
	public OperationResult deletePositionType(@PathVariable(name = "id") Long id) {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, String.format("DELETE /api/workexperiencetype/delete/%d", id), null, null);
		try {
			workExperienceTypeService.delete(id);
			return new OperationResult(true, "");
		} catch (NotFoundException e) {
			return new OperationResult(false, "cannot find any work_experience_type with id = " + id);
		}
	}
}
