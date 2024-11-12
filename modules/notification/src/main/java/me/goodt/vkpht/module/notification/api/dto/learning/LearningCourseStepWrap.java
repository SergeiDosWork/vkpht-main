package me.goodt.vkpht.module.notification.api.dto.learning;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LearningCourseStepWrap {
	List<LearningCourseStepEntity> content;
}
