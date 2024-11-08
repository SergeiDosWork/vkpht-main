package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import com.goodt.drive.rtcore.data.DivisionShortInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionShortInfoDto;

@UtilityClass
public class DivisionShortInfoFactory {

    public static DivisionShortInfoDto create(DivisionShortInfo info) {
        return new DivisionShortInfoDto(info.getId(), info.getParentId(), info.getFullName(),
                                        info.getShortName(), info.getAbbreviation());
    }
}
