/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@Operation(summary = "Проверка соединения с сервисом", description = "Проверка соединения с сервисом", tags = {"ping"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "В случае если сервис отвечает",
			content = @Content(schema = @Schema(implementation = Long.class))),
		@ApiResponse(responseCode = "500", description = "В случае если: что-то сломалось или не полностью сконфигрурировано приложение",
			content = @Content(schema = @Schema(implementation = String.class))),
	})
	@GetMapping("/api/ping")
	public Long get() {
		return 1L;
	}
}
