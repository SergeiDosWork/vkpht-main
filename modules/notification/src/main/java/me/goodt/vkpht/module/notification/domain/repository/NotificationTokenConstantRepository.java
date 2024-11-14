package me.goodt.vkpht.module.notification.domain.repository;

import org.springframework.data.repository.CrudRepository;

import me.goodt.vkpht.module.notification.domain.entity.NotificationTokenConstantEntity;

public interface NotificationTokenConstantRepository extends CrudRepository<NotificationTokenConstantEntity, Long> {
}
