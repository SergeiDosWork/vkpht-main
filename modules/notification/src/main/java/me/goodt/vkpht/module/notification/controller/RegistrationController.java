package me.goodt.vkpht.module.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.goodt.drive.auth.sur.entity.dto.SurMethodEntityDto;
import com.goodt.drive.auth.sur.entity.dto.SurViewEntityDto;
import me.goodt.vkpht.module.notification.api.RegistrationService;

@Tag(name = "Registration", description = "API для получения данных по (не)зарегистрированным объектам")
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "Получение списка view: all-view-list", description = "Получение списка всех view", tags = {"view"})
    @GetMapping("/api/registration/all-view-list")
    public List<SurViewEntityDto> getAllView(@RequestParam(required = false, defaultValue = "") String filter) {
        return registrationService.getAllView(filter);
    }

    @Operation(summary = "Получение списка rest: all-methods-list", description = "Получение списка всех rest методов", tags = {"methods"})
    @GetMapping("/api/registration/all-methods-list")
    public List<SurMethodEntityDto> getAllMethods(@RequestParam(required = false, defaultValue = "") String filter) {
        return registrationService.getAllRestMethods(filter);
    }

}
