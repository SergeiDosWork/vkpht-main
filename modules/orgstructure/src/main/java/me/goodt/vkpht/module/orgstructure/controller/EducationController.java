package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.domain.dao.EducationTypeDao;
import me.goodt.vkpht.module.orgstructure.api.dto.ActualEducationDto;
import me.goodt.vkpht.module.orgstructure.domain.factory.EducationTypeFactory;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
@RequestMapping("/api/education")
public class EducationController {

    public final EducationTypeDao educationTypeDao;

    @Operation(summary = "Получение информации об актуальных уровнях образования", description = "Получение информации об актуальных уровнях образования")
    @BadRequestAPIResponses
    @GetMapping("/level")
    public List<ActualEducationDto> getInfo() {
        return educationTypeDao.findAllActual(Pageable.unpaged()).getContent().stream()
            .map(EducationTypeFactory::create)
            .collect(Collectors.toList());
    }
}

