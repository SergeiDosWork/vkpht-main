package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.domain.entity.DomainObject;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientType;
import me.goodt.vkpht.module.notification.api.dto.Recipient;
import me.goodt.vkpht.module.notification.api.dto.RecipientToken;
import me.goodt.vkpht.module.notification.application.RecipientResolver;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentRecipientDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.factory.NotificationRecipientFactory;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationResolverTableUpdater {

    private final NotificationRecipientDao notificationRecipientDao;
    private final NotificationTemplateContentRecipientDao notificationTemplateContentRecipientDao;
    private final List<RecipientResolver> recipientResolverList;

    @Transactional
    public void updateRecipientTable() {
        log.info("Update notification_recipient table");
        // Все реализованные в ПО токены
        List<RecipientToken> actualTokens = new ArrayList<>();
        for (RecipientResolver recipientResolver : recipientResolverList) {
            actualTokens.addAll(recipientResolver.recipientsRegistration());
        }
        List<String> actualTokenNames = actualTokens.stream().map(RecipientToken::getName).collect(Collectors.toList());
        checkDuplicate(actualTokenNames);

        // Все существующие в БД токены, за исключением статических
        List<NotificationRecipientEntity> existingTokens = notificationRecipientDao.findAll().stream()
            .filter(n -> !StringUtils.equalsAny(n.getName(), NotificationRecipientType.getAllStaticTypes()))
            .collect(Collectors.toList());

        // Преобразованные в Recipient существующие в БД токены, с целью отделения name от параметров. Параметры из БД нас не интересуют
        Map<String, List<Pair<NotificationRecipientEntity, Recipient>>> existingTokensAndConvertedToRecipientsMap = existingTokens.stream()
            .map(r -> Pair.of(r, new Recipient(NotificationRecipientFactory.create(r, new ArrayList<>(), new ArrayList<>()))))
            .collect(Collectors.groupingBy(pair -> pair.getSecond().getBasicValue(), Collectors.toCollection(ArrayList::new)));

        // Составление списка токенов, которых больше нет в списке обрабатываемых приложением
        List<NotificationRecipientEntity> toDelete = existingTokensAndConvertedToRecipientsMap.entrySet().stream()
            .filter(e -> !actualTokenNames.contains(e.getKey()))
            .flatMap(e -> e.getValue().stream().map(Pair<NotificationRecipientEntity, Recipient>::getFirst))
            .collect(Collectors.toList());

        // Составление списка токенов, которые появились, но их не было
        List<String> existingTokenNames = existingTokensAndConvertedToRecipientsMap.keySet().stream()
            .distinct()
            .collect(Collectors.toList());

        List<NotificationRecipientEntity> toCreate = actualTokens.stream()
            .filter(t -> !existingTokenNames.contains(t.getName()))
            .map(t -> {
                NotificationRecipientEntity e = new NotificationRecipientEntity(t.getName(), t.getDescription());
                e.setIsSystem(true);
                return e;
            })
            .collect(Collectors.toList());

        // Разбор специальных(устаревших) токенов с параметрами в имени
        List<RecipientToken> allSpecialCases = actualTokens.stream().flatMap(t -> t.getSpecialCases().stream()).collect(Collectors.toList());
        allSpecialCases.forEach(specialCase -> {
            if (existingTokens.stream().filter(et -> et.getName().equals(specialCase.getName())).findAny().orElse(null) == null) {
                NotificationRecipientEntity newEntity = new NotificationRecipientEntity(specialCase.getName(), specialCase.getDescription());
                newEntity.setIsSystem(true);
                toCreate.add(newEntity);
            }
        });
        List<RecipientToken> allTokensWithSpecialCases = new ArrayList<>();
        allTokensWithSpecialCases.addAll(allSpecialCases);
        allTokensWithSpecialCases.addAll(actualTokens);
        existingTokens.forEach(token -> {
            if (allTokensWithSpecialCases.stream().filter(sc -> sc.getName().equals(token.getName())).findAny().orElse(null) == null) {
                toDelete.add(token);
            }
        });

        // Обработка изменений в дескрипшнах всех существующих токенах, если нужно
        List<NotificationRecipientEntity> toChange = new ArrayList<>();
        allTokensWithSpecialCases.forEach(token -> {
            NotificationRecipientEntity entity = existingTokens.stream().filter(et -> et.getName().equals(token.getName())).findAny().orElse(null);
            if (entity != null && (!StringUtils.equals(entity.getDescription(), token.getDescription()) || Boolean.TRUE != entity.getIsSystem())) {
                entity.setIsSystem(Boolean.TRUE);
                entity.setDescription(token.getDescription());
                toChange.add(entity);
            }
        });

        // Выполнение операций
        if (!toDelete.isEmpty()) {
            log.info("Delete {} recipients", toDelete.size());
            notificationTemplateContentRecipientDao.deleteAllByRecipientId(toDelete.stream().map(DomainObject::getId).collect(Collectors.toList()));
            notificationRecipientDao.deleteAllInBatch(toDelete);
        }
        if (!toCreate.isEmpty()) {
            log.info("Add {} new recipients", toCreate.size());
            notificationRecipientDao.saveAll(toCreate);
        }
        if (!toChange.isEmpty()) {
            log.info("Change {} existing recipients", toChange.size());
            notificationRecipientDao.saveAll(toChange);
        }
        notificationRecipientDao.flush();
    }

    private void checkDuplicate(List<String> t) {
        Set<Map.Entry<String, Long>> collect = t.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream().filter(m -> m.getValue() > 1)
            .collect(Collectors.toSet());
        if (!collect.isEmpty()) {
            collect.forEach(c -> log.error("Token {} has duplicate realization", c));
        }

    }

}
