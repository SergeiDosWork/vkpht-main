package me.goodt.vkpht.common.api.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.goodt.drive.rtcore.api.error.ErrorResponse;

/**
 * Аннотация Swagger документации для добавления стандартных API ответов ошибок с кодами 400 и 404.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "400",
        description = "Указаны некорректные входные данные.",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Запрашиваемый объект с указанным идентификатором не найден в БД",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
})
public @interface BadRequestAPIResponses {
}
