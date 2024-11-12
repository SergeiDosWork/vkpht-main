package me.goodt.vkpht.module.notification.application.impl;

import me.goodt.vkpht.module.notification.application.NotificationRecipientEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;

@Service
public class NotificationRecipientEmailServiceImpl implements NotificationRecipientEmailService {

    @Autowired
    private NotificationRecipientEmailDao dao;

    @Override
    public List<String> findEmailByEmailIds(Collection<Long> ids) {
        return dao.findAllById(ids)
            .stream()
            .map(e -> e.getEmail())
            .collect(Collectors.toList());
    }
}
