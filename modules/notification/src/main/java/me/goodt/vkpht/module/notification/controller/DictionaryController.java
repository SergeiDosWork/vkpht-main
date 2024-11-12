package me.goodt.vkpht.module.notification.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {
    @GetMapping
    public RepresentationModel<?> getDictionaryList() {
        RepresentationModel<?> response = new RepresentationModel<>();
        response.add(
            WebMvcLinkBuilder.linkTo(NotificationReceiverSystemDictController.class)
                .withRel("notification-receiver-system")
        );

        return response;
    }
}
