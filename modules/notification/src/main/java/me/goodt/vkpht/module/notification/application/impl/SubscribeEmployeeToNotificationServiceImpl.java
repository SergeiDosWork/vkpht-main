package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.SubscribeEmployeeToNotificationService;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentEmployeeSubscribeDto;
import me.goodt.vkpht.module.notification.domain.dao.NotificationReceiverSystemDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationReceiverSystemEmployeeDisabledDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentEmployeeSubscribeDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEmployeeDisabledEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEmployeeSubscribeEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.factory.NotificationTemplateContentEmployeeSubscribeFactory;

@RequiredArgsConstructor
@Service
public class SubscribeEmployeeToNotificationServiceImpl implements SubscribeEmployeeToNotificationService {

    private final NotificationReceiverSystemEmployeeDisabledDao notificationReceiverSystemEmployeeDisabledDao;
    private final NotificationTemplateContentEmployeeSubscribeDao notificationTemplateContentEmployeeSubscribeDao;
    private final NotificationTemplateContentDao notificationTemplateContentDao;
    private final NotificationReceiverSystemDao notificationReceiverSystemDao;

    @Override
    public boolean isPossibleSendNotificationToEmployee(Long employeeId, Long notificationTemplateContentId, Long notificationReceiverSystemId) {

        boolean isSubscribe = false;

        //доступность отправки уведомления сотруднику указанным способом
        NotificationReceiverSystemEmployeeDisabledEntity disabledNotificationToEmployeeEntity =
            notificationReceiverSystemEmployeeDisabledDao
                .findByEmployeeIdAndNotificationReceiverSystemId(employeeId, notificationReceiverSystemId);

        if (Objects.isNull(disabledNotificationToEmployeeEntity)) {
            //доступность отправки сотруднику указанного типа уведомления
            NotificationTemplateContentEmployeeSubscribeEntity subscribeEmployeeToNotificationEntity =
                notificationTemplateContentEmployeeSubscribeDao
                    .findByEmployeeIdAndNotificationTemplateContentId(employeeId, notificationTemplateContentId);

            //по умолчанию все (новые) сотрудники получают все типы уведомлений
            if (Objects.isNull(subscribeEmployeeToNotificationEntity)) {

                Optional<NotificationTemplateContentEntity> notificationTemplateContent =
                    notificationTemplateContentDao.findById(notificationTemplateContentId);
                if (notificationTemplateContent.isPresent()) {
                    notificationTemplateContentEmployeeSubscribeDao.save(
                        new NotificationTemplateContentEmployeeSubscribeEntity(employeeId, notificationTemplateContent.get(), true)
                    );
                    isSubscribe = true;
                }

            } else {
                isSubscribe = subscribeEmployeeToNotificationEntity.getIsEnabled();
            }
        }

        return isSubscribe;
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateContentEmployeeSubscribeDto load(Long employeeId, String receiverSystemName) {

        List<NotificationTemplateContentEmployeeSubscribeEntity> subscribesEmployee =
            notificationTemplateContentEmployeeSubscribeDao.findByEmployeeId(employeeId, receiverSystemName);

        boolean isDisabledReceiverSystem = notificationReceiverSystemEmployeeDisabledDao.existsByEmployeeIdAndReceiverSystemName(employeeId, receiverSystemName);

        NotificationTemplateContentEmployeeSubscribeDto subscribeDetails =
            NotificationTemplateContentEmployeeSubscribeFactory.create(employeeId, subscribesEmployee, isDisabledReceiverSystem, receiverSystemName);

        return subscribeDetails;
    }

    @Override
    @Transactional
    public void save(NotificationTemplateContentEmployeeSubscribeDto dto) {

        NotificationReceiverSystemEmployeeDisabledEntity disabledEntity =
            notificationReceiverSystemEmployeeDisabledDao
                .findByEmployeeIdAndNotificationReceiverSystemName(dto.getEmployeeId(), dto.getReceiverSystemName());

        //включение канала отправки
        if (dto.getIsEnabledAllNotifications()) {
            if (Objects.nonNull(disabledEntity)) {
                notificationReceiverSystemEmployeeDisabledDao.delete(disabledEntity);
            }
        } else {
            //отключение канала отправки
            if (Objects.isNull(disabledEntity)) {
                NotificationReceiverSystemEmployeeDisabledEntity disabledEntityNew = new NotificationReceiverSystemEmployeeDisabledEntity();
                NotificationReceiverSystemEntity receiverSystemEntity = notificationReceiverSystemDao.findByName(dto.getReceiverSystemName());
                if (Objects.isNull(receiverSystemEntity)) {
                    throw new NotFoundException(String.format("Cannot find receiver system '%s'", dto.getReceiverSystemName()));
                }

                disabledEntityNew.setEmployeeId(dto.getEmployeeId());
                disabledEntityNew.setNotificationReceiverSystem(receiverSystemEntity);

                notificationReceiverSystemEmployeeDisabledDao.save(disabledEntityNew);
            }
        }

        //сохранение всех подписок на уведомления
        dto.getSubscribes().forEach(subscribe -> {
            NotificationTemplateContentEmployeeSubscribeEntity subscribeEntity =
                notificationTemplateContentEmployeeSubscribeDao.findByIdAndEmployeeId(
                    subscribe.getId(),
                    dto.getEmployeeId()
                );

            if (Objects.nonNull(subscribeEntity)) {
                if (!Objects.equals(subscribeEntity.getIsEnabled(), subscribe.getIsEnabled())) {
                    subscribeEntity.setIsEnabled(subscribe.getIsEnabled());
                    notificationTemplateContentEmployeeSubscribeDao.save(subscribeEntity);
                }
            }
        });

    }

}
