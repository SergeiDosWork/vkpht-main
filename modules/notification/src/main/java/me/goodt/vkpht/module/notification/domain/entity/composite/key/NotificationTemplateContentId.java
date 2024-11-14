package me.goodt.vkpht.module.notification.domain.entity.composite.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateContentId implements Serializable {
    private Long notificationTemplateId;
    private Long notificationRecipientId;
    private Long receiverSystemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationTemplateContentId that = (NotificationTemplateContentId) o;
        return Objects.equals(notificationTemplateId, that.notificationTemplateId) &&
            Objects.equals(notificationRecipientId, that.notificationRecipientId) &&
            Objects.equals(receiverSystemId, that.receiverSystemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationTemplateId, notificationRecipientId, receiverSystemId);
    }
}
