package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.domain.dao.FunctionDao;
import me.goodt.vkpht.module.orgstructure.api.dto.FunctionDto;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.FunctionFactory;

@RestController
@GeneralAPIResponses
@RequiredArgsConstructor
public class FunctionController {

    private final FunctionDao functionDao;

    @Operation(summary = "Получение информации о функции", description = "Получение информации о функции по её индентификатору", tags = {"function"})
    @BadRequestAPIResponses
    @GetMapping("/api/function/{id}")
    public FunctionDto get(
        @Parameter(name = "id", description = "Идентификатор функции (таблица function)", example = "1")
        @PathVariable(name = "id") Long id) throws NotFoundException {
        return FunctionFactory.create(functionDao.findById(id)
                                          .orElseThrow(() -> new NotFoundException(String.format("Function with id=%d is not found", id))));
    }

    @Operation(summary = "Получение информации о всех актуальных функциях",
        description = "Получение информации о всех актуальных функциях",
        tags = {"function"})
    @GetMapping("/api/function/list")
    public List<FunctionDto> get() {
        return functionDao.findAllActual(Pageable.unpaged()).getContent().stream()
            .map(FunctionFactory::create)
            .collect(Collectors.toList());
    }
}
