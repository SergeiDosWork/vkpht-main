package me.goodt.vkpht.module.notification.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentFilter;
import me.goodt.vkpht.module.notification.domain.entity.*;
import me.goodt.vkpht.common.dictionary.core.dao.AbstractDao;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class NotificationTemplateContentDao extends AbstractDao<NotificationTemplateContentEntity, Long> {

    private static final QNotificationTemplateContentEntity meta = QNotificationTemplateContentEntity.notificationTemplateContentEntity;
    private static final QNotificationTemplateEntity metaParent = QNotificationTemplateEntity.notificationTemplateEntity;

    public NotificationTemplateContentDao(EntityManager em) {
        super(NotificationTemplateContentEntity.class, em);
    }

    public List<NotificationTemplateContentEntity> findByNotificationTemplateId(Long notificationTemplateId) {
        final BooleanExpression exp = meta.isEnabled.eq(1)
            .and(meta.notificationTemplate.id.eq(notificationTemplateId));
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public List<NotificationTemplateContentEntity> findNotificationTemplateContentByCode(String code) {
        final QNotificationTemplateEntity notificationTemplate = QNotificationTemplateEntity.notificationTemplateEntity;
        final BooleanExpression exp = meta.isEnabled.eq(1)
            .and(meta.substitute.isNull())
            .and(notificationTemplate.code.eq(code))
            .and(meta.dateTo.isNull().or(Expressions.currentTimestamp().between(meta.dateFrom, meta.dateTo)))
            .and(notificationTemplate.isEnabled.eq(1))
            .and(notificationTemplate.dateTo.isNull().or(Expressions.currentTimestamp().between(notificationTemplate.dateFrom, meta.dateTo)));

        return query().selectFrom(meta)
            .innerJoin(meta.notificationTemplate, notificationTemplate)
            .where(exp)
            .fetch();
    }

    public List<NotificationRecipientEntity> findRecipientByNotificationTemplateCode(String code) {
        final QNotificationTemplateEntity nt = new QNotificationTemplateEntity("notificationTemplate");
        final QNotificationRecipientEntity nr = new QNotificationRecipientEntity("notificationRecipient");
        final BooleanExpression exp = meta.isEnabled.eq(1)
            .and(meta.substitute.isNull())
            .and(nt.code.eq(code));

        return query().selectFrom(meta)
            .where(exp)
            .fetch()
            .stream()
            .flatMap(s -> s.getNotificationRecipient().stream())
            .collect(Collectors.toList());
    }

    public Map<NotificationRecipientEntity, List<NotificationTemplateContentEntity>> findRecipientNameAndNotTempContentByCode(String code) {
        final QNotificationTemplateEntity nt = new QNotificationTemplateEntity("notificationTemplate");
        final QNotificationRecipientEntity nr = new QNotificationRecipientEntity("notificationRecipient");
        final BooleanExpression exp = meta.isEnabled.eq(1)
            .and(meta.substitute.isNull())
            .and(nt.code.eq(code));

        return query().selectFrom(meta)
            .where(exp)
            .fetch()
            .stream()
            .flatMap(s -> s.getNotificationRecipient().stream().map(m -> Pair.of(m, s)))
            .collect(groupingBy(Pair::getFirst, mapping(Pair::getSecond, toList())));
    }

    public List<NotificationTemplateContentEntity> findBySubstituteId(Long substituteId) {
        final BooleanExpression exp = meta.idSubstitute.eq(substituteId)
            .and(meta.isEnabled.eq(1))
            .and(meta.dateTo.isNull().or(Expressions.currentTimestamp().between(meta.dateFrom, meta.dateTo)));
        return query().selectFrom(meta)
            .where(exp)
            .fetch();
    }

    public List<NotificationTemplateContentEntity> findAll(NotificationTemplateContentFilter filter) {
        BooleanExpression expression = meta.dateTo.isNull();
        if (filter != null && filter.getNotificationTemplateId() != null) {
            expression = expression.and(meta.notificationTemplate.id.eq(filter.getNotificationTemplateId()));
        }
        return query().selectFrom(meta)
            .join(metaParent).on(meta.notificationTemplate.id.eq(metaParent.id))
            .where(expression.and(metaParent.unitCode.eq(filter.getUnitCode())))
            .fetch();
    }
}
