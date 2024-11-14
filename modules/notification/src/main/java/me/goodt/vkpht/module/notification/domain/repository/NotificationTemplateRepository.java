package me.goodt.vkpht.module.notification.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;

@Transactional(readOnly = true)
public interface NotificationTemplateRepository extends CrudRepository<NotificationTemplateEntity, Long> {

    @Query(value = "SELECT * FROM notification_template WHERE code = :code", nativeQuery = true)
    Optional<NotificationTemplateEntity> getByCode(@Param("code") String code);

    @Query(value = "SELECT NEXTVAL('notification_template_code_manual_mailing_seq')", nativeQuery = true)
    Long incrementCodeManualMailing();
}
