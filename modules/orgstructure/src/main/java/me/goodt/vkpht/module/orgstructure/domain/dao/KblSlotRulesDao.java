package me.goodt.vkpht.module.orgstructure.domain.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.KblSlotRules;
import me.goodt.vkpht.module.orgstructure.domain.entity.QKblSlotRules;

@Repository
public class KblSlotRulesDao extends AbstractDao<KblSlotRules, Long> {

    private static final QKblSlotRules meta = QKblSlotRules.kblSlotRules;

    public KblSlotRulesDao(EntityManager em) {
        super(KblSlotRules.class, em);
    }

    public List<KblSlotRules> findAllByModuleSlot(@NotNull String module, String slotId) {
        BooleanExpression exp = meta.module.eq(module)
                .and(slotId != null ? meta.slot.eq(slotId) : null);
        return query().selectFrom(meta)
                .where(exp)
                .fetch();
    }

}
