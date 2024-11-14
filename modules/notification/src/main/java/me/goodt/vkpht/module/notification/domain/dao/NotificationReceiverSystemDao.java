package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;
import me.goodt.vkpht.module.notification.domain.entity.QNotificationReceiverSystemEntity;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class NotificationReceiverSystemDao extends AbstractDao<NotificationReceiverSystemEntity, Long> {

    private static final QNotificationReceiverSystemEntity meta = QNotificationReceiverSystemEntity.notificationReceiverSystemEntity;

    public NotificationReceiverSystemDao(EntityManager em) {
        super(NotificationReceiverSystemEntity.class, em);
    }

    public NotificationReceiverSystemEntity findByName(String name) {
        return query().selectFrom(meta)
            .where(meta.name.eq(name))
            .fetchFirst();
    }

    public Page<NotificationReceiverSystemEntity> findAllIsActive(String unitCode, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(meta.unitCode.eq(unitCode));
        builder.and(meta.isActive.isTrue());

        return this.findAll(builder, pageable);
    }

}
