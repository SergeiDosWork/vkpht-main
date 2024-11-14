package me.goodt.vkpht.common.application.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.common.api.dto.error.ErrorResponse;
import me.goodt.vkpht.common.api.dto.error.ValidationErrorResponse;

import me.goodt.vkpht.common.api.exception.ForbiddenException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurAccessException;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.NotFoundException;

/**
 * Компонент обработки всех ошибок, возникших на стороне API.
 *
 * <p>Единый обработчик всех выбрасываемых исключений из всех
 * контроллеров сервиса.
 * <p>Формирует объект {@link ErrorResponse} в качестве возвращаемого тела ответа.
 */
@Slf4j
@ControllerAdvice
@ConditionalOnProperty(value = "appConfig.error-response-string-format", havingValue = "false", matchIfMissing = true)
public class ApiExceptionHandler {

    /**
     * Требуется авторизация. Токен отсутствует или он невалиден.
     */
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDenied(HttpServletRequest request,
                                                            AccessDeniedException e) {
        log.debug("Access to resource is forbidden (403) for request: \"{}\" message: \"{}\"",
                  request.getRequestURL(), e.getMessage());

        ErrorResponse response = new ErrorResponse(403, "AccessDenied", "Доступ запрещен: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Ошибка доступа к сущности по СУР
     */
    @ExceptionHandler(value = { SurAccessException.class })
    public ResponseEntity<ErrorResponse> handleSurAccess(HttpServletRequest request, SurAccessException e) {
        log.debug("SUR: Access to resource is forbidden (403) for request: \"{}\" message: \"{}\"",
                  request.getRequestURL(), e.getMessage());

        ErrorResponse response = new ErrorResponse(403, "SurAccessDenied", "Доступ запрещен по СУР: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Запрашиваемая (чаще всего по идентификатору) сущность не найдена.
     */
    @ExceptionHandler({ EntityNotFoundException.class, NotFoundException.class })
    public ResponseEntity<ErrorResponse> handleEntityNotFound(Exception e) {
        ErrorResponse response = new ErrorResponse(404, "EntityNotFound", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Общая исключение, сигнализируещее о "плохом запросе".
     */
    @ExceptionHandler({ BadRequestException.class, ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleBindException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(
                400,
                "BadRequest",
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Текущее состояние данных не позволяет применить к ним запрашиваемое действие.
     */
    @ExceptionHandler({ ForbiddenException.class })
    public ResponseEntity<ErrorResponse> handleNotAcceptable(ForbiddenException e) {
        ErrorResponse response = new ErrorResponse(
            406,
            "NotAcceptable",
            e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Во время сохранения данных в СУБД возникли конфликты в ограничениях (constraint violation)
     * по первичому, уникальному или внешнему ключу.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflicts(HttpServletRequest request,
                                                         DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        Throwable rootCause = exception.getRootCause();
        if (rootCause != null && rootCause.getLocalizedMessage() != null) {
            message = rootCause.getLocalizedMessage();
        }
        log.error("Conflict (409) for request: {} {} error message: {}",
                  request.getMethod(), request.getRequestURL(), message, exception);

        ErrorResponse response = new ErrorResponse(409, "Conflict", message);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Нехватка выделенной оперативной памяти для обработки запроса.
     * Ошибка на уровне JVM.
     */
    @ExceptionHandler(value = { OutOfMemoryError.class })
    public ResponseEntity<ErrorResponse> handleOutOfMemory(OutOfMemoryError error) {
        log.error("Out of memory error: {}", ExceptionUtils.getStackTrace(error));

        ErrorResponse response = new ErrorResponse(500, "InternalError", "Out of memory: " + error.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Внутренняя ошибка сервера с возвратом клиенту сообщения об ошибке.
     */
    @ExceptionHandler(value = { InternalServerException.class })
    public ResponseEntity<ErrorResponse> handleInternalServerExceptions(InternalServerException e) {
        log.error("InternalServerException occurred while handling the request.", e);

        ErrorResponse errorResponse = new ErrorResponse(500, "InternalServerError", e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /////////////////////////////////////////////
    // Внетренние исключения от Spring MVC:

    /**
     * Отсутствует обязательный параметр, помеченный аннотацией @RequestParam
     */
    @ExceptionHandler({ MissingServletRequestParameterException.class })
    public ResponseEntity<ErrorResponse> handleBindException(MissingServletRequestParameterException e) {
        ErrorResponse response = new ErrorResponse(
            400,
            "BadRequest",
            "Отсутствует обязательный параметр '" + e.getParameterName() + "'"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка в неверном типе передаваемого параметра, возникающие при попытке
     * привязки значения из запроса на указываемые поля/аргумент в @RequestParam или @RequestBody.
     */
    @ExceptionHandler({ TypeMismatchException.class })
    public ResponseEntity<ErrorResponse> handleTypeMismatch(TypeMismatchException e) {
        String message = formatTypeMismatchMessage(e);

        ErrorResponse response = new ErrorResponse(400, "BadRequest", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * Ошибка в привязке значений из запроса на указываемые аргументы метода контроллера.
     *
     * <p>Также для ошибок валидации данных, при использовании аннотации @{@link javax.validation.Valid}.
     */
    @ExceptionHandler({ BindException.class })
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        List<String> constraints = new ArrayList<>(e.getAllErrors().size());
        for (ObjectError error : e.getAllErrors()) {
            constraints.add(formatFieldErrorMessage(error));
        }

        ErrorResponse response = new ValidationErrorResponse(
            400,
            "BadRequest",
            "Получены ошибки при проверке данных входящего запроса.",
            constraints
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Отсуствует обработчик или маппинг для полученного запроса
     */
    @ExceptionHandler({ NoHandlerFoundException.class })
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException e) {
        ErrorResponse response = new ErrorResponse(
            404,
            "NotFound",
            "URL " + e.getRequestURL() + " не найден"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Недопустимый HTTP метод при обращении к указанному URL.
     */
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<ErrorResponse> handleMethodNoAllowed(HttpRequestMethodNotSupportedException e) {
        ErrorResponse response = new ErrorResponse(405, "MethodNotAllowed", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Тело ответа(JSON) повреждено или в невалидном формате.
     */
    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException e) {
        ErrorResponse response = new ErrorResponse(
            400,
            "BadRequest",
            "Невозможно прочитать тело запроса: " + e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /////////////////////////////////////////////

    /**
     * Все оставшиеся неотловленные исключения.
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleUncaughtException(HttpServletRequest request, Exception e) {
        log.error(String.format("Uncaught exception while handling \"%s\" to API %s",
                                request.getMethod(), request.getRequestURL()), e);

        ErrorResponse response = new ErrorResponse(
            500,
            "InternalServerError",
            // TODO: Только для "dev" профиля стоит подставлять в сообщение реальный e.getMessage()
            //  и, возможно, еще и stacktrace.
            e.getMessage()
            // TODO: Для "прода" делать статичную заглушку
            // "Во время обработки запроса произошла внутренняя ошибка."
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /////////////////////////////////////////////

    private static String formatTypeMismatchMessage(TypeMismatchException e) {
        if (Integer.class.equals(e.getRequiredType()) || Long.class.equals(e.getRequiredType())) {
            return "Значение '" + e.getValue() + "' не является допустимым целочисленным значением.";
        }

        if (LocalDate.class.equals(e.getRequiredType()) || Date.class.equals(e.getRequiredType())) {
            return "Значение '" + e.getValue() + "' не является допустимым значением даты.";
        }

        String name = e.getPropertyName();
        if (e instanceof MethodArgumentTypeMismatchException) {
            name = ((MethodArgumentTypeMismatchException) e).getName();
        }
        return "Значение '" + e.getValue() + "' недопустимо для параметра " + name;
    }

    private String formatFieldErrorMessage(ObjectError error) {
        if (error.contains(TypeMismatchException.class)) {
            return formatTypeMismatchMessage(error.unwrap(TypeMismatchException.class));
        }

        if (error instanceof FieldError) {
            FieldError fieldError = (FieldError) error;
            return String.format("Значение для поля %s %s.", fieldError.getField(), fieldError.getDefaultMessage());
        }

        return error.getObjectName() + " " + error.getDefaultMessage();
    }
}
