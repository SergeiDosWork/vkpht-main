package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.NotificationTemplateContentService;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentFilter;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentRequest;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentResponse;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentShortResponse;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentSubstituteInfo;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateDto;
import me.goodt.vkpht.module.notification.api.dto.data.OperationResult;

@Tag(name = "notification_template_content", description = "API для работы с шаблонами уведомлений о событиях")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification-template-content")
public class NotificationTemplateContentController {

	private final LoggerService loggerService;

	private final NotificationTemplateContentService notificationTemplateContentService;

	@Operation(summary = "Получение шаблона уведомления",
		description = "Получение notification_template_content",
		tags = "notification_template_content")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = NotificationTemplateContentResponse.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или " +
			"с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон уведомления не найден",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTemplateContentResponse getById(
		@Parameter(name = "id", description = "Уникальный идентификатор шаблона уведомления", example = "1")
		@PathVariable Long id) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "GET /api/notification-template-content", Map.of("id", id), null);

		return notificationTemplateContentService.getById(id);
	}

	@Operation(summary = "Получение списка шаблонов уведомлений",
		description = "Получение массива notification_template_content",
		tags = "notification_template_content")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(array = @ArraySchema(
				schema = @Schema(implementation = NotificationTemplateContentShortResponse.class)))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping
    @SurProtected(operation = SurOperation.UNIT)
	public List<NotificationTemplateContentShortResponse> findAll(NotificationTemplateContentFilter filter) {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "GET /api/notification-template-content", null, null);

		return notificationTemplateContentService.findAll(filter);
	}

	@Operation(summary = "Создание шаблона уведомлений",
		description = "Создание notification_template_content",
		tags = "notification_template_content")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если связанные сущности не найдены",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
    @PostMapping
    @SurProtected(operation = SurOperation.UNIT)
    public NotificationTemplateContentResponse create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Информация о шаблоне уведомления")
        @RequestBody NotificationTemplateContentRequest dto) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/notification-template-content", null, dto);

        return notificationTemplateContentService.create(dto);
    }

	@Operation(summary = "Изменение шаблона уведомления",
		description = "Изменение notification_template_content",
		tags = "notification_template_content")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PutMapping("/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTemplateContentResponse update(
		@Parameter(name = "id", description = "Уникальный идентификатор шаблона уведомления", example = "1")
		@PathVariable Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Информация о шаблоне уведомления")
		@RequestBody NotificationTemplateContentRequest dto) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "PUT /api/notification-template-content/%d".formatted(id),
								null, null);

		return notificationTemplateContentService.update(id, dto);
	}

	@Operation(summary = "Удаление шаблона уведомления",
		description = """
			Удаление записи notification_template_content.
			
			Физического удаления не производится. Запись архивируется: производится проставление даты в поле dateTo.""",
		tags = "notification_template_content")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = OperationResult.class))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если удаляемый шаблон уведомления не найден",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@DeleteMapping("/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public OperationResult delete(
		@Parameter(name = "id", description = "Уникальный идентификатор шаблона уведомления", example = "1")
		@PathVariable Long id) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "DELETE /api/notification-template-content/%d".formatted(id),
								null, null);

		notificationTemplateContentService.delete(id);
		return new OperationResult(true, "");
	}

	@Operation(summary = "Получение списка замещающих шаблонов для определенного события",
		description = "Получение краткой информации из таблицы notification_template_content. При полученном " +
			"notificationTemplateContentId шаблон с данным идентификатором исключается",
		tags = "notification_template_content")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(array = @ArraySchema(
				schema = @Schema(implementation = NotificationTemplateContentSubstituteInfo.class)))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/substitute")
    @SurProtected(operation = SurOperation.UNIT)
	public List<NotificationTemplateContentSubstituteInfo> findSubstitutes(
		@Parameter(name = "notificationTemplateId", description = "Уникальный идентификатор события", example = "1", required = true)
		@RequestParam(value = "notificationTemplateId") Long templateId,
		@Parameter(name = "notificationTemplateContentId", description = "Уникальный идентификатор шаблона", example = "1")
		@RequestParam(value = "notificationTemplateContentId", required = false) Long contentId
	) {
		UUID hash = UUID.randomUUID();
		Map<String, Object> getParams = new HashMap<>();
		getParams.put("notificationTemplateId", templateId);
		getParams.put("notificationTemplateContentId", contentId);
		loggerService.createLog(hash, "GET /api/notification-template-content/substitute",
								getParams, null);

		return notificationTemplateContentService.findSubstitutes(templateId, contentId);
	}
}
