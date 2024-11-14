package me.goodt.vkpht.module.notification.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

@Repository
public interface NotificationLogDao extends JpaRepository<NotificationLogEntity, Long> {

    @Query("SELECT n FROM NotificationLogEntity n " +
        "LEFT JOIN FETCH n.notificationTemplateContent ntc " +
        "LEFT JOIN FETCH ntc.notificationTemplate nt " +
        "LEFT JOIN FETCH ntc.receiverSystem rs " +
        "LEFT JOIN FETCH ntc.substitute s " +
        "WHERE n.id = :id")
    Optional<NotificationLogEntity> getByIdEager(@Param("id") Long id);

}
