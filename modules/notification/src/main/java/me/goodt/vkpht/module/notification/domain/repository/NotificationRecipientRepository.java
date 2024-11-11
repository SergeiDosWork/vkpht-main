package me.goodt.vkpht.module.notification.domain.repository;

import org.springframework.data.repository.CrudRepository;

import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;

public interface NotificationRecipientRepository extends CrudRepository<NotificationRecipientEntity, Long> {
}
