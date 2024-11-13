package me.goodt.vkpht.module.notification.application.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public class GlobalDefs {
    public static final Long EVENT_STATUS = 1L;
    public static final Long EVENT_TYPE_INCOMING_REQUEST = 34L;
    public static final Long EVENT_TYPE_REJECT_REQUEST = 35L;
    public static final Long EVENT_TYPE_REJECT_ASSIGNMENT = 36L;
    public static final Long EVENT_TYPE_ACCEPT_REQUEST = 37L;
    public static final Long EVENT_TYPE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS = 38L;
    public static final Long EVENT_TYPE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION = 39L;
    public static final Long EVENT_TYPE_UPDATE_SUCCESSOR = 40L;
    public static final List<Long> HR_ROLE_LIST_LONG = List.of(3L, 4L);
    public static final Set<Integer> HR_ROLE_LIST_INT = Set.of(3, 4);
    public static final Long EVENT_TYPE_FOR_STATUS_CHANGE = 45L;
    public static final Long EVENT_TYPE_FOR_UPDATE_DTA_ROTATION = 47L;
    public static final Long EVENT_TYPE_FOR_UPDATE_DTA_ROTATION_COMPLETED = 48L;
    public static final List<Long> TASK_LINK_TYPE_IDS = List.of(300L, 301L, 302L);
    public static final Long BFF_MATERIAL_STATUS = 3018L;
    public static final Long BFF_MATERIAL_TYPE = 306L;
    public static final Long BFF_MATERIAL_NAME_FIELD_TYPE_ID = 330L;
    public static final Long BFF_MATERIAL_DESCRIPTION_FIELD_TYPE_ID = 331L;
    public static final Long BFF_MATERIAL_LINK_FIELD_TYPE_ID = 332L;
    public static final Long BFF_PLAN_UPDATE_DATE_FROM_PLAN_FIELD_TYPE_ID = 300L;
    public static final Long BFF_PLAN_UPDATE_DATE_TO_PLAN_FIELD_TYPE_ID = 301L;
    public static final Long BFF_PLAN_UPDATE_GROUP_FLAG_FIELD_TYPE_ID = 302L;
    public static final Long BFF_PLAN_UPDATE_ONBOARDING_PLAN_ID_FIELD_TYPE_ID = 340L;
    public static final Long BFF_PLAN_UPDATE_EMPLOYEE_ID_FIELD_TYPE_ID = 342L;
    public static final Long TASK_TYPE_ID_1 = 1L;
    public static final Long TASK_TYPE_ID_20 = 20L;
    public static final Long TASK_TYPE_ID_80 = 80L;
    public static final Long TASK_TYPE_ID_81 = 81L;
    public static final Long TASK_TYPE_ID_91 = 91L;
    public static final Long TASK_TYPE_ID_92 = 92L;
    public static final Long TASK_TYPE_ID_103 = 103L;
    public static final Long TASK_TYPE_ID_104 = 104L;
    public static final Long TASK_TYPE_ID_107 = 107L;
    public static final Long TASK_TYPE_ID_108 = 108L;
    public static final Long TASK_TYPE_ID_170 = 170L;
    public static final Long TASK_TYPE_ID_172 = 172L;
    public static final Long TASK_TYPE_ID_173 = 173L;
    public static final Long TASK_TYPE_ID_174 = 174L;
    public static final Long TASK_TYPE_ID_181 = 181L;
    public static final Long TASK_TYPE_ID_184 = 184L;
    public static final Long TASK_TYPE_ID_186 = 186L;
    public static final Long TASK_TYPE_ID_190 = 190L;
    public static final Long TASK_TYPE_ID_202 = 202L;
    public static final Long TASK_TYPE_ID_300 = 300L;
    public static final Long TASK_TYPE_ID_301 = 301L;
    public static final Long TASK_TYPE_ID_302 = 302L;
    public static final Long TASK_TYPE_ID_303 = 303L;
    public static final Long TASK_TYPE_ID_304 = 304L;
    public static final Long TASK_TYPE_ID_305 = 305L;
    public static final Long TASK_TYPE_ID_306 = 306L;
    public static final Long TASK_TYPE_ID_307 = 307L;
    public static final Long TASK_TYPE_ID_308 = 308L;
    public static final Long TASK_STATUS_ID_300 = 300L;
    public static final Long TASK_STATUS_ID_402 = 402L;
    public static final Long TASK_STATUS_ID_407 = 407L;
    public static final Long TASK_STATUS_ID_563 = 563L;
    public static final Long TASK_STATUS_ID_567 = 567L;
    public static final Long TASK_STATUS_ID_686 = 686L;
    public static final Long TASK_STATUS_ID_805 = 805L;
    public static final Long TASK_FIELD_TYPE_ID_47 = 47L;
    public static final Long TASK_FIELD_TYPE_ID_140 = 140L;
    public static final Long TASK_FIELD_TYPE_ID_156 = 156L;
    public static final Long TASK_FIELD_TYPE_ID_159 = 159L;
    public static final Long TASK_FIELD_TYPE_ID_211 = 211L;
    public static final Long TASK_FIELD_TYPE_ID_213 = 213L;
    public static final Long TASK_FIELD_TYPE_ID_217 = 217L;
    public static final Long TASK_FIELD_TYPE_ID_218 = 218L;
    public static final Long TASK_FIELD_TYPE_ID_219 = 219L;
    public static final Long TASK_FIELD_TYPE_ID_220 = 220L;
    public static final Long TASK_FIELD_TYPE_ID_221 = 221L;
    public static final Long TASK_FIELD_TYPE_ID_222 = 222L;
    public static final Long TASK_FIELD_TYPE_ID_223 = 223L;
    public static final Long TASK_FIELD_TYPE_ID_265 = 265L;
    public static final Long TASK_FIELD_TYPE_ID_295 = 295L;
    public static final Long TASK_FIELD_TYPE_ID_329 = 329L;
    public static final Long TASK_FIELD_TYPE_ID_334 = 334L;
    public static final Long TASK_FIELD_TYPE_ID_342 = 342L;
    public static final Long TASK_FIELD_TYPE_ID_348 = 348L;
    public static final Long TASK_FIELD_TYPE_ID_350 = 350L;
    public static final Long TASK_FIELD_TYPE_ID_370 = 370L;
    public static final Long TASK_FIELD_TYPE_ID_403 = 403L;
    public static final Long TASK_LINK_TYPE_ID_9 = 9L;
    public static final Long TASK_LINK_TYPE_ID_12 = 12L;
    public static final Long TASK_LINK_TYPE_ID_14 = 14L;
    public static final Long TASK_LINK_TYPE_ID_37 = 37L;
    public static final Long TASK_LINK_TYPE_ID_38 = 38L;
    public static final Long TASK_LINK_TYPE_ID_303 = 303L;
    public static final Long TASK_TYPE_ID_5 = 5L;
    public static final Long TASK_TYPE_ID_7 = 7L;
    public static final Long TASK_TYPE_ID_8 = 8L;
    public static final Long TASK_TYPE_ID_19 = 19L;
    public static final Long PROCESS_TYPE_ID_9 = 9L;
    public static final Long STAGE_TYPE_ID_3 = 3L;
    public static final Long TASK_TYPE_FIELD_ID_295 = 295L;

}
