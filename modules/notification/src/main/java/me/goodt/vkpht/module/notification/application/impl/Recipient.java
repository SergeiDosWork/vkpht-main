package me.goodt.vkpht.module.notification.application.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;

@Getter
@Slf4j
public class Recipient {

    private static final String LOG_ERROR = "An error has occurred, message: {}";
    private final String basicValue;
    private List<Long> ids = new ArrayList<>();
    private final NotificationRecipientDto recipient;

    public Recipient(NotificationRecipientDto recipient) {
        this.recipient = recipient;
        String[] array = recipient.getName().split("[.]");
        this.basicValue = array[0];
        if (!recipient.getParameters().isEmpty()) {
            ids.addAll(recipient.getParameters());
        }
        // TODO: Дальнейший код получения всех id путем разбиения name на части через . необходимо выпилить после переноса всех параметров из name в parameters
        if (array.length > 1) {
            ids = new ArrayList<>(array.length - 1);
            for (int i = 1; i < array.length; i++) {
                try {
                    String str = array[i];
                    if (str.contains("_")) {
                        String[] split = str.split("_");
                        for (String s : split) {
                            ids.add(Long.parseLong(s));
                        }
                    } else {
                        ids.add(Long.parseLong(str));
                    }
                } catch (NumberFormatException ex) {
                    log.error(LOG_ERROR, ex.getMessage());
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipient rcpnt = (Recipient) o;
        return basicValue.equals(rcpnt.basicValue) && ids.equals(rcpnt.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basicValue, ids);
    }
}
