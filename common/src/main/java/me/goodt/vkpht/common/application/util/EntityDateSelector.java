package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.goodt.drive.rtcore.model.ArchivableEntity;

@UtilityClass
public class EntityDateSelector {

    public static <T extends ArchivableEntity> List<T> selectActual(List<T> items) {
        final Date currentDate = new Date();
        List<T> selected = items.stream().filter(i -> {
            if (i.getDateTo() == null) {
                return true;
            }
            return i.getDateTo().after(currentDate);
        }).collect(Collectors.toList());
        return selected;
    }
}
