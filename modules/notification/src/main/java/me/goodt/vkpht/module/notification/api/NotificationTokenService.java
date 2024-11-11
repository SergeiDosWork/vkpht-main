package me.goodt.vkpht.module.notification.api;

import java.util.List;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.dto.NotificationTokenDto;

public interface NotificationTokenService {

	List<NotificationTokenDto> getAll();

	NotificationTokenDto getById(Long id) throws NotFoundException;

	NotificationTokenDto create(NotificationTokenDto tokenDto);

	NotificationTokenDto update(Long id, NotificationTokenDto tokenDto) throws NotFoundException;

	void delete(Long id) throws NotFoundException;

}
