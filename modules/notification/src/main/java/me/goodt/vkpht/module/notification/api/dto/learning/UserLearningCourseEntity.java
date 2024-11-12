package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.LEARNING_COURSE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.USER_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.USER_LEARNING_COURSE_STEPS_TAG;

@Getter
@Setter
public class UserLearningCourseEntity {
	@JsonSetter(USER_ID_TAG)
	private Long userId;
	@JsonSetter(LEARNING_COURSE_TAG)
	private LearningCourseEntity learningCourse;
	@JsonSetter(USER_LEARNING_COURSE_STEPS_TAG)
	private List<UserLearningCourseStepEntity> userLearningCourseSteps;
}
