package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.NotificationLogService;
import me.goodt.vkpht.module.notification.api.dto.NotificationLogDto;
import me.goodt.vkpht.module.notification.api.dto.kafka.NotificationLogResponseKafkaDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification-log")
public class NotificationLogController {

	private final NotificationLogService notificationLogService;

	@Operation(summary = "Обновление статуса отправки уведомления в Журнале уведомлений", description = "Обновление статуса отправки уведомления в Журнале уведомлений на основе ответа из kafka-messenger", tags = {"notification_log"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с обновлением БД (см. таблицу notification_log)",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если что-то из представленных данных не найдено.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@PutMapping(value = "/update-status")
    @SurProtected(operation = SurOperation.UNIT)
	public void updateStatus(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO представление ответа от kafka-messenger")
		@RequestBody NotificationLogResponseKafkaDto notificationLogResponseKafkaDto) {
		notificationLogService.updateStatusFromOutside(notificationLogResponseKafkaDto);
	}

	@Operation(summary = "Загрузка карточки в Журнале уведомлений", description = "Загрузка карточки в Журнале уведомлений", tags = {"notification_log"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с загрузкой данных из БД (см. таблицу notification_log)",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "403", description = "В случае если пользователь не является HR",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "404", description = "В случае если что-то из представленных данных не найдено.",
			content = @Content(schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping(value = "/{id}")
    @SurProtected(operation = SurOperation.UNIT)
	public ResponseEntity<NotificationLogDto> load(@PathVariable Long id) throws NotFoundException {
		return ResponseEntity.ok(notificationLogService.load(id));
	}

}
