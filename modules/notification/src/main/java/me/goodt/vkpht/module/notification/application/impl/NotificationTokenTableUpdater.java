package me.goodt.vkpht.module.notification.application.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.NotificationToken;
import me.goodt.vkpht.module.notification.application.Resolver;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTokenDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTokenEntity;

@Service
@Slf4j
public class NotificationTokenTableUpdater {
	private final NotificationTokenDao notificationTokenDao;
	private final List<Resolver> groupResolversList;

	public NotificationTokenTableUpdater(NotificationTokenDao notificationTokenDao,
										 List<Resolver> groupResolversList) {
		this.notificationTokenDao = notificationTokenDao;
		this.groupResolversList = groupResolversList;
	}

	@Transactional
	public void updateNotificationTokenTable() {
		log.info("Update notification_token table");
		List<NotificationTokenEntity> existing = notificationTokenDao.findAll();

		List<NotificationTokenEntity> needToCreate = new ArrayList<>();
		List<NotificationTokenEntity> needToChange = new ArrayList<>();
		List<NotificationTokenEntity> needToRemove = new ArrayList<>(existing);

		List<NotificationToken> tokensToCompare = groupResolversList.stream()
			.flatMap(resolver -> resolver.getResolvedTokens().stream())
			.collect(Collectors.toList());
		tokensToCompare.forEach(notificationToken -> {
			NotificationTokenEntity notificationTokenEntity = find(notificationToken, existing);
			if (notificationTokenEntity != null) {
				needToRemove.remove(notificationTokenEntity);
				if (!StringUtils.equals(notificationTokenEntity.getDescription(), notificationToken.getDescription())) {
					notificationTokenEntity.setDescription(notificationToken.getDescription());
					needToChange.add(notificationTokenEntity);
                }
            } else {
                NotificationTokenEntity created = new NotificationTokenEntity(
                    tokenize(makeFullToken(notificationToken.getGroupName(), notificationToken.getTokenName())),
                    notificationToken.getDescription(),
                    notificationToken.getTokenName(),
                    notificationToken.getGroupName());
                needToCreate.add(created);
            }
        });
        if (!needToCreate.isEmpty()) {
            List<NotificationTokenEntity> created = notificationTokenDao.saveAll(needToCreate);
            log.info("Add new {} records to notification_token", created.size());
        }
        if (!needToChange.isEmpty()) {
            List<NotificationTokenEntity> updated = notificationTokenDao.saveAll(needToChange);
            log.info("Update {} records to notification_token", updated.size());
        }
        if (!needToRemove.isEmpty()) {
            notificationTokenDao.deleteAllInBatch(needToRemove);
            log.info("Removed {} records from notification_token", needToRemove.size());
        }
        notificationTokenDao.flush();
    }

	private NotificationTokenEntity find(NotificationToken token, List<NotificationTokenEntity> existing) {
		for (NotificationTokenEntity notificationTokenEntity : existing) {
			String tokenA = makeFullToken(notificationTokenEntity.getGroupName(), notificationTokenEntity.getShortName());
			String tokenB = makeFullToken(token.getGroupName(), token.getTokenName());
			if (StringUtils.equals(tokenA, tokenB)) {
				return notificationTokenEntity;
			}
		}
		return null;
	}

	private String makeFullToken(String group, String token) {
		if (StringUtils.isEmpty(token)) {
			return group;
		}
		return group + "." + token;
	}

	private String tokenize(String token) {
		return "%" + token + "%";
	}

}
