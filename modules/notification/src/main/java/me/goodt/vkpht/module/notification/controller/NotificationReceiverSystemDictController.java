package me.goodt.vkpht.module.notification.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDictDto;
import me.goodt.vkpht.module.notification.application.impl.NotificationReceiverSystemCrudService;

@RestController
@RequestMapping("/api/dict/notification-receiver-system")
@SurProtected(entityName = "notification_receiver_system")
public class NotificationReceiverSystemDictController extends UnfilteredDictController<Long, NotificationReceiverSystemDictDto> {

    private final NotificationReceiverSystemCrudService service;

    public NotificationReceiverSystemDictController(DictionaryMetaGenerator dictionaryMetaGenerator,
                                                    NotificationReceiverSystemCrudService service) {
        super(dictionaryMetaGenerator);
        this.service = service;
    }

    @Override
    public Page<NotificationReceiverSystemDictDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public NotificationReceiverSystemDictDto get(Long id) {
        return service.get(id);
    }

    @Override
    public NotificationReceiverSystemDictDto create(NotificationReceiverSystemDictDto request) {
        return service.create(request);
    }

    @Override
    public NotificationReceiverSystemDictDto update(Long id, NotificationReceiverSystemDictDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}
