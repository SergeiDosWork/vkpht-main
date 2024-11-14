package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public class GlobalDefs {
    public static final String API_VERSION = "1.0";
    public static final String DATABASE = "H2 In Memory DB";
    public static final String ENVIRONMENT = "LOCAL";

    public static final Integer HEAD_SYSTEM_ROLE_ID = 1;
    public static final Integer HEAD_ID = 1;
    public static final String HEAD_SYSTEM_ROLE_NAME = "head";

    public static final Set<Integer> HR_ROLE_SET = Set.of(3, 4);

    public static final Long TASK_OWNER_ROLE_CODE = 1L;
    public static final Long TASK_EMPLOYEE_HEAD_ROLE_CODE = 2L;
    public static final Long TASK_EMPLOYEE_SUBORDINATE_ROLE_CODE = 3L;
    public static final Long TASK_EMPLOYEE_SUBORDINATE_HEAD_ROLE_CODE = 4L;
    public static final Long TASK_ASSIGNMENT_ROLE_CODE = 5L;
    public static final Long TASK_HR_ROLE_CODE = 6L;
    public static final Long TASK_HEAD_ROLE_CODE = 7L;
    public static final Long TASK_OTHER_ROLE_CODE = -1L;
    public static final Long DATASOURCE_ROLE_ID = 8L;
    public static final Long EVELUATOR_ROLE_ID = 9L;
    public static final String TASK_WEIGHT_TYPE = "{\"type\":\"weight\"}";
    public static final String TASK_PROGRESS_TYPE = "{\"type\":\"progress\"}";
    public static final String TASK_ONBOARDING_DATE_TO_PLAN_TYPE = "{\"type\":\"onboarding_date_to_plan\"}";
    public static final String TASK_PROBATION_DATE_TO_PLAN_TYPE = "{\"type\":\"probation_date_to_plan\"}";
	/*
	 * task_org.org_role ids start
	 */
	public static final Long EMPLOYEE_ORG_ROLE_AS_HEAD = 1L;
	public static final Long EMPLOYEE_ORG_ROLE_AS_EMPLOYEE = 2L;
	public static final Long EMPLOYEE_ORG_ROLE_AS_BRANCH_OFFICE_HR = 3L;
	public static final Long EMPLOYEE_ORG_ROLE_AS_COMPANY_HR = 4L;
	public static final List<Integer> HR_ROLE_LIST = List.of(EMPLOYEE_ORG_ROLE_AS_BRANCH_OFFICE_HR.intValue(), EMPLOYEE_ORG_ROLE_AS_COMPANY_HR.intValue());

	/*
	 * task_org.org_role ids end
	 */
    public static final Set<Long> TASK_TYPE_SET = Set.of(1L, 7L, 9L, 11L, 13L, 15L, 17L, 20L, 21L, 22L, 90L, 188L, 190L, 300L, 301L, 302L, 303L, 304L, 305L, 306L, 308L);
    public static final Long TASK_TYPE_PUBLIC = 2L;

    public static final Long TASK_USER_TYPE_TEAM_DIVISION_ASSIGNMENT = 1L;
    public static final Long TASK_USER_TYPE_DIVISION = 2L;
    public static final Long TASK_USER_TYPE_LEGAL_ENTITY = 3L;
	public static final Long TASK_USER_TYPE_EMPLOYEE = 7L;
	public static final Long TASK_USER_TYPE_PROCESS = 8L;
    public static final Set<Long> USER_TYPE_ID_LIST = Set.of(2L, 3L, 4L);

    // Logging
    public static final String ACTION_FOR_ENTITY = "%s for entity = %s";

    public static final Long TASK_LINK_TYPE_ID_2 = 2L;
    public static final Integer SYSTEM_ROLE_ID_FOR_BUDDY = 7;
    public static final Long TASK_LINK_TYPE_ID_FOR_CHECK_BUDDY = 11L;
    public static final Long TASK_LINK_TYPE_ID_FOR_CHECK_BUDDY_20 = 20L;
    public static final List<Long> TASK_LINK_TYPE_ID_LIST_FOR_CHECK_BUDDY_300_301_302 = List.of(300L, 301L, 302L);
    public static final List<Long> TASK_LINK_TYPE_ID_LIST_FOR_CHECK_EXPERT_9_14 = List.of(9L, 14L);
    public static final Long TASK_TYPE_ID_202 = 202L;
    public static final Long TASK_TYPE_ID_20 = 20L;
    public static final List<Long> TASK_LINK_TYPE_ID_LIST_37_38_39 = List.of(37L, 38L, 39L);
    public static final Long TASK_TYPE_ID_FOR_CHECK_BUDDY_120 = 120L;
    public static final Long TASK_TYPE_ID_FOR_CHECK_BUDDY_301 = 301L;
    public static final Long TASK_TYPE_ID_FOR_CHECK_EXPERT = 120L;
    public static final Set<Long> TASK_TYPE_ID_SET_FOR_UPLOAD_FILE = Set.of(308L, 304L);
    public static final Set<Long> TASK_TYPE_FIELD_ID_SET_FOR_DOWNLOAD_FILE = Set.of(336L, 327L);

    public static final Set<Long> TASK_STATUS_ID_LIST_FOR_EMPLOYEE = Set.of(602L, 604L, 605L, 624L);
    public static final Set<Long> TASK_STATUS_ID_LIST_FOR_HEAD_EMPLOYEE = Set.of(603L, 604L);
    public static final Set<Long> TASK_STATUS_ID_LIST_FOR_HR_EMPLOYEE = Set.of(602L, 605L);
    public static final Set<Long> TASK_STATUS_ID_FULL_LIST_FOR_TASK_STATUS_CHANGE_FROM_ROSTALENT = Set.of(602L, 603L, 604L, 605L, 624L);
    public static final Set<Long> TASK_STATUS_ID_LIST_FOR_STATUS_CHANGE = Set.of(78L, 79L, 80L, 81L, 82L, 83L, 84L, 85L, 86L, 87L, 88L);

    public static final Long EVENT_TASK_STATUS_ID_DRAFT = 12L;
    public static final Long EVENT_TASK_STATUS_ID_ACTIVE = 13L;
    public static final Long EVENT_TASK_STATUS_ID_ARCHIVE = 14L;

    public static final Long TASK_TYPE_ID_1 = 1L;
    public static final Long TASK_TYPE_ID_2 = 2L;
    public static final Long TASK_TYPE_ID_3 = 3L;
    public static final Long TASK_TYPE_ID_5 = 5L;
    public static final Long TASK_TYPE_ID_6 = 6L;
    public static final Long TASK_TYPE_ID_9 = 9L;
    public static final Long TASK_TYPE_ID_10 = 10L;
    public static final Long TASK_TYPE_ID_11 = 11L;
    public static final Long TASK_TYPE_ID_12 = 12L;
    public static final Long TASK_TYPE_ID_13 = 13L;
    public static final Long TASK_TYPE_ID_14 = 14L;
    public static final Long TASK_TYPE_ID_15 = 15L;
    public static final Long TASK_TYPE_ID_16 = 16L;
    public static final Long TASK_TYPE_ID_17 = 17L;
    public static final Long TASK_TYPE_ID_80 = 80L;
    public static final Long TASK_TYPE_ID_90 = 90L;
    public static final Long TASK_TYPE_ID_91 = 91L;
    public static final Long TASK_TYPE_ID_92 = 92L;
    public static final Long TASK_TYPE_ID_102 = 102L;
    public static final Long TASK_TYPE_ID_101 = 101L;
    public static final Long TASK_TYPE_ID_104 = 104L;
    public static final Long TASK_TYPE_ID_108 = 108L;
    public static final Long TASK_TYPE_ID_111 = 111L;
    public static final Long TASK_TYPE_ID_113 = 113L;
    public static final Long TASK_TYPE_ID_114 = 114L;
    public static final Long TASK_TYPE_ID_116 = 116L;
    public static final Long TASK_TYPE_ID_117 = 117L;
    public static final Long TASK_TYPE_ID_119 = 119L;
    public static final Long TASK_TYPE_ID_170 = 170L;
    public static final Long TASK_TYPE_ID_171 = 171L;
    public static final Long TASK_TYPE_ID_172 = 172L;
    public static final Long TASK_TYPE_ID_173 = 173L;
    public static final Long TASK_TYPE_ID_174 = 174L;
    public static final Set<Long> SET_TASK_TYPE_ID_101_102 = Set.of(TASK_TYPE_ID_101, TASK_TYPE_ID_102);
    public static final Long TASK_TYPE_ID_181 = 181L;
    public static final Long TASK_TYPE_ID_184 = 184L;
    public static final Long TASK_TYPE_ID_188 = 188L;
    public static final Long TASK_TYPE_ID_189 = 189L;
    public static final Long TASK_TYPE_ID_190 = 190L;
    public static final Long TASK_TYPE_ID_300 = 300L;
    public static final Long TASK_TYPE_ID_301 = 301L;
    public static final Long TASK_TYPE_ID_304 = 304L;

    public static final Long TASK_FIELD_TYPE_ID_223 = 223L;
    public static final Long TASK_FIELD_TYPE_ID_225 = 225L;
    public static final Long TASK_FIELD_TYPE_ID_213 = 213L;
    public static final Long TASK_FIELD_TYPE_ID_215 = 215L;
    public static final Long TASK_FIELD_TYPE_ID_217 = 217L;
    public static final Long TASK_FIELD_TYPE_ID_218 = 218L;
    public static final Long TASK_FIELD_TYPE_ID_219 = 219L;
    public static final Long TASK_FIELD_TYPE_ID_222 = 222L;
    public static final Long STATUS_EVALUATION_ACQUAINTANCE = 10L;

    public static final Long STATUS_ID_2 = 2L;
    public static final Long STATUS_ID_9 = 9L;
    public static final Long STATUS_ID_322 = 322L;
    public static final Long STATUS_ID_323 = 323L;
    public static final Long STATUS_ID_325 = 325L;
    public static final Long STATUS_ID_326 = 326L;
    public static final Long STATUS_ID_328 = 328L;
    public static final Long STATUS_ID_329 = 329L;
    public static final Long STATUS_ID_330 = 330L;
    public static final Long STATUS_ID_336 = 336L;
    public static final Long STATUS_ID_337 = 337L;
    public static final Long STATUS_ID_338 = 338L;
    public static final Long STATUS_ID_402 = 402L;
    public static final Long STATUS_ID_550 = 550L;
    public static final Long STATUS_ID_552 = 552L;
    public static final Long STATUS_ID_567 = 567L;
    public static final Long STATUS_ID_681 = 681L;
    public static final Long STATUS_ID_682 = 682L;
    public static final Long STATUS_ID_683 = 683L;
    public static final Long STATUS_ID_684 = 684L;
    public static final Long STATUS_ID_685 = 685L;
    public static final Long STATUS_ID_687 = 687L;
    public static final Long STATUS_ID_688 = 688L;
    public static final Long STATUS_ID_805 = 805L;
    public static final Long STATUS_ID_824 = 824L;
    public static final Long STATUS_ID_3002 = 3002L;
    public static final Long STATUS_ID_3003 = 3003L;
    public static final Long STATUS_ID_3004 = 3004L;
    public static final Long STATUS_ID_3005 = 3005L;
    public static final Long STATUS_ID_3015 = 3015L;
    public static final Long STATUS_ID_3028 = 3028L;
    public static final Long STATUS_ID_3037 = 3037L;
    public static final Long STATUS_ID_3038 = 3038L;
    public static final Long STATUS_ID_3039 = 3039L;
    public static final Long STATUS_ID_3040 = 3040L;
    public static final Long STATUS_ID_3041 = 3041L;
    public static final Long STATUS_ID_3042 = 3042L;
    public static final Long STATUS_ID_3044 = 3044L;
    public static final Long STATUS_ID_3045 = 3045L;
    public static final Long STATUS_ID_3705 = 3705L;
    public static final Long STATUS_ID_3706 = 3706L;
    public static final Long STATUS_ID_3707 = 3707L;

    public static final Long STATUS_ID_825 = 825L;

    public static final Long TASK_TYPE_FIELD_ID_7 = 7L;
    public static final Long TASK_TYPE_FIELD_ID_13 = 13L;
    public static final Long TASK_TYPE_FIELD_ID_18 = 18L;
    public static final Long TASK_TYPE_FIELD_ID_24 = 24L;
    public static final Long TASK_TYPE_FIELD_ID_50 = 50L;
    public static final Long TASK_TYPE_FIELD_ID_213 = 213L;
    public static final Long TASK_TYPE_FIELD_ID_216 = 216L;
    public static final Long TASK_TYPE_FIELD_ID_217 = 217L;
    public static final Long TASK_TYPE_FIELD_ID_349 = 349L;
    public static final Long TASK_TYPE_FIELD_ID_350 = 350L;
    public static final Long TASK_TYPE_FIELD_ID_351 = 351L;
    public static final Long TASK_TYPE_FIELD_ID_352 = 352L;
    public static final Long TASK_TYPE_FIELD_ID_403 = 403L;
    public static final Long TASK_TYPE_FIELD_ID_508 = 508L;
    public static final Long TASK_TYPE_FIELD_ID_737 = 737L;
    public static final Long TASK_TYPE_FIELD_PLAN_KPE_VALUE = 12L;
    public static final Long TASK_TYPE_FIELD_ACTUAL_KPE_VALUE = 13L;
    public static final Long TASK_TYPE_FIELD_KPE_COMPLETION_VALUE = 15L;

    public static final Set<Long> TASK_TYPE_FIELD_IDS_349_351_352 = Set.of(
            TASK_TYPE_FIELD_ID_349,
            TASK_TYPE_FIELD_ID_351,
            TASK_TYPE_FIELD_ID_352
    );

    public static final Long TASK_LINK_TYPE_ID_9 = 9L;
    public static final Long TASK_LINK_TYPE_ID_14 = 14L;
    public static final Long TASK_LINK_TYPE_ID_35 = 35L;
    public static final Long TASK_LINK_TYPE_ID_37 = 37L;

    public static final Long ACCESS_TYPE_PRIVATE = 1L;
    public static final Long ACCESS_TYPE_PUBLIC = 2L;
    public static final Long ACCESS_TYPE_FULL_COMPLETE = 3L;
    public static final Long ACCESS_TYPE_FULL_LEGAL_ENTITY = 4L;

    public static final Long COMPONENT_EVALUATION_SCALE = 9999L;


}
