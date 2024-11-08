package me.goodt.vkpht.module.orgstructure.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.asm.org.KblSlotRulesAsm;
import me.goodt.vkpht.module.orgstructure.api.dto.KblSlotRulesRes;
import me.goodt.vkpht.module.orgstructure.domain.entity.KblSlotRules;
import me.goodt.vkpht.module.orgstructure.api.KblSlotRulesService;
import me.goodt.vkpht.common.controller.AbstractCrudController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kbl/rules")
@SurProtected(entityName = "org_slot_rules")
public class KblSlotRulesController extends AbstractCrudController<KblSlotRules, KblSlotRulesRes> {

    @Getter
    private final KblSlotRulesService service;

    @Getter
    private final KblSlotRulesAsm asm;

    @GetMapping("module")
    public List<KblSlotRulesRes> getAll(@RequestParam String module,
                                        @RequestParam(required = false) String slotId) {
        return service.getAllByFilter(module, slotId, asm::toList);
    }
}
