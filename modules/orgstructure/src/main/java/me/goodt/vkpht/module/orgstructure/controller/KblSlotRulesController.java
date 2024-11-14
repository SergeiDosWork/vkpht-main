package me.goodt.vkpht.module.orgstructure.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractCrudController;
import me.goodt.vkpht.module.orgstructure.api.dto.KblSlotRulesRes;
import me.goodt.vkpht.module.orgstructure.application.impl.KblSlotRulesServiceImpl;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.KblSlotRulesAsm;
import me.goodt.vkpht.module.orgstructure.domain.entity.KblSlotRules;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kbl/rules")
@SurProtected(entityName = "org_slot_rules")
public class KblSlotRulesController extends AbstractCrudController<KblSlotRules, KblSlotRulesRes> {

    @Getter
    private final KblSlotRulesServiceImpl service;

    @Getter
    private final KblSlotRulesAsm asm;

    @GetMapping("module")
    public List<KblSlotRulesRes> getAll(@RequestParam String module,
                                        @RequestParam(required = false) String slotId) {
        return service.getAllByFilter(module, slotId, asm::toList);
    }
}
