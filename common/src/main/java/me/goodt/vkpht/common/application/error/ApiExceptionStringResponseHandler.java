package me.goodt.vkpht.common.application.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.ForbiddenException;
import me.goodt.vkpht.common.api.exception.InternalServerException;
import me.goodt.vkpht.common.api.exception.NotFoundException;

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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurAccessException;
import com.goodt.drive.auth.sur.unit.IncorrectUnitException;

// TODO Удалить после реализации на фронтенде обработки ErrorResponse
@Slf4j
@ControllerAdvice
@ConditionalOnProperty(value = "appConfig.error-response-string-format", havingValue = "true")
public class ApiExceptionStringResponseHandler {

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

    /**
     * Требуется авторизация. Токен отсуствует или он невалиден.
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<String> handleAccessDenied(HttpServletRequest request,
                                                     AccessDeniedException e) {
        log.debug("Access to resource is forbidden (403) for request: \"{}\" message: \"{}\"",
                  request.getRequestURL(), e.getMessage());

        String response = "Доступ запрещен: " + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Ошибка доступа к сущности по СУР
     */
    @ExceptionHandler(value = {SurAccessException.class})
    public ResponseEntity<String> handleSurAccess(HttpServletRequest request, SurAccessException e) {
        log.debug("SUR: Access to resource is forbidden (403) for request: \"{}\" message: \"{}\"",
                  request.getRequestURL(), e.getMessage());

        String response = "Доступ запрещен по СУР: " + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {IncorrectUnitException.class})
    public ResponseEntity<String> handleUnitAccess(HttpServletRequest request, IncorrectUnitException e) {
        log.debug("UNIT: Access to unit is forbidden (403) for request: \"{}\" message: \"{}\"",
                request.getRequestURL(), e.getMessage());

        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Запрашиваемая (чаще всего по идентификатору) сущность не найдена.
     */
    @ExceptionHandler({EntityNotFoundException.class, NotFoundException.class})
    public ResponseEntity<String> handleEntityNotFound(Exception e) {
        String response = e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Общая исключение, сигнализируещее о "плохом запросе".
     */
    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleBindException(BadRequestException e) {
        String response = e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Текущее состояние данных не позволяет применить к ним запрашиваемое действие.
     */
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<String> handleNotAcceptable(ForbiddenException e) {
        String response = e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Во время сохранения данных в СУБД возникли конфликты в ограничениях (constraint violation)
     * по первичому, уникальному или внешнему ключу.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleConflicts(HttpServletRequest request,
                                                  DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        Throwable rootCause = exception.getRootCause();
        if (rootCause != null && rootCause.getLocalizedMessage() != null) {
            message = rootCause.getLocalizedMessage();
        }
        log.error("Conflict (409) for request: {} {} error message: {}",
                  request.getMethod(), request.getRequestURL(), message, exception);

        String response = message;
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Внутренняя ошибка сервера с возвратом клиенту сообщения об ошибке.
     */
    @ExceptionHandler(value = { InternalServerException.class })
    public ResponseEntity<String> handleInternalServerExceptions(InternalServerException e) {
        log.error("InternalServerException occurred while handling the request.", e);

        String response = e.getMessage();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /////////////////////////////////////////////
    // Внетренние исключения от Spring MVC:

    /**
     * Нехватка выделенной оперативной памяти для обработки запроса.
     * Ошибка на уровне JVM.
     */
    @ExceptionHandler(value = {OutOfMemoryError.class})
    public ResponseEntity<String> handleOutOfMemory(OutOfMemoryError error) {
        log.error("Out of memory error: {}", ExceptionUtils.getStackTrace(error));

        String response = "Out of memory: " + error.getMessage();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Отсутствует обязательный параметр, помеченный аннотацией @RequestParam
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleBindException(MissingServletRequestParameterException e) {
        String response = "Отсутствует обязательный параметр '" + e.getParameterName() + "'";
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка в неверном типе передаваемого параметра, возникающие при попытке
     * привязки значения из запроса на указываемые поля/аргумент в @RequestParam или @RequestBody.
     */
    @ExceptionHandler({TypeMismatchException.class})
    public ResponseEntity<String> handleTypeMismatch(TypeMismatchException e) {
        String message = formatTypeMismatchMessage(e);

        String response = message;
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Ошибка в привязке значений из запроса на указываемые аргументы метода контроллера.
     *
     * <p>Также для ошибок валидации данных, при использовании аннотации @{@link javax.validation.Valid}.
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity<String> handleBindException(BindException e) {
        String response;
        if (e instanceof MethodArgumentNotValidException) {
            List<String> constraints = new ArrayList<>(e.getAllErrors().size());
            for (ObjectError error : e.getAllErrors()) {
                constraints.add(formatFieldErrorMessage(error));
            }
            response = String.join(" ", constraints);
        } else {
            response = "Получены ошибки при проверке данных входящего запроса.";
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Отсуствует обработчик или маппинг для полученного запроса
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<String> handleNoHandlerFound(NoHandlerFoundException e) {
        String response = "URL " + e.getRequestURL() + " не найден";
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Недопустимый HTTP метод при обращении к указанному URL.
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<String> handleMethodNoAllowed(HttpRequestMethodNotSupportedException e) {
        String response = e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /////////////////////////////////////////////

    /**
     * Тело ответа(JSON) повреждено или в невалидном формате.
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleUnreadableMessage(HttpMessageNotReadableException e) {
        String response = "Невозможно прочитать тело запроса: " + e.getMessage();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /////////////////////////////////////////////

    /**
     * Все оставшиеся неотловленные исключения.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleUncaughtException(HttpServletRequest request, Exception e) {
        log.error(String.format("Uncaught exception while handling \"%s\" to API %s",
                                request.getMethod(), request.getRequestURL()), e);

        String response = e.getMessage();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
