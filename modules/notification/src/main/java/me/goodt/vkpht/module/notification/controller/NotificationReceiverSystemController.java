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
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemRequestDto;
import me.goodt.vkpht.module.notification.api.NotificationReceiverSystemService;
import me.goodt.vkpht.module.notification.api.logging.LoggerService;

@Slf4j
@RestController
public class NotificationReceiverSystemController {

	private final NotificationReceiverSystemService notificationReceiverSystemService;
	private final LoggerService loggerService;

	public NotificationReceiverSystemController(NotificationReceiverSystemService notificationReceiverSystemService, LoggerService loggerService) {
		this.notificationReceiverSystemService = notificationReceiverSystemService;
		this.loggerService = loggerService;
	}

	@Operation(summary = "Получение массива notification_receiver_system", description = "Получение массива notification_receiver_system", tags = {"notification_receiver_system"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если удалось получить данные по входным параметрам",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationReceiverSystemDto.class)))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-receiver-system")
    @SurProtected(operation = SurOperation.UNIT)
	public List<NotificationReceiverSystemDto> getAllNotificationReceiverSystem() {
		return notificationReceiverSystemService.getAllNotificationReceiverSystem();
	}

	@Operation(summary = "Получение notification_receiver_system по коду", description = "Получение notification_receiver_system по ID", tags = {"notification_receiver_system"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с чтением БД (см. таблицу notification_receiver_system) и удалось получить DTO представление этого объекта",
			content = @Content(schema = @Schema(implementation = NotificationReceiverSystemDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если шаблон не найден.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/notification-receiver-system/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationReceiverSystemDto findNotificationReceiverSystemById(
		@Parameter(name = "id", title = "Уникальный код шаблона")
		@PathVariable Long id) throws NotFoundException {
		return notificationReceiverSystemService.getNotificationReceiverSystemById(id);
	}

	@Operation(summary = "Создание notification_receiver_system", description = "Создание notification_receiver_system", tags = {"notification_receiver_system"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с созданием БД (см. таблицу notification_receiver_system)",
			content = @Content(schema = @Schema(implementation = NotificationReceiverSystemDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если что-то из представленных данных не найдено.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PostMapping("/api/notification-receiver-system/create")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationReceiverSystemDto createNotificationReceiverSystem(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление (таблица notification_receiver_system)")
		@RequestBody NotificationReceiverSystemRequestDto dto) {
		loggerService.createLog(UUID.randomUUID(), "POST /api/notification-receiver-system/create", null, dto);
		return notificationReceiverSystemService.createNotificationReceiverSystem(dto);
	}

	@Operation(summary = "Обновление notification_receiver_system", description = "Обновление notification_receiver_system", tags = {"notification_receiver_system"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с созданием БД (см. таблицу notification_receiver_system)",
			content = @Content(schema = @Schema(implementation = NotificationReceiverSystemDto.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если что-то из представленных данных не найдено.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PutMapping("/api/notification-receiver-system/update/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public NotificationReceiverSystemDto updateNotificationReceiverSystem(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление (таблица notification_receiver_system)")
		@RequestBody NotificationReceiverSystemRequestDto dto,
		@PathVariable Long id
	) throws NotFoundException {
		loggerService.createLog(UUID.randomUUID(), "PUT /api/notification-receiver-system/update/%d".formatted(id), null, dto);
		dto.setId(id);
		return notificationReceiverSystemService.updateNotificationReceiverSystem(dto);
	}

	@Operation(summary = "Удаление notification_receiver_system", description = "Удаление notification_receiver_system", tags = {"notification_receiver_system"})
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
	@DeleteMapping("/api/notification-receiver-system/delete/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public OperationResult deleteNotificationReceiverSystem(@PathVariable Long id) {
		loggerService.createLog(UUID.randomUUID(), "PUT /api/notification-receiver-system/delete/%d".formatted(id), null, null);
		if (notificationReceiverSystemService.deleteNotificationReceiverSystem(id)) {
			return new OperationResult(true, "");
		} else {
			return new OperationResult(false, "cannot find any notificationreceiversystem with id = " + id);
		}
	}
}
