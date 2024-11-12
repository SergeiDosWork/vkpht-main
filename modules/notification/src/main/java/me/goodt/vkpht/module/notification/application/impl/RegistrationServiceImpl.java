package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import com.goodt.drive.auth.sur.entity.dto.SurMethodEntityDto;
import com.goodt.drive.auth.sur.entity.dto.SurViewEntityDto;
import com.goodt.drive.auth.sur.service.SurRegistrationService;
import me.goodt.vkpht.module.notification.domain.dao.NativeDao;
import me.goodt.vkpht.module.notification.api.RegistrationService;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

	private final NativeDao nativeDao;
    private final SurRegistrationService surRegistrationService;

    @Value("${appConfig.schemas-for-views}")
    private List<String> schemas;

    @Override
    public List<SurViewEntityDto> getAllView(String filter) {
        var allView = nativeDao.getAllView(schemas);
        return surRegistrationService.getAllView(allView, filter);
    }
    @Override
    public List<SurMethodEntityDto> getAllRestMethods(String filter) {
        return surRegistrationService.getAllRestMethods(filter);
    }
}
