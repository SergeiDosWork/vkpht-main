package me.goodt.vkpht.security.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import com.goodt.drive.auth.security.AuthAccessTokenDetails;
import com.goodt.drive.auth.security.AuthTokenExtractor;
import com.goodt.drive.auth.security.UserDetails;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;

/**
 * Реализация AuthService для работы с запросами, авторизованными через Keycloak.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakAuthService implements AuthService {

    public static final UserDetails SYSTEM_USER = new SystemUserDetails();

    private final EmployeeService employeeService;
    private final AuthTokenExtractor tokenExtractor;

    @Override
    public AuthAccessTokenDetails getUserToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.warn("Requested AccessToken without Authentication in context.");
            return null;
        }


        return tokenExtractor.extract(authentication);
    }

    @Override
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.warn("Requested UserDetails without Authentication in context. Returning system user");
            return SYSTEM_USER;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        log.warn("Unsupported Principal class {}. Access Denied",
            principal.getClass().getCanonicalName());
        throw new AccessDeniedException("Не удалось получить информацию текущего пользователя");
    }


    @Override
    public long getUserEmployeeId() {
        String externalId = getCurrentUser().getEmployeeExternalId();
        if (externalId == null) {
            throw new AccessDeniedException("Требуется выполнить запрос от имени сотрудника");
        }

        final Long employeeId = employeeService.findIdByExternalId(externalId);
        if (employeeId == null) {
            throw new AccessDeniedException("Сотрудник с идентификатором + " + externalId + " не найден");
        }
        return employeeId;
    }


    @Override
    public boolean isTechUser() {
        UserDetails user = getCurrentUser();
        return Boolean.TRUE.equals(user.isTechUser());
    }

    private static class SystemUserDetails implements UserDetails {

        @Override
        public String getUuid() {
            return "system";
        }

        @Override
        public String getUsername() {
            return "system";
        }

        @Override
        public String getName() {
            return "system";
        }

        @Override
        public List<String> getGroups() {
            return List.of();
        }

        @Override
        public boolean isTechUser() {
            return true;
        }

        @Override
        public String getEmployeeExternalId() {
            return "0";
        }
    }
}
