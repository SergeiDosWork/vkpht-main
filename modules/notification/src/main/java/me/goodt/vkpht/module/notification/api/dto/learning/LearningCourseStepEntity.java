package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_END_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_START_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.LEARNING_COURSE_ID_TAG;

@Getter
@Setter
public class LearningCourseStepEntity {
    private Long id;
    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DATE_TO_TAG)
    private Date dateTo;
    private String name;
    @JsonSetter(DATE_START_TAG)
    private Date dateStart;
    @JsonSetter(DATE_END_TAG)
    private Date dateEnd;
    @JsonSetter(LEARNING_COURSE_ID_TAG)
    private Long learningCourseId;
}
