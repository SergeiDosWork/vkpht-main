package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.SubscribeEmployeeToNotificationService;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentEmployeeSubscribeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.notification.api.orgstructure.OrgstructureServiceAdapter;
import me.goodt.vkpht.module.notification.application.utils.TextConstants;

@RestController
@RequiredArgsConstructor
public class SubscribeEmployeeToNotificationController {

	private final SubscribeEmployeeToNotificationService subscribeEmployeeToNotificationService;

	private final OrgstructureServiceAdapter orgstructureServiceAdapter;

	@Operation(summary = "Загрузка Управление подписками", description = "Получение данных по управлению подписками пользователя", tags = {"subscribe"})
	@GetMapping("/api/subscribe-employee-to-notification")
	public ResponseEntity<NotificationTemplateContentEmployeeSubscribeDto> load(
		@RequestParam Long employeeId,
		@RequestParam String receiverSystemName) throws NotFoundException {

		checkExistsEmployee(employeeId);
		return ResponseEntity.ok(subscribeEmployeeToNotificationService.load(employeeId, receiverSystemName));
	}

	@Operation(summary = "Сохранение Управление подписками", description = "Сохранение данных по управлению подписками пользователя", tags = {"subscribe"})
	@PostMapping("/api/subscribe-employee-to-notification/save")
	public ResponseEntity<NotificationTemplateContentEmployeeSubscribeDto> save(@RequestBody NotificationTemplateContentEmployeeSubscribeDto dto) throws NotFoundException {
		checkExistsEmployee(dto.getEmployeeId());
		subscribeEmployeeToNotificationService.save(dto);
		return ResponseEntity.ok(subscribeEmployeeToNotificationService.load(dto.getEmployeeId(), dto.getReceiverSystemName()));
	}

	private void checkExistsEmployee(Long employeeId) {
		EmployeeInfoResponse employee = orgstructureServiceAdapter.findEmployee(Collections.singletonList(employeeId));
		if (Objects.isNull(employee) || CollectionUtils.isEmpty(employee.getData())) {
			throw new NotFoundException(TextConstants.ORGSTRUCTURE_CANNOT_FIND_EMPLOYEE);
		}
	}

}
