package me.goodt.vkpht.module.notification.application.impl.resolver.employee;

import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.notification.application.impl.resolver.group.NotificationToken;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getGreetingEmployeeFullName;
import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getGreetingEmployeeShorName;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DEAR_USER_FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DEAR_USER_FIO_SHORT;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NOTIFICATION_USER_GREETING;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NOTIFICATION_USER_GREETING_DEAR_USER_FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NOTIFICATION_USER_GREETING_DEAR_USER_FIO_SHORT;

@Slf4j
@Component
public class DearUserEmployeeResolver implements TokenEmployeeResolver {

    private static final String ALTERNATIVE_APPEAL = "Коллега";

    @Override
    public void resolve(ResolverContext context, RecipientInfoDto employeeInfoDto, Map<String, String> resolvedTokenValues) {
        log.info(LOG_MESSAGE_GROUP, NOTIFICATION_USER_GREETING);

        EmployeeExtendedInfoDto extendedEmployeeInfoDto;

        if (employeeInfoDto instanceof EmployeeInfoDto) {
            extendedEmployeeInfoDto = (EmployeeExtendedInfoDto) context.getResolverServiceContainer()
                .getOrgstructureServiceAdapter().getEmployeeInfo(employeeInfoDto.getId());
        } else {
            extendedEmployeeInfoDto = null;
        }

        List<TokenWithValues> tokens = context.getParsedTokens().get(NOTIFICATION_USER_GREETING);

        tokens.forEach(token -> {
            if (token.getBasicValue().equals(DEAR_USER_FIO_FULL)) {
                log.info(LOG_MESSAGE_TOKEN, NOTIFICATION_USER_GREETING, DEAR_USER_FIO_FULL);
                resolvedTokenValues.put(
                    NOTIFICATION_USER_GREETING_DEAR_USER_FIO_FULL,
                    Objects.nonNull(extendedEmployeeInfoDto) ? getGreetingEmployeeFullName(extendedEmployeeInfoDto) : ALTERNATIVE_APPEAL
                );
            }
            if (token.getBasicValue().equals(DEAR_USER_FIO_SHORT)) {
                log.info(LOG_MESSAGE_TOKEN, NOTIFICATION_USER_GREETING, DEAR_USER_FIO_SHORT);
                resolvedTokenValues.put(
                    NOTIFICATION_USER_GREETING_DEAR_USER_FIO_SHORT,
                    Objects.nonNull(extendedEmployeeInfoDto) ? getGreetingEmployeeShorName(extendedEmployeeInfoDto) : ALTERNATIVE_APPEAL
                );
            }
        });
    }

    @Override
    public List<NotificationToken> getResolvedTokens() {
        return Arrays.asList(
            new NotificationToken(NOTIFICATION_USER_GREETING, DEAR_USER_FIO_FULL, "ФИО сотрудника в формате Уважаемый(ая) Фамилия Имя Отчество пример: Уважаемый Скорин Иван Васильевич"),
            new NotificationToken(NOTIFICATION_USER_GREETING, DEAR_USER_FIO_SHORT, "ФИО сотрудника в формате Уважаемый(ая) Фамилия И. О. пример: Уважаемая Петренко И.О.")
        );
    }
}
