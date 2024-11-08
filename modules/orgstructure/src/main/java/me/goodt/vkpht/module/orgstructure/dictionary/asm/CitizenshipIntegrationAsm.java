package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.CitizenshipIntegrationDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipIntegrationEntity;
import me.goodt.vkpht.common.application.asm.AbstractAsm;

@Component
public class CitizenshipIntegrationAsm extends AbstractAsm<CitizenshipIntegrationEntity, CitizenshipIntegrationDto> {

    @Override
    public CitizenshipIntegrationDto toRes(CitizenshipIntegrationEntity entity) {
        return new CitizenshipIntegrationDto()
                .setId(entity.getId())
                .setName(entity.getName())
                .setAbbreviation(entity.getAbbreviation())
                .setDesignation(entity.getDesignation())
                .setInEnglish(entity.getInEnglish())
                .setResponsible(entity.getResponsible())
                .setBasis(entity.getBasis())
                .setDateIntroduction(entity.getDateIntroduction())
                .setDateExpiration(entity.getDateExpiration())
                .setLastModified(entity.getLastModified())
                .setReasonForChange(entity.getReasonForChange());
    }

    @Override
    public void create(CitizenshipIntegrationEntity entity, CitizenshipIntegrationDto res) {
        createOrUpdateCopyFields(entity, res);
    }

    @Override
    public void update(CitizenshipIntegrationEntity entity, CitizenshipIntegrationDto res) {
        createOrUpdateCopyFields(entity, res);
    }

    private void createOrUpdateCopyFields(CitizenshipIntegrationEntity entity, CitizenshipIntegrationDto res) {
        entity.setName(res.getName());
        entity.setAbbreviation(res.getAbbreviation());
        entity.setDesignation(res.getDesignation());
        entity.setInEnglish(res.getInEnglish());
        entity.setResponsible(res.getResponsible());
        entity.setBasis(res.getBasis());
        entity.setDateIntroduction(res.getDateIntroduction());
        entity.setDateExpiration(res.getDateExpiration());
        entity.setLastModified(res.getLastModified());
        entity.setReasonForChange(res.getReasonForChange());
    }
}
