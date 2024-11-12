package me.goodt.vkpht.module.notification.application.impl;

import me.goodt.vkpht.module.notification.application.NotificationService;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.EventService;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final EventService eventService;
    private final ExecutorService threadPool;

	public NotificationServiceImpl(@Lazy EventService eventService, ExecutorService threadPool) {
		this.eventService = eventService;
        this.threadPool = threadPool;
    }

	@Override
	public void baseNotification(Map<String, Object> tokenValues, String code, Map<String, Object> parameters) {
        BaseNotificationInputData data = new BaseNotificationInputData(tokenValues, code, parameters);

        // Оборачиваем в DelegatingSecurityContextRunnable, чтобы в новом потоке
        // был доступ к текущему SecurityContextHolder для получения информации
        // о текущем авторизованном пользователе и его токене доступа.
        Runnable runnable = DelegatingSecurityContextRunnable.create(
            () -> eventService.baseNotification(data),
            SecurityContextHolder.getContext()
        );

        threadPool.execute(runnable);
	}
}
