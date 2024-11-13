package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.notification.api.dto.data.OperationResult;
import me.goodt.vkpht.module.notification.api.dto.NotificationDynamicRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;
import me.goodt.vkpht.module.notification.api.NotificationRecipientService;
import me.goodt.vkpht.common.api.LoggerService;

@Slf4j
@RestController
public class NotificationRecipientController {

	private final NotificationRecipientService notificationRecipientService;
	private final LoggerService loggerService;

	public NotificationRecipientController(NotificationRecipientService notificationRecipientService, LoggerService loggerService) {
		this.notificationRecipientService = notificationRecipientService;
		this.loggerService = loggerService;
	}

	@Operation(summary = "Получение массива notification_recipient", description = "Получение массива notification_recipient", tags = {"notification_recipient"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если удалось получить данные по входным параметрам",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationRecipientDto.class)))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-recipient")
    @SurProtected(operation = SurOperation.UNIT)
	public List<NotificationRecipientDto> getAllNotificationRecipient() {
		return notificationRecipientService.getAllNotificationRecipient();
	}

	@Operation(summary = "Получение notification_recipient", description = "Получение notification_recipient", tags = {"notification_recipient"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с чтением БД (см. таблицу notification_recipient) и удалось получить DTO представление этого объекта",
			content = @Content(schema = @Schema(implementation = NotificationRecipientDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-recipient/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationRecipientDto getNotificationRecipient(
		@Parameter(name = "id", description = "Идентификатор notification_recipient (таблица notification_recipient).", example = "1")
		@PathVariable Long id) throws NotFoundException {
		return notificationRecipientService.getNotificationRecipient(id);
	}

	@Operation(summary = "Создание notification_recipient", description = "Создание notification_recipient", tags = {"notification_recipient"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с созданием БД (см. таблицу notification_recipient)",
			content = @Content(schema = @Schema(implementation = NotificationRecipientDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если что-то из представленных данных не найдено.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PostMapping("/api/notification-recipient/create")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationRecipientDto createNotificationRecipient(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление (таблица notification_recipient)")
		@RequestBody NotificationRecipientDto dto) {
		loggerService.createLog(UUID.randomUUID(), "POST /api/notification-recipient/create", null, dto);
		return notificationRecipientService.createNotificationRecipient(dto);
	}

	@Operation(summary = "Обновление notification_recipient", description = "Обновление notification_recipient", tags = {"notification_recipient"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с созданием БД (см. таблицу notification_recipient)",
			content = @Content(schema = @Schema(implementation = NotificationRecipientDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если что-то из представленных данных не найдено.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PutMapping("/api/notification-recipient/update/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationRecipientDto updateNotificationRecipient(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление (таблица notification_recipient)")
		@RequestBody NotificationRecipientDto dto,
		@PathVariable Long id
	) throws NotFoundException {
		loggerService.createLog(UUID.randomUUID(), "PUT /api/notification-recipient/update/%d".formatted(id), null, dto);
		dto.setId(id);
		return notificationRecipientService.updateNotificationRecipient(dto);
	}

	@Operation(summary = "Удаление notification_recipient", description = "Удаление notification_recipient", tags = {"notification_recipient"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если объект в БД был успешно удален)",
			content = @Content(schema = @Schema(implementation = OperationResult.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@DeleteMapping("/api/notification-recipient/delete/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public OperationResult deleteNotificationRecipient(@PathVariable Long id) {
		loggerService.createLog(UUID.randomUUID(), "PUT /api/notification-recipient/delete/%d".formatted(id), null, null);
		if (notificationRecipientService.deleteNotificationRecipient(id)) {
			return new OperationResult(true, "");
		} else {
			return new OperationResult(false, "cannot find any notificationrecipient with id = " + id);
		}
	}

	@Operation(summary = "Получение вычисляемых получателей",
		description = "Получение записей из таблицы notification_recipient, " +
			"исключая static_employee и static_division",
		tags = {"notification_recipient"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",
			description = "В случае если не возникло проблем удалось получить вычисляемых получателей",
			content = @Content(schema = @Schema(implementation = NotificationDynamicRecipientDto.class))),
		@ApiResponse(responseCode = "401",
			description = "В случае если запрос отправлен без токена или с недействительным токеном",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500",
			description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-recipient/dynamic")
    @SurProtected(operation = SurOperation.UNIT)
	public List<NotificationDynamicRecipientDto> getDynamicRecipients() {
		return notificationRecipientService.getDynamicRecipients();
	}
}
