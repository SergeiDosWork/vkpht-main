package me.goodt.vkpht.module.notification.api;

import java.util.List;

import com.goodt.drive.auth.sur.entity.dto.SurMethodEntityDto;
import com.goodt.drive.auth.sur.entity.dto.SurViewEntityDto;

public interface RegistrationService {
    List<SurViewEntityDto> getAllView(String filter);
    List<SurMethodEntityDto> getAllRestMethods(String filter);

}
