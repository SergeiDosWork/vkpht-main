package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamDto;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;

@RestController
@RequestMapping("/api/division-team")
@RequiredArgsConstructor
@GeneralAPIResponses
public class DivisionTeamController {

    private final DivisionService divisionService;

    @Operation(summary = "Получение информации о команде дивизиона", description = "Получение информации о команде дивизиона", tags = {"division_team"})
    @BadRequestAPIResponses
    @GetMapping("/{id}")
    public DivisionTeamDto findById(
        @Parameter(name = "id", description = "ИД команды дивизиона")
        @PathVariable("id") Long id) {
        return divisionService.findDivisionTeamById(id);
    }

    @Operation(summary = "Получение информации об актуальных командах дивизиона", description = "Получение информации об актуальных командах дивизиона", tags = {"division_team"})
    @GetMapping
    public List<DivisionTeamDto> findActual() {
        return divisionService.findActual();
    }
}
