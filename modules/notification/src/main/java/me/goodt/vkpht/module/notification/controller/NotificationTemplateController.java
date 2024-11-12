package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.notification.api.dto.data.OperationResult;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateFilter;
import me.goodt.vkpht.module.notification.domain.factory.NotificationTemplateFactory;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;
import me.goodt.vkpht.module.notification.api.NotificationTemplateService;
import me.goodt.vkpht.module.notification.api.logging.LoggerService;
import me.goodt.vkpht.module.notification.api.orgstructure.OrgstructureServiceClient;

@Slf4j
@RestController
public class NotificationTemplateController {

	@Autowired
	private NotificationTemplateService notificationTemplateService;

	@Autowired
	private OrgstructureServiceClient orgstructureService;

	@Autowired
	private LoggerService loggerService;

	@Operation(summary = "Получение notification_template", description = "Получение notification_template", tags = {"notification_template"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с чтением БД (см. таблицу notification_template) и удалось получить DTO представление этого объекта",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-template/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTemplateDto getNotificationTemplate(
		@Parameter(name = "id", title = "Идентификатор notification_template (таблица notification_template).", example = "1")
		@PathVariable Long id) throws NotFoundException {
		return NotificationTemplateFactory.create(notificationTemplateService.getById(id));
	}

	@Operation(summary = "Получение массива notification_template", description = "Получение массива notification_template", tags = {"notification_template"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если удалось получить данные по входным параметрам",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationTemplateDto.class)))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-template")
    @SurProtected(operation = SurOperation.UNIT)
	public Page<NotificationTemplateDto> getAllNotificationTemplate(NotificationTemplateFilter filter,
																	@ParameterObject Pageable paging) {
		return notificationTemplateService.getAll(filter, paging)
			.map(NotificationTemplateFactory::create);
	}

	@Operation(summary = "Создание notification_template", description = "Создание notification_template", tags = {"notification_template"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с записью в БД (см. таблицу notification_template) и удалось создать этот объект",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PostMapping("/api/notification-template")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTemplateDto createNotificationTemplate(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление шаблона (таблица notification_template)")
		@RequestBody NotificationTemplateDto dto) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "POST /api/notification-template", null, dto);

		Long employeeId = orgstructureService.getEmployeeId();
		return NotificationTemplateFactory.create(notificationTemplateService.create(dto, employeeId));
	}

	@Operation(summary = "Изменение notification_template", description = "Изменение notification_template", tags = {"notification_template"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с чтением и записью в БД (см. таблицу notification_template) и удалось обновить этот объект",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PutMapping("/api/notification-template/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTemplateDto updateNotificationTemplate(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление шаблона (таблица notification_template)")
		@RequestBody NotificationTemplateDto dto,
		@Parameter(name = "id", title = "Идентификатор email (таблица notification_template).", example = "1")
		@PathVariable Long id) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "PUT /api/notification-template/%d".formatted(id), null, dto);

		Long employeeId = orgstructureService.getEmployeeId();
		return NotificationTemplateFactory.create(notificationTemplateService.update(id, dto, employeeId));
	}

	@Operation(summary = "Удаление notification_template", description = "Удаление notification_template", tags = {"notification_template"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если объект в БД был успешно удален",
			content = @Content(schema = @Schema(implementation = OperationResult.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@DeleteMapping("/api/notification-template/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public OperationResult deleteNotificationTemplate(
		@Parameter(name = "id", title = "Идентификатор notification_template (таблица notification_template).", example = "1")
		@PathVariable Long id) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "DELETE /api/notification-template/%d".formatted(id), null, null);

		NotificationTemplateEntity entity = notificationTemplateService.getById(id);
		if (Objects.isNull(entity.getDateTo())) {
			Long employeeId = orgstructureService.getEmployeeId();
			notificationTemplateService.delete(id, employeeId);
			return new OperationResult(true, "");
		} else {
			return new OperationResult(false, "notification_template with id=%d is already closed".formatted(id));
		}
	}

	@Operation(summary = "Получение notification_template по коду", description = "Получение notification_template по коду", tags = {"email"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с чтением БД (см. таблицу notification_template) и удалось получить DTO представление этого объекта",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notificationtemplate/findbycode")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTemplateDto findNotificationTemplateByCode(
		@Parameter(name = "code", title = "Уникальный код шаблона")
		@RequestParam String code) throws NotFoundException {
		return NotificationTemplateFactory.create(notificationTemplateService.getNotificationTemplateByCode(code));
	}

	@Operation(summary = "Получение списка notification_template_content по коду", description = "Получение списка notification_template_content по коду", tags = {"email"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с чтением БД (см. таблицу notification_template, notification_template_content) и удалось получить DTO представление этих объектов",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationTemplateContentDto.class)))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notificationtemplatecontent/findbycode")
	public List<NotificationTemplateContentDto> findNotificationTemplateContentByCode(
		@Parameter(name = "code", title = "Уникальный код шаблона")
		@RequestParam String code) {
		return notificationTemplateService.findNotificationTemplateContentByCode(code);
	}
}
