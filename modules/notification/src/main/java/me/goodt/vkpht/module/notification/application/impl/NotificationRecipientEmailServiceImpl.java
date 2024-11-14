package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.application.NotificationRecipientEmailService;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;

@RequiredArgsConstructor
@Service
public class NotificationRecipientEmailServiceImpl implements NotificationRecipientEmailService {

    private final NotificationRecipientEmailDao dao;

    @Override
    public List<String> findEmailByEmailIds(Collection<Long> ids) {
        return dao.findAllById(ids)
            .stream()
            .map(NotificationRecipientEmailEntity::getEmail)
            .collect(Collectors.toList());
    }
}
