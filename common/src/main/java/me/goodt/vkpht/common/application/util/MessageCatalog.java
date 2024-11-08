package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageCatalog {
    public static final String REQUIRED_CONDITION_TITLE = "Обязательные условия";
    public static final String OPTIONAL_CONDITION_TITLE = "Рекомендации";

    // design tasks
    // required conditions messages
    public static final String MIN_REQUIRED_DESIGN_TASKS_MESSAGE = "Поставьте себе цели на будущий период. Целей должно быть не меньше 1";
    public static final String MAX_REQUIRED_DESIGN_TASKS_MESSAGE = "Для отправки на согласование целей должно быть не больше 7.";
    public static final String MIN_KEY_RESULT_DESIGN_TASKS_MESSAGE = "Обязательно укажите ключевые результаты проектных целей. Их должно быть не меньше 3.";
    public static final String MAX_AMBITIOUS_DESIGN_TASKS_MESSAGE = "Не ставьте слишком много амбициозных целей. Их должно быть не больше 2. ";
    public static final Integer MIN_REQUIRED_DESIGN_TASKS_CODE = 1;
    public static final Integer MAX_REQUIRED_DESIGN_TASKS_CODE = 2;
    public static final Integer MIN_KEY_RESULT_DESIGN_TASKS_CODE = 3;
    public static final Integer MAX_AMBITIOUS_DESIGN_TASKS_CODE = 4;

    public static final String MIN_REQUIRED_DESIGN_TASK_WEIGHT_MESSAGE = "Каждая цель должна иметь вес, не меньше 10%";
    public static final String MIN_REQUIRED_DESIGN_TASK_WEIGHT_GROUP_ONE_MESSAGE = "Каждая процессная цель (включая обязательную) должна иметь вес, не меньше 5%";
    public static final String MIN_REQUIRED_DESIGN_TASK_WEIGHT_GROUP_TWO_MESSAGE = "Каждая проектная цель должна иметь вес, не меньше 15%";
    public static final String MAX_REQUIRED_DESIGN_TASK_WEIGHT_MESSAGE = "Вес любой цели должен быть не больше 50%";
    public static final String MIN_AMBITIOUS_DESIGN_TASK_WEIGHT_MESSAGE = "Вес цели с критерием амбициозности не должен быть меньше 20%.";
    public static final String TOTAL_DESIGN_TASK_WEIGHT_MESSAGE = "Сумма весов всех целей должна равняться 100%. ";
    public static final String CONDITIONS_OF_APPROVAL_DESIGN_TASKS_MESSAGE = "Условия утверждения целей";
    public static final String MIN_REQUIRED_WEIGHT_FOR_INDEX_DESIGN_TASK_MESSAGE = "Каждый показатель должен иметь вес, не меньше 5%";
    public static final Integer MIN_REQUIRED_DESIGN_TASK_WEIGHT_CODE = 5;
    public static final Integer MIN_REQUIRED_DESIGN_TASK_WEIGHT_GROUP_ONE_CODE = 6;
    public static final Integer MIN_REQUIRED_DESIGN_TASK_WEIGHT_GROUP_TWO_CODE = 7;
    public static final Integer MAX_REQUIRED_DESIGN_TASK_WEIGHT_CODE = 8;
    public static final Integer MIN_AMBITIOUS_DESIGN_TASK_WEIGHT_CODE = 9;
    public static final Integer TOTAL_DESIGN_TASK_WEIGHT_CODE = 10;
    public static final Integer CONDITIONS_OF_APPROVAL_DESIGN_TASKS_CODE = 11;
    public static final Integer MIN_REQUIRED_WEIGHT_FOR_INDEX_DESIGN_TASK_CODE = 52;

    // optional conditions messages
    public static final String MIN_AMBITIOUS_DESIGN_TASKS_MESSAGE = "Стремитесь поставить себе хотя бы одну амбициозную цель.";
    public static final String MAX_DESIGN_DESIGN_TASKS_MESSAGE = "Постарайтесь, чтобы проектных целей было не больше 4.";
    public static final String RESULT_VALUES_DESIGN_TASKS_MESSAGE = "Стремитесь дополнить все ваши цели измеримыми ключевыми этапами (результатами). Рекомендуется дополнять 4 ключевыми этапами.";
    public static final Integer MIN_AMBITIOUS_DESIGN_TASKS_CODE = 12;
    public static final Integer MAX_DESIGN_DESIGN_TASKS_CODE = 13;
    public static final Integer RESULT_VALUES_DESIGN_TASKS_CODE = 14;

    public static final String MIN_OPTIONAL_DESIGN_TASKS_MESSAGE = "Стремитесь поставить себе хотя бы 5 целей.";
    public static final String MAX_KEY_RESULT_DESIGN_TASKS_MESSAGE = "Постарайтесь поставить себе от 3 до 5 ключевых этапов (результатов) в каждой цели.";
    public static final Integer MIN_OPTIONAL_DESIGN_TASKS_CODE = 15;
    public static final Integer MAX_KEY_RESULT_DESIGN_TASKS_CODE = 16;

    // skills tasks
    public static final String MIN_ZERO_WEIGHT_SKILL_TASKS_MESSAGE = "Траекторий развития должно быть не больше 2 шт.";
    public static final String TOTAL_WEIGHT_SKILL_TASKS_MESSAGE = "Сумма весов должна быть равна 100%.";
    public static final String MIN_PERSONAL_GROW_SKILL_TASKS_MESSAGE = "Развиваемых компетенций должно быть не меньше 6 шт.";
    public static final String MAX_DEVELOPING_SKILL_TASKS_MESSAGE = "Развиваемых компетенций должно быть не больше 12 шт.";
    public static final String EVERY_DEVELOPING_SKILL_TASK_HAS_LINK_MESSAGE = "Каждая развиваемая компетенция связывается с мероприятием, выбранным из форм развития.";
    public static final String HEAD_CONDITIONS_OF_APPROVAL_SKILL_TASKS_MESSAGE = "Условия утверждения ИПР руководителем";
    public static final Integer MIN_ZERO_WEIGHT_SKILL_TASKS_CODE = 17;
    public static final Integer TOTAL_WEIGHT_SKILL_TASKS_CODE = 18;
    public static final Integer MIN_PERSONAL_GROW_SKILL_TASKS_CODE = 19;
    public static final Integer MAX_DEVELOPING_SKILL_TASKS_CODE = 20;
    public static final Integer EVERY_DEVELOPING_SKILL_TASK_HAS_LINK_CODE = 21;
    public static final Integer HEAD_CONDITIONS_OF_APPROVAL_SKILL_TASKS_CODE = 22;

    //techserv tasks
    //required conditions messages
    public static final String MIN_REQUIRED_TECHSERV_TASKS_MESSAGE = "Поставьте себе цели на будущий период. Целей должно быть не меньше 3.";
    public static final String MAX_REQUIRED_TECHSERV_TASKS_MESSAGE = "Для отправки на согласование целей должно быть не больше 20.";
    public static final String MAX_KEY_RESULT_TECHSERV_TASKS_MESSAGE = "Не указывайте слишком много ключевых результатов (этапов) цели. Их должно быть не больше 10.";
    public static final String MAX_REQUIRED_TECHSERV_TASK_WEIGHT_MESSAGE = "Вес любой цели должен быть не больше 80%.";
    public static final String TOTAL_TECHSERV_TASK_WEIGHT_MESSAGE = "Сумма весов всех целей должна равняться 100%.";
    public static final Integer MIN_REQUIRED_TECHSERV_TASKS_CODE = 23;
    public static final Integer MAX_REQUIRED_TECHSERV_TASKS_CODE = 24;
    public static final Integer MAX_KEY_RESULT_TECHSERV_TASKS_CODE = 25;
    public static final Integer MAX_REQUIRED_TECHSERV_TASK_WEIGHT_CODE = 26;
    public static final Integer TOTAL_TECHSERV_TASK_WEIGHT_CODE = 27;
    //optional conditions messages
    public static final String MIN_KEY_RESULTS_TECHSERV_TASK_MESSAGE = "Постарайтесь, чтобы ключевых результатов (этапов) у цели было не меньше 3.";
    public static final String MIN_TECHSERV_TASKS_MESSAGE = "Стремитесь поставить себе 12 целей.";
    public static final Integer MIN_KEY_RESULTS_TECHSERV_TASK_CODE = 28;
    public static final Integer MIN_TECHSERV_TASKS_CODE = 29;

    //adaptation tasks
    //required conditions messages
    public static final String TASKS_COMPLETED_ADAPTATION_MESSAGE = "Новый сотрудник выполнил все задачи адаптации.";
    public static final String TASKS_DATE_ADAPTATION_MESSAGE = "Подошло время подтверждения испытательного срока.";
    public static final String PROBATIONARY_PERIOD_HAS_ENDED_ADAPTATION_MESSAGE = "Испытательный срок завершился.";
    public static final Integer TASKS_COMPLETED_ADAPTATION_CODE = 30;
    public static final Integer TASKS_DATE_ADAPTATION_CODE = 31;
    public static final Integer PROBATIONARY_PERIOD_HAS_ENDED_ADAPTATION_CODE = 45;

    //competence profile tasks
    //required conditions messages
    public static final String RIGHT_COMPETENCE_AMOUNT_MESSAGE = "Отлично, у вас не более 7 digital компетенций.";
    public static final String ERROR_COMPETENCE_AMOUNT_MESSAGE = "Старайтесь указывать одновременно не более 7 профильных и digital компетенций.";
    public static final String DATE_INCLUSION_IS_EXISTED_MESSAGE = "Указана дата включения.";
    public static final String POSITION_IS_NOT_LINKED_TO_ANOTHER_PROFILE_MESSAGE = "Позиция не имеет привязки к другому профилю.";
    public static final String CONTAIN_ALL_COMPETENCE_CATALOG_ID_MESSAGE = "Добавлено по одной компетенции каждого типа.";
    public static final String ADDED_HR_HEAD_INTO_COMPETENCY_PROFILE_APPROVAL_PATH_MESSAGE = "В путь согласования профиля компетенций добавлен Руководитель HR организации/филиала.";
    public static final String AT_LEAST_ONE_EMPLOYEE_IS_EXPERT_PARENT_COMPETENCY_PROFILE_MESSAGE = "Выбран хотя бы один работник на роль Эксперта (Родительская).";
    public static final String AT_LEAST_ONE_EMPLOYEE_IS_EXPERT_CHILD_COMPETENCY_PROFILE_MESSAGE = "Выбран хотя бы один работник на роль Эксперта (Дочерняя).";
    public static final String COMPETENCY_PROFILE_MUST_BE_APPROVED_COMPETENCY_PROFILE_MESSAGE = "Профиль компетенций должен быть Утвержден.";
    public static final Integer RIGHT_COMPETENCE_AMOUNT_CODE = 32;
    public static final Integer ERROR_COMPETENCE_AMOUNT_CODE = 33;
    public static final Integer DATE_INCLUSION_IS_EXISTED_CODE = 39;
    public static final Integer POSITION_IS_NOT_LINKED_TO_ANOTHER_PROFILE_CODE = 40;
    public static final Integer CONTAIN_ALL_COMPETENCE_CATALOG_ID_CODE = 41;
    public static final Integer ADDED_HR_HEAD_INTO_COMPETENCY_PROFILE_APPROVAL_PATH_CODE = 42;
    public static final Integer AT_LEAST_ONE_EMPLOYEE_IS_EXPERT_PARENT_COMPETENCY_PROFILE_CODE = 46;
    public static final Integer AT_LEAST_ONE_EMPLOYEE_IS_EXPERT_CHILD_COMPETENCY_PROFILE_CODE = 47;
    public static final Integer COMPETENCY_PROFILE_MUST_BE_APPROVED_COMPETENCY_PROFILE_CODE = 48;

    //quarter tasks
    //required conditions messages
    public static final String MIN_TASK_AMOUNT_IN_QUARTER_QUARTER_TASKS_MESSAGE = "В периоде \"квартал\" не может быть меньше 2х целей";
    public static final String MIN_TASK_AMOUNT_IN_YEAR_QUARTER_TASKS_MESSAGE = "В периоде \"год\" не может быть меньше 2х целей";
    public static final String MAX_TASK_WEIGHT_QUARTER_TASKS_MESSAGE = "Максимальный вес одной цели не может быть более 80%";
    public static final String REQUIRED_TASK_WEIGHT_QUARTER_TASKS_MESSAGE = "Необходимо проставить веса целей минимум в одном квартале";
    public static final Integer MIN_TASK_AMOUNT_IN_QUARTER_QUARTER_TASKS_CODE = 34;
    public static final Integer MIN_TASK_AMOUNT_IN_YEAR_QUARTER_TASKS_CODE = 35;
    public static final Integer MAX_TASK_WEIGHT_QUARTER_TASKS_CODE = 36;
    public static final Integer REQUIRED_TASK_WEIGHT_QUARTER_TASKS_CODE = 43;
    //optional conditions messages
    public static final String MAX_TASK_AMOUNT_IN_QUARTER_QUARTER_TASKS_MESSAGE = "В периоде квартал рекомендовано ставить не более 10 целей";
    public static final String MAX_TASK_AMOUNT_IN_YEAR_QUARTER_TASKS_MESSAGE = "В периоде год рекомендовано ставить не более 20 целей";
    public static final Integer MAX_TASK_AMOUNT_IN_QUARTER_QUARTER_TASKS_CODE = 37;
    public static final Integer MAX_TASK_AMOUNT_IN_YEAR_QUARTER_TASKS_CODE = 38;

    //task type 17
    public static final Integer ANALYSIS_APPROVAL_CONDITION_TASK_TYPE_17_CODE = 44;
    public static final String ANALYSIS_APPROVAL_CONDITION_TASK_TYPE_17_MESSAGE = "Условия утверждения анализа";

    //task type 170
    public static final Integer COMPETENCE_EXISTS_CONDITION_TASK_TYPE_17_CODE = 49;
    public static final Integer UNIQUENESS_OF_EMPLOYEES_CONDITION_TASK_TYPE_17_CODE = 50;
    public static final Integer ALLOWABLE_COMPOSITION_OF_EMPLOYEES_CONDITION_TASK_TYPE_17_CODE = 51;
    public static final String COMPETENCE_EXISTS_CONDITION_TASK_TYPE_17_MESSAGE = "Наличие компетенции/ий для оценки";
    public static final String UNIQUENESS_OF_EMPLOYEES_CONDITION_TASK_TYPE_17_MESSAGE = "Уникальность работников на ролях Экспертов";
    public static final String ALLOWABLE_COMPOSITION_OF_EMPLOYEES_CONDITION_TASK_TYPE_17_MESSAGE = "Допустимый состав работников на роли Экспертов";

    public static final Integer ALL_MATCHERS_SELECTED_CODE = 52;
    public static final Integer MONITORING_PERIOD_SET_CODE = 53;
    public static final Integer MIN_ONE_COMPETENCE_CODE = 54;
    public static final Integer MIN_ONE_FORM_OF_DEVELOPMENT_CODE = 55;
    public static final String ALL_MATCHERS_SELECTED_MESSAGE = "Выбраны все согласующие";
    public static final String MONITORING_PERIOD_SET_MESSAGE = "Установлен период мониторинга";
    public static final String MIN_ONE_COMPETENCE_MESSAGE = "Добавлена минимум 1 компетенция";
    public static final String MIN_ONE_FORM_OF_DEVELOPMENT_MESSAGE = "Добавлена минимум 1 форма развития";
}
