--liquibase formatted sql
--changeset d.oskin:init-notification-schema

-- notification_receiver_system definition
CREATE TABLE notification_receiver_system (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	"name" varchar(256) NOT NULL,
	description varchar(1024) NULL,
	unit_code varchar(128) DEFAULT 'default'::character varying NOT NULL,
	is_active bool DEFAULT true NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	is_editable_if_system bool DEFAULT false NOT NULL,
	is_enabled bool DEFAULT false NOT NULL,
	CONSTRAINT "notification_receiver_systemPK" PRIMARY KEY (id)
);
COMMENT ON TABLE notification_receiver_system IS 'Каналы отправки уведомления';
COMMENT ON COLUMN notification_receiver_system.id IS 'Синтетический id';
COMMENT ON COLUMN notification_receiver_system."name" IS 'Наименование';
COMMENT ON COLUMN notification_receiver_system.description IS 'Описание';
COMMENT ON COLUMN notification_receiver_system.unit_code IS 'Код юнита';
COMMENT ON COLUMN notification_receiver_system.is_active IS 'Признак активности записи (отображение на фронте, использование в системе и тд.)';
COMMENT ON COLUMN notification_receiver_system.is_system IS 'Признак указывающий на основание создания процесса (true - процесс поставляется вместе с системой, false - создан на проекте)';
COMMENT ON COLUMN notification_receiver_system.is_editable_if_system IS 'Признак указывающий на доступность редактирования системной записи';


-- notification_recipient definition
CREATE TABLE notification_recipient (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	"name" varchar(256) NOT NULL,
	description varchar(1024) NULL,
	is_system bool DEFAULT false NOT NULL,
	unit_code varchar(128) DEFAULT 'default'::character varying NOT NULL,
	CONSTRAINT "notification_recipientPK" PRIMARY KEY (id)
);
COMMENT ON COLUMN notification_recipient.unit_code IS 'Код юнита';


-- notification_template definition
CREATE TABLE notification_template (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	date_from timestamp DEFAULT now() NOT NULL,
	date_to timestamp NULL,
	author_employee_id BIGINT NULL,
	author_update_employee_id BIGINT NULL,
	date_update timestamp NULL,
	"name" varchar(256) NULL,
	code varchar(128) NULL,
	description varchar(1024) DEFAULT ''::character varying NULL,
	is_enabled int2 DEFAULT 0 NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	unit_code varchar(128) DEFAULT 'default'::character varying NOT NULL,
	CONSTRAINT "notification_template_PK" PRIMARY KEY (id)
);
COMMENT ON COLUMN notification_template.unit_code IS 'Код юнита';


-- notification_token definition
CREATE TABLE notification_token (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	"name" varchar(256) NOT NULL,
	description varchar(1024) NULL,
	short_name varchar(256) NULL,
	group_name varchar(256) NOT NULL,
	unit_code varchar(128) DEFAULT 'default'::character varying NOT NULL,
	CONSTRAINT notification_token_pkey PRIMARY KEY (id),
	CONSTRAINT uq_notification_token_unit_code_name UNIQUE (unit_code, name)
);
COMMENT ON TABLE notification_token IS 'Информация о токенах уведомлений';
COMMENT ON COLUMN notification_token.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN notification_token."name" IS 'Полное наименование токена';
COMMENT ON COLUMN notification_token.description IS 'Описание токена';
COMMENT ON COLUMN notification_token.short_name IS 'Краткое наименование токена';
COMMENT ON COLUMN notification_token.group_name IS 'Группа к которой относится токен';
COMMENT ON COLUMN notification_token.unit_code IS 'Код юнита';


-- notification_token_constant definition
CREATE TABLE notification_token_constant (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	token_full_name varchar(256) NOT NULL,
	constant varchar(128) NOT NULL,
	value BIGINT NOT NULL,
	unit_code varchar(128) DEFAULT 'default'::character varying NOT NULL,
	CONSTRAINT "notification_token_constantPK" PRIMARY KEY (id),
	CONSTRAINT uq_notification_token_constant_token_full_name UNIQUE (token_full_name)
);
COMMENT ON COLUMN notification_token_constant.unit_code IS 'Код юнита';


-- notification_receiver_system_employee_disabled definition
CREATE TABLE notification_receiver_system_employee_disabled (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	employee_id BIGINT NOT NULL,
	notification_receiver_system_id BIGINT NOT NULL,
	CONSTRAINT notification_receiver_system_employee_disabled_pkey PRIMARY KEY (id),
	CONSTRAINT "notification_receiver_system_id_FK" FOREIGN KEY (notification_receiver_system_id) REFERENCES notification_receiver_system(id)
);
COMMENT ON TABLE notification_receiver_system_employee_disabled IS 'Отключенные каналы отправки уведомлений для Сотрудников';
COMMENT ON COLUMN notification_receiver_system_employee_disabled.id IS 'Синтетический id';
COMMENT ON COLUMN notification_receiver_system_employee_disabled.employee_id IS 'ID сотрудника в orgstructure.Employee';
COMMENT ON COLUMN notification_receiver_system_employee_disabled.notification_receiver_system_id IS 'id канала отправки уведомления в notification_receiver_system (FK)';


-- notification_recipient_email definition
CREATE TABLE notification_recipient_email (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	email varchar NOT NULL,
	notification_recipient_id BIGINT NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	CONSTRAINT notification_recipient_email_pkey PRIMARY KEY (id),
	CONSTRAINT fk_notification_recipient_id FOREIGN KEY (notification_recipient_id) REFERENCES notification_recipient(id)
);
CREATE INDEX fk_notification_recipient_id_idx ON notification_recipient_email USING btree (notification_recipient_id);
COMMENT ON TABLE notification_recipient_email IS 'Статичные Email получателей уведомления';
COMMENT ON COLUMN notification_recipient_email.id IS 'Синтетический id';
COMMENT ON COLUMN notification_recipient_email.email IS 'Статичный Email получателя';
COMMENT ON COLUMN notification_recipient_email.notification_recipient_id IS 'ID получателя уведомления (notification_recipient)';
COMMENT ON COLUMN notification_recipient_email.is_system IS 'Статичный Email получателя';


-- notification_recipient_parameters definition
CREATE TABLE notification_recipient_parameters (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	value BIGINT NOT NULL,
	notification_recipient_id BIGINT NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	CONSTRAINT notification_recipient_parameters_pkey PRIMARY KEY (id),
	CONSTRAINT "notification_recipient_id_FK" FOREIGN KEY (notification_recipient_id) REFERENCES notification_recipient(id)
);
COMMENT ON TABLE notification_recipient_parameters IS 'Таблица содержит параметры для вычисляемых групп получателей уведомлений';
COMMENT ON COLUMN notification_recipient_parameters.id IS 'Идентификатор записи';
COMMENT ON COLUMN notification_recipient_parameters.value IS 'Идентификатор из таблицы вычисляемых групп получателей уведомлений';
COMMENT ON COLUMN notification_recipient_parameters.notification_recipient_id IS 'Значение для вычисляемой группы получателей';


-- notification_template_content definition
CREATE TABLE notification_template_content (
	notification_template_id BIGINT GENERATED ALWAYS AS IDENTITY  NOT NULL,
	receiver_system_id BIGINT NOT NULL,
	is_enabled int2 DEFAULT 0 NOT NULL,
	body_json json DEFAULT '{}'::json NOT NULL,
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	id_to_substitute BIGINT NULL,
	description varchar(1024) NULL,
	priority bool DEFAULT false NOT NULL,
	date_from timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	date_to timestamp NULL,
	is_system bool DEFAULT false NOT NULL,
	code_module varchar(128) NULL,
	CONSTRAINT notification_template_contentpk PRIMARY KEY (id),
	CONSTRAINT fk_notification_template_content_notification_template_id FOREIGN KEY (notification_template_id) REFERENCES notification_template(id),
	CONSTRAINT fk_notification_template_content_receiver_system_id FOREIGN KEY (receiver_system_id) REFERENCES notification_receiver_system(id),
	CONSTRAINT fk_notificationtemplatecontent_id_to_substitute FOREIGN KEY (id_to_substitute) REFERENCES notification_template_content(id)
);
COMMENT ON COLUMN notification_template_content.description IS 'Наименование шаблона';
COMMENT ON COLUMN notification_template_content.priority IS 'Признак указания приоритета уведомления';
COMMENT ON COLUMN notification_template_content.date_from IS 'Дата создания шаблона уведомления';
COMMENT ON COLUMN notification_template_content.date_to IS 'Дата удаления шаблона уведомления';
COMMENT ON COLUMN notification_template_content.code_module IS 'Код модуля (из configuration.module.code)';


-- notification_template_content_attachment definition
CREATE TABLE notification_template_content_attachment (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	notification_template_content_id BIGINT NOT NULL,
	storage_file_path varchar NOT NULL,
	file_name varchar(256) NOT NULL,
	file_type varchar(128) NULL,
	is_system bool DEFAULT false NOT NULL,
	CONSTRAINT notification_template_content_attachment_pkey PRIMARY KEY (id),
	CONSTRAINT notification_template_content_notification_template_content_key UNIQUE (notification_template_content_id, storage_file_path),
	CONSTRAINT fk_notification_template_content_attachment_id FOREIGN KEY (notification_template_content_id) REFERENCES notification_template_content(id)
);
COMMENT ON TABLE notification_template_content_attachment IS 'Таблица вложений, относящихся к конкретным шаблонам уведомлений';
COMMENT ON COLUMN notification_template_content_attachment.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN notification_template_content_attachment.notification_template_content_id IS 'Ссылка на идентификатор шаблона уведомления';
COMMENT ON COLUMN notification_template_content_attachment.storage_file_path IS 'Путь к файлу для получения его содержимого из файлового хранилища';
COMMENT ON COLUMN notification_template_content_attachment.file_name IS 'Исходное имя загруженного файла';
COMMENT ON COLUMN notification_template_content_attachment.file_type IS 'Тип файла (MIME Type)';


-- notification_template_content_employee_subscribe definition
CREATE TABLE notification_template_content_employee_subscribe (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	employee_id BIGINT NOT NULL,
	notification_template_content_id BIGINT NOT NULL,
	is_enabled bool DEFAULT true NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	CONSTRAINT notification_template_content_employee_subscribe_pkey PRIMARY KEY (id),
	CONSTRAINT "notification_template_content_id_FK" FOREIGN KEY (notification_template_content_id) REFERENCES notification_template_content(id)
);
COMMENT ON TABLE notification_template_content_employee_subscribe IS 'Подписки сотрудников на Типы уведомлений (включение/отключение)';
COMMENT ON COLUMN notification_template_content_employee_subscribe.id IS 'Синтетический id';
COMMENT ON COLUMN notification_template_content_employee_subscribe.employee_id IS 'ID сотрудника в orgstructure.Employee';
COMMENT ON COLUMN notification_template_content_employee_subscribe.notification_template_content_id IS 'id шаблона уведомления в notification_template_content (FK)';
COMMENT ON COLUMN notification_template_content_employee_subscribe.is_enabled IS 'Включение/Отключение подписки (true - включена / false - отключена)';


-- notification_template_content_event definition
CREATE TABLE notification_template_content_event (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	notification_template_content_id BIGINT NOT NULL,
	date_start timestamp DEFAULT (date_trunc('hour'::text, CURRENT_TIMESTAMP) + ceil(date_part('minute'::text, CURRENT_TIMESTAMP)::numeric / 30::numeric)::double precision * '00:30:00'::interval) NOT NULL,
	date_end timestamp DEFAULT (date_trunc('hour'::text, CURRENT_TIMESTAMP + '00:30:00'::interval) + ceil(date_part('minute'::text, CURRENT_TIMESTAMP + '00:30:00'::interval)::numeric / 30::numeric)::double precision * '00:30:00'::interval) NOT NULL,
	"location" varchar(1024) NULL,
	reminders varchar(128) NULL,
	CONSTRAINT notification_template_content_event_pkey PRIMARY KEY (id),
	CONSTRAINT fk_notification_template_content_id FOREIGN KEY (notification_template_content_id) REFERENCES notification_template_content(id)
);
COMMENT ON TABLE notification_template_content_event IS 'Событие в календарь на основе шаблона уведомления';
COMMENT ON COLUMN notification_template_content_event.id IS 'Синтетический id';
COMMENT ON COLUMN notification_template_content_event.notification_template_content_id IS 'Идентификатор записи таблицы шаблонов уведомлений (notification_template_content.id)';
COMMENT ON COLUMN notification_template_content_event.date_start IS 'Дата и время начала встречи';
COMMENT ON COLUMN notification_template_content_event.date_end IS 'Дата и время окончания встречи';
COMMENT ON COLUMN notification_template_content_event."location" IS 'Место проведения встречи (например, наименование конференц-зала или ссылка на онлайн-платформу)';
COMMENT ON COLUMN notification_template_content_event.reminders IS 'Поле с системными значениями для напоминаний';


-- notification_template_content_recipient definition
CREATE TABLE notification_template_content_recipient (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	notification_recipient_id BIGINT NOT NULL,
	notification_template_content_id BIGINT NOT NULL,
	is_copy bool DEFAULT false NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	CONSTRAINT notification_template_content_recipient_pkey PRIMARY KEY (id),
	CONSTRAINT "notification_recipient_id_FK" FOREIGN KEY (notification_recipient_id) REFERENCES notification_recipient(id),
	CONSTRAINT "notification_template_content_id_FK" FOREIGN KEY (notification_template_content_id) REFERENCES notification_template_content(id)
);
CREATE UNIQUE INDEX uq_ntf_template_content_recipient_tmplt_id_ntf_rec_id_is_copy ON notification_template_content_recipient USING btree (notification_recipient_id, notification_template_content_id, is_copy);
COMMENT ON TABLE notification_template_content_recipient IS '{"tegs":["Уведомления"], description:"данная таблица содержит информацию о связях шаблона уведомления с получателем"}';
COMMENT ON COLUMN notification_template_content_recipient.id IS 'Идентификатор записи';
COMMENT ON COLUMN notification_template_content_recipient.notification_recipient_id IS 'Идентификатор из таблицы вычисляемых получателей уведомлений';
COMMENT ON COLUMN notification_template_content_recipient.notification_template_content_id IS 'Идентификатор из таблицы шаблонов уведомлений';
COMMENT ON COLUMN notification_template_content_recipient.is_copy IS 'Признак того, что данная запись предназначена для указания копии адресата письма true или основного получателя false';


-- notification_log definition
CREATE TABLE notification_log (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	notification_template_content_id BIGINT NOT NULL,
	date_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	status varchar(128) NOT NULL,
	subject varchar(1024) NULL,
	message varchar NULL,
	error_message varchar NULL,
	CONSTRAINT notification_log_pkey PRIMARY KEY (id),
	CONSTRAINT "notification_template_content_id_FK" FOREIGN KEY (notification_template_content_id) REFERENCES notification_template_content(id)
);
COMMENT ON TABLE notification_log IS 'Журнал уведомлений';
COMMENT ON COLUMN notification_log.id IS 'Синтетический id';
COMMENT ON COLUMN notification_log.notification_template_content_id IS 'id шаблона уведомления в notification_template_content (FK)';
COMMENT ON COLUMN notification_log.date_time IS 'Дата и время отправки уведомления';
COMMENT ON COLUMN notification_log.status IS 'Статус отправки уведомления';
COMMENT ON COLUMN notification_log.subject IS 'Тема уведомления';
COMMENT ON COLUMN notification_log.message IS 'Текст уведомления';
COMMENT ON COLUMN notification_log.error_message IS 'Описание ошибки (в случае возникновения)';


-- notification_log_email definition
CREATE TABLE notification_log_email (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	notification_log_id BIGINT NOT NULL,
	email varchar NOT NULL,
	is_copy bool DEFAULT false NOT NULL,
	is_system bool DEFAULT false NOT NULL,
	CONSTRAINT notification_log_email_pkey PRIMARY KEY (id),
	CONSTRAINT fk_notification_log_id FOREIGN KEY (notification_log_id) REFERENCES notification_log(id)
);
CREATE INDEX fk_notification_log_id_idx ON notification_log_email USING btree (notification_log_id);
COMMENT ON TABLE notification_log_email IS 'Журнал уведомлений. Статичный Email получателей уведомления';
COMMENT ON COLUMN notification_log_email.id IS 'Email адрес, на который было отправлено уведомление';
COMMENT ON COLUMN notification_log_email.notification_log_id IS 'ID записи в Журнале уведомлений (notification_log)';
COMMENT ON COLUMN notification_log_email.is_copy IS 'Признак отправки копии (true - копия, false - основной получатель)';
COMMENT ON COLUMN notification_log_email.is_system IS 'Признак системной записи (true - да, false - нет)';


-- notification_log_employee definition
CREATE TABLE notification_log_employee (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	notification_log_id BIGINT NOT NULL,
	employee_id BIGINT NOT NULL,
	is_copy bool DEFAULT false NOT NULL,
	CONSTRAINT notification_log_employee_pkey PRIMARY KEY (id),
	CONSTRAINT "notification_log_id_FK" FOREIGN KEY (notification_log_id) REFERENCES notification_log(id)
);
COMMENT ON TABLE notification_log_employee IS 'Информация о получателях уведомлений в разрезе журнала уведомлений';
COMMENT ON COLUMN notification_log_employee.notification_log_id IS 'id записи Журнала уведомлений в notification_log (FK)';
COMMENT ON COLUMN notification_log_employee.employee_id IS 'id сотрудника в orgstructure.Employee';
COMMENT ON COLUMN notification_log_employee.is_copy IS 'Признак копии уведомления (true - получатель получил копию уведомления, false - получатель получил оригинал уведомления)';


-- notification_deferred_send definition
CREATE TABLE notification_deferred_send (
	notification_log_id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	body_json json DEFAULT '{}'::json NOT NULL,
	date_from timestamp DEFAULT now() NOT NULL,
	CONSTRAINT notification_deferred_send_pk PRIMARY KEY (notification_log_id),
	CONSTRAINT fk_notification_log_id FOREIGN KEY (notification_log_id) REFERENCES notification_log(id)
);
COMMENT ON TABLE notification_deferred_send IS 'Журнал отложенных уведомлений (которые не были отправлены из-за недоступности kafka-messenger)';
COMMENT ON COLUMN notification_deferred_send.notification_log_id IS 'Идентификатор записи в журнале уведомлений (notification_log.id)';
COMMENT ON COLUMN notification_deferred_send.body_json IS 'Дата создания записи';