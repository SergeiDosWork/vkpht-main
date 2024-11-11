package me.goodt.vkpht.module.notification.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import me.goodt.vkpht.module.notification.domain.entity.NotificationDeferredSendEntity;


@Repository
public interface NotificationDeferredSendDao extends JpaRepository<NotificationDeferredSendEntity, Long> {

    @Query(value = "select e.id from NotificationDeferredSendEntity e")
    List<Long> findAllNotificationLogId();
}
