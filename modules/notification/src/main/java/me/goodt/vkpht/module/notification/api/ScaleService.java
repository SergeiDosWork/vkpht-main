package me.goodt.vkpht.module.notification.api;

import com.goodt.drive.notify.application.dto.rtcore.ScaleLevelNotificationDto;

public interface ScaleService {

	ScaleLevelNotificationDto findScaleLevelById(Long id);

}
