package me.goodt.vkpht.module.orgstructure.domain.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionLinksEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.QDivisionLinksEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.DivisionLinksId;

@Repository
@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public class DivisionLinksDao extends AbstractDao<DivisionLinksEntity, DivisionLinksId> {

    private static final QDivisionLinksEntity meta = QDivisionLinksEntity.divisionLinksEntity;

    public DivisionLinksDao(EntityManager em) {
        super(DivisionLinksEntity.class, em);
    }

    public List<Long> findAllParents(Long division) {
        return query().selectFrom(meta)
                .where(meta.childId.eq(division))
                .select(meta.parentId)
                .fetch();
    }
}
