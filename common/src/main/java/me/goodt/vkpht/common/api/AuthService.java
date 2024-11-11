package me.goodt.vkpht.common.api;

import com.goodt.drive.auth.security.AuthAccessTokenDetails;
import com.goodt.drive.auth.security.UserDetails;

import org.springframework.security.access.AccessDeniedException;

/**
 * Сервис для извлечения авторизационнной информации текущего запроса.
 */
public interface AuthService {
    /**
     * Возвращает данные токена авторизации.
     *
     * @return объект токена или <code>null</code>, если текущий запрос не был авторизован.
     */
    AuthAccessTokenDetails getUserToken();

    /**
     * Возвращает объект с информацией о текущем пользователе.
     */
    UserDetails getCurrentUser();

    /**
     * Возвращает идентификатор сотрудника текущего авторизованного пользователя.
     *
     * <p>Вызов данного метода предполагает, что пользователь должен быть авторизован
     * под сотрудником. Иначе будет выброшен {@link AccessDeniedException}.
     *
     * @return внутренний (системный) идентификатор записи сотрудника.
     * @throws AccessDeniedException если пользователь не авторизован под сотрудником.
     */
    long getUserEmployeeId();

    /**
     * Возвращает признак, что запрос авторизован под техническим пользователем.
     */
    boolean isTechUser();
}
