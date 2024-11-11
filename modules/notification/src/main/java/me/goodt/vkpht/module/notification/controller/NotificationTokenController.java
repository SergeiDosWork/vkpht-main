package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.NotificationTokenService;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTokenDto;
import me.goodt.vkpht.module.notification.api.dto.OperationResult;

@Tag(name = "notification_token", description = "API для работы с токенами уведомлений о событиях")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification-token")
public class NotificationTokenController {

	private final LoggerService loggerService;

	private final NotificationTokenService notificationTokenService;

	@Operation(summary = "Получение токена уведомлений", description = "Получение notification_token",
		tags = "notification_token")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = NotificationTokenDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или " +
			"с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если токен не найден",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationTokenDto getById(
		@Parameter(name = "id", description = "Уникальный идентификатор токена", example = "1")
		@PathVariable Long id) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "GET /api/notification-tokens", Map.of("id", id), null);

		return notificationTokenService.getById(id);
	}

	@Operation(summary = "Получение списка токенов уведомлений", description = "Получение массива notification_token",
		tags = "notification_token")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationTokenDto.class)))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping
    @SurProtected(operation = SurOperation.UNIT)
	public List<NotificationTokenDto> getAll() {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "GET /api/notification-tokens", null, null);

		return notificationTokenService.getAll();
	}
    @Hidden
	@Operation(summary = "Создание токена уведомлений", description = "Создание notification_token",
		tags = "notification_token")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = NotificationTemplateDto.class))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PostMapping
	public NotificationTokenDto create(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление с информацией о токене")
		@RequestBody NotificationTokenDto tokenDto) {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "POST /api/notification-tokens", null, tokenDto);

		return notificationTokenService.create(tokenDto);
	}

	@Operation(summary = "Изменение токена уведомления", description = "Изменение notification_token",
		tags = "notification_token")
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
	public NotificationTokenDto update(
		@Parameter(name = "id", description = "Уникальный идентификатор токена", example = "1")
		@PathVariable Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление с информацией о токене")
		@RequestBody NotificationTokenDto tokenDto) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "PUT /api/notification-tokens/%d".formatted(id),
								null, null);

		return notificationTokenService.update(id, tokenDto);
	}

	@Operation(summary = "Удаление токена уведомления", description = "Удаление notification_token",
		tags = "notification_token")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если операция произведена успешно",
			content = @Content(schema = @Schema(implementation = OperationResult.class))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если токен уведомления не найден",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@DeleteMapping("/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public OperationResult delete(@Parameter(name = "id", description = "Уникальный идентификатор токена",
		example = "1") @PathVariable Long id) throws NotFoundException {
		UUID hash = UUID.randomUUID();
		loggerService.createLog(hash, "DELETE /api/notification-tokens/%d".formatted(id),
								null, null);

		notificationTokenService.delete(id);
		return new OperationResult(true, "Success");
	}
}
