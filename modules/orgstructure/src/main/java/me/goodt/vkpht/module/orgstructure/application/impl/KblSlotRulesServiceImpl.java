package me.goodt.vkpht.module.orgstructure.application.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import me.goodt.vkpht.common.dictionary.core.service.AbstractCrudService;
import me.goodt.vkpht.common.dictionary.core.service.Converter;
import me.goodt.vkpht.module.orgstructure.domain.dao.KblSlotRulesDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.KblSlotRules;

@RequiredArgsConstructor
@Getter
@Service
@Transactional
public class KblSlotRulesServiceImpl extends AbstractCrudService<KblSlotRules, Long> {

    private final KblSlotRulesDao dao;

    @Transactional(readOnly = true)
    public <T> T getAllByFilter(@NotNull String module, String slotId, Converter<List<KblSlotRules>, T> converter) {
        final List<KblSlotRules> l = dao.findAllByModuleSlot(module, slotId);
        return converter.convert(l);
    }
}
