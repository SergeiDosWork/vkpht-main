package me.goodt.vkpht.common.api.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import me.goodt.vkpht.common.api.error.ErrorResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация Swagger документации для добавления стандартного набора API ответов.
 *
 * <p>При добавлении на класс @RestController добавляет ответы ко всем методам контроллера
 * <pre>{@code
 * @GeneralAPIResponses
 * @RestController
 * public class OperationController {
 *
 *     // мapped methods...
 * }}
 * </pre>
 *
 * <p>Возможно переопределение одного или несколько из описанных ответов:
 * <pre>{@code
 * @GeneralAPIResponses
 * @PostMapping("/start")
 * @ApiResponse(responseCode = "200", description = "Операция успешно запущена.")
 * public void startOperation() {
 *     // method implementation
 * }}
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Запрос успешно обработан"),
    @ApiResponse(
        responseCode = "401",
        description = "Запрос не содержит токен авторизации или указанный токен недействителен.",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "Недостаточно прав для выполнения запроса.",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Внутренняя ошибка сервера",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface GeneralAPIResponses {
    String successCode() default "200";
}
