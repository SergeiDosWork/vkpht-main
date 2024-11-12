package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.notification.api.dto.rtcore.ScaleLevelNotificationDto;
import me.goodt.vkpht.module.notification.api.ScaleService;
import me.goodt.vkpht.module.notification.api.rtcore.RtCoreServiceClient;

@Service
@RequiredArgsConstructor
public class ScaleServiceImpl implements ScaleService {

	private final RtCoreServiceClient rtCoreServiceClient;

	@Override
	public ScaleLevelNotificationDto findScaleLevelById(Long id) {
		return rtCoreServiceClient.findScaleLevelById(id);
	}
}
