package me.goodt.vkpht.module.notification.application.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.notification.application.RecipientResolver;
import me.goodt.vkpht.module.notification.domain.factory.NotificationTemplateContentFactory;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goodt.drive.notify.application.dto.orgstructure.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.api.dto.Recipient;
import me.goodt.vkpht.module.notification.api.notification.employees.TokenEmployeeResolver;
import me.goodt.vkpht.module.notification.api.notification.groups.TokenGroupResolver;
import me.goodt.vkpht.module.notification.api.notification.recipients.RecipientResolver;
import me.goodt.vkpht.module.notification.application.TokenResolver;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientParametersDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientParameterEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;

@Service
@Slf4j
public class TokenResolverImpl implements TokenResolver {
	private final NotificationRecipientParametersDao notificationRecipientParametersDao;
    private final NotificationRecipientEmailDao notificationRecipientEmailDao;

	private final List<TokenGroupResolver> tokenGroupResolverList;
	private final List<RecipientResolver> recipientResolverList;
	private final List<TokenEmployeeResolver> tokenEmployeeResolverList;

	public TokenResolverImpl(List<TokenGroupResolver> tokenGroupResolverList,
							 List<RecipientResolver> recipientResolverList,
							 List<TokenEmployeeResolver> tokenEmployeeResolverList,
							 NotificationRecipientParametersDao notificationRecipientParametersDao,
                             NotificationRecipientEmailDao notificationRecipientEmailDao) {
		this.tokenGroupResolverList = tokenGroupResolverList;
		this.recipientResolverList = recipientResolverList;
		this.tokenEmployeeResolverList = tokenEmployeeResolverList;
		this.notificationRecipientParametersDao = notificationRecipientParametersDao;
        this.notificationRecipientEmailDao = notificationRecipientEmailDao;
	}

	@Override
	public Map<String, String> resolveTokens(ResolverContext context) {
		Map<String, String> resolvedTokenValues = new HashMap<>();
		tokenGroupResolverList.forEach(tokenGroupResolver -> {
			if (tokenGroupResolver.tokenMustBeResolved(context.getParsedTokens())) {
				try {
					tokenGroupResolver.resolve(context, resolvedTokenValues);
				} catch (Exception e) {
					log.error("Ошибка при вычислении токена {}", tokenGroupResolver.getClass().toString(), e);
				}
			}
		});
		return resolvedTokenValues;
	}

	@Override
	public Map<String, String> resolveEmployeeTokens(RecipientInfoDto employeeInfoDto, ResolverContext context) {
		Map<String, String> resolvedTokenValues = new HashMap<>();
		tokenEmployeeResolverList.forEach(tokenEmployeeResolver -> {
			if (tokenEmployeeResolver.tokenMustBeResolved(context.getParsedTokens())) {
				tokenEmployeeResolver.resolve(context, employeeInfoDto, resolvedTokenValues);
			}
		});
		return resolvedTokenValues;
	}

    @Override
    public ResolveRecipitentResult resolveRecipients(NotificationTemplateContentDto ntc, ResolverServiceContainer resolverServiceContainer, BaseNotificationInputData data, boolean isCopy, int substituteLevel) {
        List<NotificationRecipientDto> recipients = isCopy ? ntc.getNotificationRecipientCopy() : ntc.getNotificationRecipient();

        ResolveRecipitentResult result = new ResolveRecipitentResult();
        Set<RecipientInfoDto> rcp = new HashSet<>();

        final ResolverContext context = new ResolverContext(resolverServiceContainer)
            .prepareFromBaseNotificationInputData(data)
            .notificationTemplateContent(ntc);

        recipients.forEach(notificationRecipient -> {
            Recipient recipient = new Recipient(notificationRecipient);
            recipientResolverList.forEach(recipientResolver -> {
                try {
                    recipientResolver.resolve(context, recipient, rcp);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
        });
        if (CollectionUtils.isEmpty(rcp)) {
            int newLevel = substituteLevel - 1;
            if (newLevel >= 0) {
                List<NotificationTemplateContentEntity> substitutes = context.getResolverServiceContainer().getNotificationTemplateService().findBySubstituteId(ntc.getId());
                for (NotificationTemplateContentEntity substitudeNtc : substitutes) {
                    result = resolveRecipients(
                        NotificationTemplateContentFactory.create(
                            substitudeNtc,
                            (l) -> notificationRecipientParametersDao.findByParent(l).stream().map(NotificationRecipientParameterEntity::getValue).collect(Collectors.toList()),
                            (l) -> notificationRecipientEmailDao.findByRecipientId(l.getId()).stream().map(NotificationRecipientEmailEntity::getEmail).collect(Collectors.toList())
                        ),
                        resolverServiceContainer,
                        data,
                        isCopy,
                        newLevel
                    );
                    if (!CollectionUtils.isEmpty(result.getRecipients())) {
                        return result;
                    }
                }
            }
        }
        result.setRecipients(rcp);
        result.setResolvedTemplateContent(ntc);
        return result;
    }

    @Data
    public class ResolveRecipitentResult {
        private Set<RecipientInfoDto> recipients = new HashSet<>();
        private NotificationTemplateContentDto resolvedTemplateContent;
    }

}
