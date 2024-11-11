package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import me.goodt.vkpht.module.notification.application.NotificationRecipientEmailService;

@RestController
@RequestMapping("/api/notification-recipient-email")
public class NotificationRecipientEmailController {

    @Autowired
    private NotificationRecipientEmailService service;

    @GetMapping
    public List<String> find(
        @Parameter(name = "ids", description = "Массив идентификаторов email (таблица notifications.notification_recipient_email).", example = "[1, 2, 3]")
        @RequestParam List<Long> ids
    ) {
        return service.findEmailByEmailIds(ids);
    }

}
