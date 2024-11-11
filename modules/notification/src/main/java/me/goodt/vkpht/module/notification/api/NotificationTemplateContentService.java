package me.goodt.vkpht.module.notification.api;

import java.util.List;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentFilter;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentRequest;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentResponse;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentShortResponse;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentSubstituteInfo;

public interface NotificationTemplateContentService {

	List<NotificationTemplateContentShortResponse> findAll(NotificationTemplateContentFilter filter);

	NotificationTemplateContentResponse getById(Long id) throws NotFoundException;

	NotificationTemplateContentResponse create(NotificationTemplateContentRequest request) throws NotFoundException;

	NotificationTemplateContentResponse update(Long id, NotificationTemplateContentRequest request) throws NotFoundException;

	void delete(Long id) throws NotFoundException;

	List<NotificationTemplateContentSubstituteInfo> findSubstitutes(Long templateId, Long contentId);
}
