package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;
import me.goodt.vkpht.module.orgstructure.api.dto.KblSlotRulesRes;
import me.goodt.vkpht.module.orgstructure.domain.entity.KblSlotRules;

@Component
public class KblSlotRulesAsm extends AbstractAsm<KblSlotRules, KblSlotRulesRes> {

    @Override
    public KblSlotRulesRes toRes(KblSlotRules entity) {

        return new KblSlotRulesRes()
            .setEnabled(entity.isEnabled())
            .setLeftRule(entity.getLeftRule())
            .setLrDsOrKv(entity.getLrDsOrKv())
            .setModule(entity.getModule())
            .setNumber(entity.getNumber())
            .setOperator(entity.getOperator())
            .setRightRule(entity.getRightRule())
            .setRrDsOrKv(entity.getRrDsOrKv())
            .setSlot(entity.getSlot());
    }

    @Override
    public void create(KblSlotRules entity, KblSlotRulesRes res) {
        entity.setEnabled(res.isEnabled())
            .setLeftRule(res.getLeftRule())
            .setLrDsOrKv(res.getLrDsOrKv())
            .setModule(res.getModule())
            .setNumber(res.getNumber())
            .setOperator(res.getOperator())
            .setRightRule(res.getRightRule())
            .setRrDsOrKv(res.getRrDsOrKv())
            .setSlot(res.getSlot());
    }

    @Override
    public void update(KblSlotRules entity, KblSlotRulesRes res) {
        create(entity, res);
    }

}
