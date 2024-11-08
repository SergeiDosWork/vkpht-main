package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextConstants {

    // error messages
    public static final String BAD_REQUEST_MESSAGE_ALL_PARAMETERS_ARE_NULL = "Parameters of method are NULL. At least one method parameter must be passed in the request";
    public static final String BAD_REQUEST_MESSAGE_ALL_PARAMETERS_ARE_NOT_NULL = "All parameters of method are NOT NULL. Only one required method parameter must be passed in the request";
    public static final String CONFLICT_COMPETENCE_IS_EXISTS_FROM_UNIT = "Компетенция с таким названием уже существует";
    public static final String CONFLICT_COMPETENCE_CATALOG_IS_EXISTS_FROM_UNIT = "Папка с таким названием уже существует";
    public static final String ERROR_STATUS_NAME_EXISTS = "Статус с наименованием \"%s\" уже существует.";

	public static final String SEPARATOR_DASH = "_";
	public static final String ID_CANNOT_BE_NULL = "Id cannot be null";

	public static final String PORTAL = "portal";
	public static final String EMAIL = "email";
	public static final String NO_ASSIGNMENTS_FROM_ORGSTRUCTURE = "No assignments from orgstructure";
	public static final String ARRAY_IDS_IS_EMPTY = "Array ids is empty";
	public static final String PARAMS_TYPE_FILE = "{\"type\":\"file\"}";
    public static final String KPI_LINK_CASCADING_STYLE = "{color: #000000, background: #D2E4D2}";
	public static final String PARAMS_TYPE = "type";
	public static final String FILE = "file";
	public static final String CODE_6 = "6";
	public static final String CODE_7 = "7";
	public static final String CODE_9 = "9";
	public static final String CODE_37 = "37";
	public static final String CODE_38 = "38";
	public static final String CODE_39 = "39";
	public static final String CODE_40 = "40";
	//for notifications
	public static String SUBJECT_FOR_MAIL_STATUS_CHANGE = "ИСУ Персонал УЭПиВА";
    public static final String CODE_41 = "41";
    public static final String CODE_42 = "42";
    public static final String CODE_45 = "45";
    public static final String CODE_46 = "46";
    public static final String CODE_60 = "60";
    public static final String CODE_61 = "61";
    public static final String CODE_62 = "62";
    public static final String CODE_106 = "106";
    public static final String CODE_116 = "116";
    public static final String CODE_136 = "136";
    public static final String CODE_137 = "137";
    public static final String CODE_138 = "138";
    public static final String CODE_143 = "143";
    public static final String CODE_153 = "153";
    public static final String CODE_164 = "164";
    public static final String CODE_165 = "165";
    public static final String CODE_166 = "166";
    public static final String CODE_167 = "167";
    public static final String CODE_168 = "168";
    public static final String CODE_169 = "169";
    public static final String CODE_170 = "170";
    public static final String CODE_173 = "173";
    public static final String CODE_174 = "174";
    public static final String CODE_175 = "175";
    public static final String CODE_176 = "176";
    public static final String CODE_177 = "177";
    public static final String CODE_178 = "178";
    public static final String CODE_179 = "179";
    public static final String CODE_180 = "180";
    public static final String CODE_182 = "182";
    public static final String CODE_183 = "183";
    public static final String CODE_184 = "184";
    public static final String CODE_189 = "189";
    public static final String CODE_191 = "191";
    public static final String CODE_192 = "192";
    public static final String CODE_193 = "193";
    public static final String CODE_194 = "194";
    public static final String CODE_195 = "195";
    public static final String CODE_196 = "196";
    public static final String CODE_213 = "213";
    public static final String CODE_218 = "218";
    public static final String CODE_219 = "219";
    public static final String CODE_220 = "220";
    public static final String CODE_227 = "227";
    public static final String CODE_230 = "230";
    public static final String CODE_236 = "236";
    public static final String CODE_237 = "237";
    public static final String CODE_238 = "238";
    public static final String CODE_241 = "241";
    public static final String CODE_242 = "242";
    public static final String CODE_244 = "244";
    public static final String CODE_245 = "245";
    public static final String CODE_294 = "294";
    public static final String CODE_295 = "295";
    public static final String CODE_296 = "296";
    public static final String CODE_297 = "297";
    public static final String CODE_298 = "298";
    public static final String CODE_299 = "299";
    public static final String CODE_300 = "300";
    public static final String CODE_301 = "301";
    public static final String CODE_302 = "302";
    public static final String CODE_507 = "507";
    public static final String CODE_508 = "508";
    public static final String CODE_509 = "509";
    public static final String CODE_701 = "701";
    public static final String CODE_712 = "712";
    public static final String CODE_713 = "713";
    public static final String CODE_714 = "714";
    public static final String CODE_1174 = "1174";
    public static final String CODE_1179 = "1179";
    public static final String CODE_1238 = "1238";
    public static final String CODE_1241 = "1241";
    public static final String CODE_1242 = "1242";
    public static final String TASK_ID_TO_APPRAISAL_EVENT_INFO = "task_id_to_appraisal_event_info";
    public static final String TASK_ID_TO_TASK_HOLDER_INFO = "task_id_to_task_holder_info";
    public static final String TASK_ID_TO_COMPETENCE_PROFILE_INFO = "task_id_to_competence_profile_info";
    public static final String TASK_ID_TO_EVALUATION_EVENT_INFO = "task_id_to_evaluation_event_info";
    public static final String TASK_STATUSCHANGE_COMMENT = "task_statuschange_comment";
    public static final String TASK_ID_TO_PROCESS_INFO = "task_id_to_process_info";
    public static final String TASK_ID_TO_PRACTICE_INFO = "task_id_to_practice_info";
    public static final String TASK_ID_TO_ONBOARDING_INFO = "task_id_to_onboarding_info";
    public static final String TASK_ID_TO_LEARNING_COURSE_INFO = "task_id_to_learning_course_info";
    public static final String TASK_ID_TO_LEARNING_STUDYGROUP_INFO = "task_id_to_learning_studygroup_info";
    public static final String TASK_ID_TO_DEVPLAN_INFO = "task_id_to_devplan_info";
    public static final String TASK_LINK_ID_TO_TASK_ID_TO_HOLDER_INFO = "task_link_id_to_task_id_to_holder_info";
    public static final String TASK_LINK_ID_TO_TASK_ID_FROM_HOLDER_INFO = "task_link_id_to_task_id_from_holder_info";
    public static final String TASK_STATUSCHANGE_STATUS_INFO = "task_statuschange_status_info";
    public static final String SESSION_EMPLOYEE_INFO = "session_employee_info";
    public static final String SESSION_EMPLOYEE_ID = "session_employee_id";
    public static final String TASK_FIELD_VALUE_TO_ONBOARDING_INFO = "task_field_value_to_onboarding_info";
    public static final String TASK_ID = "task_id";
    public static final String TASK_ID_AND_STATUS_ID_TO_TASK_HISTORY_INFO = "task_id_and_status_id_to_task_history_info";
    public static final String POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO = "position_successor_id_to_position_successor_info";
    public static final String POSITION_SUCCESSOR_ID_TO_POSITION_INFO = "position_successor_id_to_position_info";
    public static final String POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_SUCCESSOR_READINESS_INFO = "position_successor_readiness_id_to_position_successor_readiness_info";
    public static final String POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO = "position_successor_readiness_id_to_position_info";
    public static final String POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO = "position_successor_id_to_employee_info";
    public static final String POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO = "position_successor_id_to_employee_by_position_info";
    public static final String DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO = "division_team_successor_id_to_position_info";
    public static final String WEIGHT_TASK = "weight_task";

    /// sign
    public static final String GREATER_TAG = "greater";
    public static final String LESS_TAG = "less";
    public static final String EQUAL_TAG = "equal";
    public static final String GREATER_OR_EQUAL_TAG = "greater_or_equal";
    public static final String LESS_OR_EQUAL_TAG = "less_or_equal";
    public static final String GREATER_OR_LESS_TAG = "greater_or_less";

    public static final String DB_TABLE_MODE = "dbtable";
    public static final String TOKEN_MODE = "token";

    // member_type
    public static final String DIVISION_TEAM_ASSIGNMENT_MEMBER_TYPE = "division_team_assignment";
    public static final String DIVISION_MEMBER_TYPE = "division";
    public static final String LEGAL_ENTITY_MEMBER_TYPE = "legal_entity";
    public static final String COMPETENCE_PROFILE_MEMBER_TYPE = "competence_profile";

	// tasksetting2:

	public static final String TASK_TYPE_181_ID_TO_APPRAISAL_EVENT_INFO = "task_type_181_id_to_appraisal_event_info";

	// conditions
	/// condition_kind_code
	public static final String SUM_WEIGHT_TASK_FIELD_VALUE_TAG = "sum_weight_task_field_value";
	public static final String COUNT_TASK_FIELD_TAG = "count_task_field";
	/// modifier_kind_code
	public static final String TASK_STATUS_ID_TAG = "task_status_id";
	/// sign
	public static final String NOT_NULL_TAG = "not_null";

	// member_type
    public static final String POSITION_RELATED = "position_relation";

	// component_field_code
    public static final String POSITION_PROFILE_COMPETENCE = "position_profile_competence";
    public static final String POSITION_PROFILE_EXPERT = "position_profile_expert";
    public static final String POSITION_PROFILE_EXPERT_ROLE_ID = "position_profile_expert_role_id";
    public static final String C0DE_0 = "0";
    public static final String C0DE_1 = "1";
    public static final String SYSTEM_STATUS_COMPETENCE_ID= "1011";

    //mdi icons
    public static final String MDI_ICON_CHEVRON_DOWN_CIRCLE_OUTLINE = "chevron-down-circle-outline";
    public static final String MDI_ICON_CIRCLE_OFF_OUTLINE = "circle-off-outline";
    public static final String MDI_ICON_CIRCLE_OUTLINE = "circle-outline";
}
