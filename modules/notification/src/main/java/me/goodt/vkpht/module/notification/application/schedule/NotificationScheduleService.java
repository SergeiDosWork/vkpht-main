package me.goodt.vkpht.module.notification.application.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;

import me.goodt.vkpht.module.notification.application.NotificationDeferredSendService;
import me.goodt.vkpht.module.notification.config.KafkaConfig;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationScheduleService {

    private final KafkaConfig kafkaConfig;

    @Autowired
    private NotificationDeferredSendService notificationDeferredSendService;

    /**
     * Отправка отложенных уведомлений
     */
    @Scheduled(fixedDelay = 3600000, initialDelay = 3600000)
    public void startSendDeferredNotifications() {
        log.info("START SEND DEFERRED NOTIFICATIONS");
        try {
            AdminClient client = AdminClient.create(kafkaConfig.producerConfigs());
            Collection<Node> nodes = client.describeCluster().nodes().get();
            client.close();
            if (CollectionUtils.isNotEmpty(nodes)) {
                notificationDeferredSendService.startDeferredSendNotification();
            }
        } catch (Exception ex) {
            log.error("Ошибка подключения к Apache Kafka при попытке отправки отложенных уведомлений.", ex);
        }
        finally {
            log.info("STOP SEND DEFERRED NOTIFICATIONS");
        }
    }

}
