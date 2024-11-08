package me.goodt.vkpht.module.orgstructure.application;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import java.util.List;

import me.goodt.vkpht.common.application.Converter;
import me.goodt.vkpht.module.orgstructure.domain.dao.KblSlotRulesDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.KblSlotRules;

@Service
@Transactional
public class KblSlotRulesService extends AbstractCrudService<KblSlotRules, Long> {

    @Getter
    @Autowired
    private KblSlotRulesDao dao;

    @Transactional(readOnly = true)
    public <T> T getAllByFilter(@NotNull String module, String slotId, Converter<List<KblSlotRules>, T> converter) {
        final List<KblSlotRules> l = dao.findAllByModuleSlot(module, slotId);
        return converter.convert(l);
    }
}
