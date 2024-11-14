package me.goodt.vkpht.module.notification.api.dto.rtcore;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EvaluationEventNotificationDto {
    private Long id;
    private String name;
    private String textForExpert;
    private String textForEvaluated;
    private String location;
    private Boolean eventFormat;
    private Date startDate;
    private Date endDate;
}
