package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_END_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_SIGN_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_START_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TERMINATION_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.STUDY_GROUP_NUMBER_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.STUDY_STUDENT_EDUCATION_TAG;

@Getter
@Setter
public class StudyStudentEducationContractEntity {
    private Long id;
    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DATE_TO_TAG)
    private Date dateTo;
    private String number;
    @JsonSetter(DATE_SIGN_TAG)
    private Date dateSign;
    @JsonSetter(DATE_START_TAG)
    private Date dateStart;
    @JsonSetter(DATE_END_TAG)
    private Date dateEnd;
    @JsonSetter(STUDY_STUDENT_EDUCATION_TAG)
    private StudyStudentEducationEntity studyStudentEducation;
    @JsonSetter(DATE_TERMINATION_TAG)
    private Date dateTermination;
    @JsonSetter(STUDY_GROUP_NUMBER_TAG)
    private String studyGroupNumber;
}
