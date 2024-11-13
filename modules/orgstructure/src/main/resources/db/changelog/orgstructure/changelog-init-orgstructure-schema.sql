--liquibase formatted sql
--changeset podrezov_s:init-orgstructure-schema

--unit
CREATE TABLE unit
(
    code               VARCHAR(128)                            NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                            NOT NULL,
    description        VARCHAR(2048) DEFAULT ''::VARCHAR       NOT NULL,
    update_date        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_system          BOOLEAN       DEFAULT FALSE,
    author_employee_id BIGINT                                  NOT NULL,
    update_employee_id BIGINT                                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE unit IS 'Таблица содержит данные об Юнитах';
COMMENT ON COLUMN unit.code IS 'Уникальный код юнита';
COMMENT ON COLUMN unit.date_from IS 'Дата создания записи';
COMMENT ON COLUMN unit.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN unit."name" IS 'Наименование юнита';
COMMENT ON COLUMN unit.description IS 'Описание юнита';
COMMENT ON COLUMN unit.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN unit.is_system IS 'Признак системности юнита';
COMMENT ON COLUMN unit.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN unit.update_employee_id IS 'ID инициатора обновления записи';
COMMENT ON COLUMN unit.external_id IS 'Внешний идентификатор';

--family_status
CREATE TABLE family_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE family_status IS 'Статусы семейного положения';
COMMENT ON COLUMN family_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN family_status."name" IS 'Наименование';
COMMENT ON COLUMN family_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN family_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN family_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN family_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN family_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN family_status.update_date IS 'Дата обновления записи';

--citizenship
CREATE TABLE citizenship
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(256)                        NOT NULL,
    short_name         VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE citizenship IS 'Гражданство сотрудников, справочник';
COMMENT ON COLUMN citizenship.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN citizenship."name" IS 'Наименование';
COMMENT ON COLUMN citizenship.short_name IS 'Сокращенное наименование';
COMMENT ON COLUMN citizenship.date_from IS 'Системная дата о начале действия записи';
COMMENT ON COLUMN citizenship.date_to IS 'Системная дата об окончании действия записи';
COMMENT ON COLUMN citizenship.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN citizenship.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN citizenship.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN citizenship.update_date IS 'Дата обновления записи';

--person
CREATE TABLE person
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    address            VARCHAR(512),
    date_birth         TIMESTAMP DEFAULT NOW()             NOT NULL,
    email              VARCHAR(64),
    inn                VARCHAR(32),
    surname            VARCHAR(128)                        NOT NULL,
    "name"             VARCHAR(128)                        NOT NULL,
    patronymic         VARCHAR(128),
    phone              VARCHAR(32),
    photo              VARCHAR(256),
    sex                VARCHAR(1)                          NOT NULL,
    snils              VARCHAR(32),
    family_status_id   BIGINT, -- todo update entity
    parent_id          BIGINT,
    spouse_id          BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    citizenship_id     BIGINT,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    telegram           VARCHAR(128),
    city               VARCHAR(64),
    postcode           VARCHAR(64),
    CONSTRAINT "person_spouse_id_fk" FOREIGN KEY (spouse_id) REFERENCES person (id),
    CONSTRAINT "person_family_status_id_fk" FOREIGN KEY (family_status_id) REFERENCES family_status (id),
    CONSTRAINT "person_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES person (id),
    CONSTRAINT "person_citizenship_id_fk" FOREIGN KEY (citizenship_id) REFERENCES citizenship (id)
);
COMMENT ON TABLE person IS 'Каталог физических лиц';
COMMENT ON COLUMN person.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN person.address IS 'Адрес';
COMMENT ON COLUMN person.date_birth IS 'Дата рождения';
COMMENT ON COLUMN person.email IS 'Указание e-mail';
COMMENT ON COLUMN person.inn IS 'Номер ИНН';
COMMENT ON COLUMN person.surname IS 'Фамилия';
COMMENT ON COLUMN person."name" IS 'Имя';
COMMENT ON COLUMN person.patronymic IS 'Отчество';
COMMENT ON COLUMN person.phone IS 'Указание телефона';
COMMENT ON COLUMN person.photo IS 'Ссылка на фото работника';
COMMENT ON COLUMN person.sex IS 'Пол работника';
COMMENT ON COLUMN person.snils IS 'Номер СНИЛС';
COMMENT ON COLUMN person.family_status_id IS 'Идентификатор семейного положения';
COMMENT ON COLUMN person.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN person.spouse_id IS 'Идентификатор супруга работника';
COMMENT ON COLUMN person.external_id IS 'Внешний код записи';
COMMENT ON COLUMN person.citizenship_id IS 'Гражданство';
COMMENT ON COLUMN person.city IS 'Город проживания';
COMMENT ON COLUMN person.postcode IS 'Индекс';
CREATE INDEX person_full_name_idx ON person (surname, "name", patronymic);
CREATE INDEX person_spouse_id_idx ON person (spouse_id);
CREATE INDEX person_family_status_id_idx ON person (family_status_id);
CREATE INDEX person_parent_id_idx ON person (parent_id);
CREATE INDEX person_citizenship_id_idx ON person (citizenship_id);

--employee
CREATE TABLE employee
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    email              VARCHAR(64),
    fax                VARCHAR(64),
    is_freelancer      BOOLEAN   DEFAULT FALSE             NOT NULL, -- TODO update entity
    is_has_mobile      BOOLEAN   DEFAULT FALSE             NOT NULL, -- TODO update entity
    number             VARCHAR(64)                         NOT NULL,
    phone              VARCHAR(64),
    person_id          BIGINT                              NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    telegram           VARCHAR(128),
    CONSTRAINT "employee_person_id_fk" FOREIGN KEY (person_id) REFERENCES person (id)
);
COMMENT ON TABLE employee IS 'Каталог работников';
COMMENT ON COLUMN employee.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN employee.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN employee.email IS 'Указание e-mail';
COMMENT ON COLUMN employee.fax IS 'Указание факса';
COMMENT ON COLUMN employee.is_freelancer IS 'Признак фрилансера';
COMMENT ON COLUMN employee.is_has_mobile IS 'Признак мобильности работника';
COMMENT ON COLUMN employee.number IS 'Табельный номер работника';
COMMENT ON COLUMN employee.phone IS 'Указание телефона';
COMMENT ON COLUMN employee.person_id IS 'Ссылка на идентификатор person, физические лица';
COMMENT ON COLUMN employee.external_id IS 'Внешний код записи';
COMMENT ON COLUMN employee.telegram IS 'Телеграм сотрудника';
CREATE INDEX employee_person_id_idx ON employee (person_id);

--position_rank
CREATE TABLE position_rank
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "position_rank_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_rank IS 'Уровни должностей';
COMMENT ON COLUMN position_rank.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_rank.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN position_rank.full_name IS 'Полное наименование';
COMMENT ON COLUMN position_rank.short_name IS 'Краткое наименование';
COMMENT ON COLUMN position_rank.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN position_rank.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN position_rank.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN position_rank.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position_rank.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position_rank.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position_rank.unit_code IS 'Код юнита';
CREATE INDEX position_rank_unit_code_idx ON position_rank (unit_code);

--function_status
CREATE TABLE function_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT function_status_name_unit_code_key UNIQUE (name, unit_code),
    CONSTRAINT function_status_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE function_status IS 'Статусы функций';
COMMENT ON COLUMN function_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN function_status."name" IS 'Наименование';
COMMENT ON COLUMN function_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN function_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN function_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN function_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN function_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN function_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN function_status.unit_code IS 'Код юнита';
CREATE INDEX function_status_unit_code_idx ON function_status (unit_code);

--legal_entity_status
CREATE TABLE legal_entity_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE legal_entity_status IS 'Статусы юридических лиц';
COMMENT ON COLUMN legal_entity_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN legal_entity_status."name" IS 'Наименование';
COMMENT ON COLUMN legal_entity_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN legal_entity_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN legal_entity_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN legal_entity_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN legal_entity_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN legal_entity_status.update_date IS 'Дата обновления записи';

--legal_entity_type
CREATE TABLE legal_entity_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE legal_entity_type IS 'Типы юридических лиц';
COMMENT ON COLUMN legal_entity_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN legal_entity_type."name" IS 'Наименование';
COMMENT ON COLUMN legal_entity_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN legal_entity_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN legal_entity_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN legal_entity_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN legal_entity_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN legal_entity_type.update_date IS 'Дата обновления записи';

--management_structure_type
CREATE TABLE management_structure_type
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    "name"                VARCHAR(256)                        NOT NULL,
    description           VARCHAR(2048),
    is_official_legal     BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_unit_authorizable  BOOLEAN   DEFAULT FALSE             NOT NULL,
    unit_code             VARCHAR(128)                        NOT NULL DEFAULT 'default',
    external_id           VARCHAR(128) UNIQUE,
    is_system             BOOLEAN   DEFAULT FALSE             NOT NULL,
    is_editable_if_system BOOLEAN   DEFAULT FALSE             NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    code                  VARCHAR(128)                        NOT NULL,
    CONSTRAINT management_structure_type_name_unit_code_key UNIQUE (code, unit_code),
    CONSTRAINT "management_structure_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE management_structure_type IS 'Данные о типах управленческих структур';
COMMENT ON COLUMN management_structure_type.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN management_structure_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN management_structure_type.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN management_structure_type."name" IS 'Наименование типа структуры';
COMMENT ON COLUMN management_structure_type.description IS 'Описание типа структуры';
COMMENT ON COLUMN management_structure_type.is_official_legal IS 'Указатель на реальное юр.лицо';
COMMENT ON COLUMN management_structure_type.is_unit_authorizable IS 'Признак, указывающий на то, доступен ли юнит, который входит в данный легал для авторизации пользователя';
COMMENT ON COLUMN management_structure_type.unit_code IS 'Код юнита';
COMMENT ON COLUMN management_structure_type.external_id IS 'Внешний идентификатор';
COMMENT ON COLUMN management_structure_type.is_system IS 'Флаг системности';
COMMENT ON COLUMN management_structure_type.is_editable_if_system IS 'Флаг доступности для редактирования';
COMMENT ON COLUMN management_structure_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN management_structure_type.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN management_structure_type.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN management_structure_type.code IS 'Код типа структуры';
CREATE INDEX management_structure_type_unit_code_idx ON management_structure_type (unit_code);

--legal_entity
CREATE TABLE legal_entity
(
    id                           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation                 VARCHAR(128)                        NOT NULL,
    date_from                    TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                      TIMESTAMP,
    full_name                    VARCHAR(512)                        NOT NULL,
    is_affiliate                 BOOLEAN   DEFAULT FALSE             NOT NULL, -- TODO update entity
    short_name                   VARCHAR(256)                        NOT NULL,
    parent_id                    BIGINT,
    precursor_id                 BIGINT,
    status_id                    BIGINT,                                       -- todo update entity
    type_id                      BIGINT,                                       -- todo update entity
    external_id                  VARCHAR(128) UNIQUE,
    cost_center_id               BIGINT,
    update_date                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id           BIGINT                              NOT NULL,
    update_employee_id           BIGINT                              NOT NULL,
    unit_code                    VARCHAR(128)                        NOT NULL DEFAULT 'default',
    management_structure_type_id BIGINT                              NOT NULL,
    CONSTRAINT "legal_entity_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES legal_entity (id),
    CONSTRAINT "legal_entity_status_id_fk" FOREIGN KEY (status_id) REFERENCES legal_entity_status (id),
    CONSTRAINT "legal_entity_type_id_fk" FOREIGN KEY (type_id) REFERENCES legal_entity_type (id),
    CONSTRAINT "legal_entity_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES legal_entity (id),
    CONSTRAINT "legal_entity_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code),
    CONSTRAINT "legal_entity_management_structure_type_id_fk" FOREIGN KEY (management_structure_type_id) REFERENCES management_structure_type (id)
);
COMMENT ON TABLE legal_entity IS 'Каталог юридических лиц';
COMMENT ON COLUMN legal_entity.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN legal_entity.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN legal_entity.date_from IS 'Дата создания записи';
COMMENT ON COLUMN legal_entity.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN legal_entity.full_name IS 'Полное наименование';
COMMENT ON COLUMN legal_entity.is_affiliate IS 'Признак филиала';
COMMENT ON COLUMN legal_entity.short_name IS 'Краткое наименование';
COMMENT ON COLUMN legal_entity.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN legal_entity.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN legal_entity.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN legal_entity.type_id IS 'Идентификатор типа ЮЛ';
COMMENT ON COLUMN legal_entity.external_id IS 'Внешний код записи';
COMMENT ON COLUMN legal_entity.cost_center_id IS 'Ссылка на идентификатор cost_center, центр затрат';
COMMENT ON COLUMN legal_entity.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN legal_entity.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN legal_entity.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN legal_entity.unit_code IS 'Код юнита';
COMMENT ON COLUMN legal_entity.management_structure_type_id IS 'Идентификатор типа управленческой должности';
CREATE INDEX legal_entity_parent_id_idx ON legal_entity (parent_id);
CREATE INDEX legal_entity_status_id_idx ON legal_entity (status_id);
CREATE INDEX legal_entity_type_id_idx ON legal_entity (type_id);
CREATE INDEX legal_entity_precursor_id_idx ON legal_entity (precursor_id);
CREATE INDEX legal_entity_unit_code_idx ON legal_entity (unit_code);
CREATE INDEX legal_entity_management_structure_type_id_idx ON legal_entity (management_structure_type_id);

--function
CREATE TABLE function
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    parent_id          BIGINT,
    precursor_id       BIGINT,
    status_id          BIGINT, -- todo update entity
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description        VARCHAR(2048),
    legal_entity_id    BIGINT, -- TODO update entity
    CONSTRAINT "function_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES function (id),
    CONSTRAINT "function_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES function (id),
    CONSTRAINT "function_status_id_fk" FOREIGN KEY (status_id) REFERENCES function_status (id),
    CONSTRAINT "function_legal_entity_id_fk" FOREIGN KEY (legal_entity_id) REFERENCES legal_entity (id)
);
COMMENT ON TABLE function IS 'Функции';
COMMENT ON COLUMN function.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN function.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN function.date_from IS 'Дата создания записи';
COMMENT ON COLUMN function.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN function.full_name IS 'Полное наименование';
COMMENT ON COLUMN function.short_name IS 'Краткое наименование';
COMMENT ON COLUMN function.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN function.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN function.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN function.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN function.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN function.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN function.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN function.description IS 'Описание функции';
COMMENT ON COLUMN function.legal_entity_id IS 'Ссылается на ID в таблице legal_entity';
CREATE INDEX function_precursor_id_idx ON function (precursor_id);
CREATE INDEX function_parent_id_idx ON function (parent_id);
CREATE INDEX function_status_id_idx ON function (status_id);
CREATE INDEX function_legal_entity_id_idx ON function (legal_entity_id);

--team_status
CREATE TABLE team_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT team_status_name_unit_code_key UNIQUE ("name", unit_code),
    CONSTRAINT "team_status_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE team_status IS 'Статусы команд';
COMMENT ON COLUMN team_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN team_status."name" IS 'Наименование';
COMMENT ON COLUMN team_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN team_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN team_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN team_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN team_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN team_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN team_status.unit_code IS 'Код юнита';
CREATE INDEX team_status_unit_code_idx ON team_status (unit_code);

--team_type
CREATE TABLE team_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT team_type_name_unit_code_key UNIQUE (name, unit_code),
    CONSTRAINT team_type_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE team_type IS 'Типы команд';
COMMENT ON COLUMN team_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN team_type."name" IS 'Наименование';
COMMENT ON COLUMN team_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN team_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN team_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN team_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN team_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN team_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN team_type.unit_code IS 'Код юнита';
CREATE INDEX team_type_unit_code_idx ON team_type (unit_code);

--function_team
CREATE TABLE function_team
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    function_id        BIGINT,
    parent_id          BIGINT,
    precursor_id       BIGINT,
    status_id          BIGINT, -- todo update entity
    type_id            BIGINT, -- todo update entity
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description        VARCHAR(2048),
    CONSTRAINT "function_team_function_id_fk" FOREIGN KEY (function_id) REFERENCES function (id),
    CONSTRAINT "function_team_status_id_fk" FOREIGN KEY (status_id) REFERENCES team_status (id),
    CONSTRAINT "function_team_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES function_team (id),
    CONSTRAINT "function_team_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES function_team (id),
    CONSTRAINT "function_team_type_id_fk" FOREIGN KEY (type_id) REFERENCES team_type (id)
);
COMMENT ON TABLE function_team IS 'Каталог функциональных команд';
COMMENT ON COLUMN function_team.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN function_team.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN function_team.date_from IS 'Дата создания записи';
COMMENT ON COLUMN function_team.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN function_team.full_name IS 'Полное наименование';
COMMENT ON COLUMN function_team.short_name IS 'Краткое наименование';
COMMENT ON COLUMN function_team.function_id IS 'Идентификатор функции';
COMMENT ON COLUMN function_team.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN function_team.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN function_team.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN function_team.type_id IS 'Идентификатор типа функциональной команды';
COMMENT ON COLUMN function_team.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN function_team.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN function_team.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN function_team.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN function_team.description IS 'Описание функциональной команды';
CREATE INDEX function_team_function_id_idx ON function_team (function_id);
CREATE INDEX function_team_status_id_idx ON function_team (status_id);
CREATE INDEX function_team_parent_id_idx ON function_team (parent_id);
CREATE INDEX function_team_precursor_id_idx ON function_team (precursor_id);
CREATE INDEX function_team_type_id_idx ON function_team (type_id);

--work_function_status
CREATE TABLE work_function_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT work_function_status_name_unit_code_key UNIQUE ("name", unit_code),
    CONSTRAINT work_function_status_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE work_function_status IS 'Статусы рабочих функций';
COMMENT ON COLUMN work_function_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_function_status."name" IS 'Наименование';
COMMENT ON COLUMN work_function_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN work_function_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN work_function_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN work_function_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN work_function_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN work_function_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN work_function_status.unit_code IS 'Код юнита';
CREATE INDEX work_function_status_unit_code_idx ON work_function_status (unit_code);

--work_function
CREATE TABLE work_function
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation            VARCHAR(128)                        NOT NULL,
    date_from               TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                 TIMESTAMP,
    full_name               VARCHAR(512)                        NOT NULL,
    is_required_certificate BOOLEAN   DEFAULT FALSE             NOT NULL, -- todo update entity
    short_name              VARCHAR(256)                        NOT NULL,
    function_id             BIGINT,
    precursor_id            BIGINT,
    status_id               BIGINT,                                       -- todo update entity
    external_id             VARCHAR(128) UNIQUE,
    author_employee_id      BIGINT                              NOT NULL,
    update_employee_id      BIGINT                              NOT NULL,
    update_date             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code               VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "work_function_function_id_fk" FOREIGN KEY (function_id) REFERENCES function (id),
    CONSTRAINT "work_function_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES work_function (id),
    CONSTRAINT "work_function_status_id_fk" FOREIGN KEY (status_id) REFERENCES work_function_status (id),
    CONSTRAINT "work_function_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE work_function IS 'Типы рабочих функций';
COMMENT ON COLUMN work_function.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_function.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN work_function.date_from IS 'Дата создания записи';
COMMENT ON COLUMN work_function.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN work_function.full_name IS 'Полное наименование';
COMMENT ON COLUMN work_function.is_required_certificate IS 'Признак необходимости наличия сертификата';
COMMENT ON COLUMN work_function.short_name IS 'Краткое наименование';
COMMENT ON COLUMN work_function.function_id IS 'Идентификатор функции';
COMMENT ON COLUMN work_function.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN work_function.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN work_function.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN work_function.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN work_function.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN work_function.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN work_function.unit_code IS 'Код юнита';
CREATE INDEX work_function_function_id_idx ON work_function (function_id);
CREATE INDEX work_function_precursor_id_idx ON work_function (precursor_id);
CREATE INDEX work_function_status_id_idx ON work_function (status_id);
CREATE INDEX work_function_unit_code_idx ON work_function (unit_code);

--position_type
CREATE TABLE position_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(256)                        NOT NULL,
    description        VARCHAR(256),
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "position_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_type IS 'Типы должностей';
COMMENT ON COLUMN position_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_type."name" IS 'Наименование';
COMMENT ON COLUMN position_type.description IS 'Описание';
COMMENT ON COLUMN position_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN position_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN position_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN position_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position_type.unit_code IS 'Код юнита';
CREATE INDEX position_type_unit_code_idx ON position_type (unit_code);

--location_group
CREATE TABLE location_group
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(54)                         NOT NULL,
    parent_id          BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "location_group_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES location_group (id)
);
COMMENT ON TABLE location_group IS 'Каталог групп адресов';
COMMENT ON COLUMN location_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN location_group."name" IS 'Наименование';
COMMENT ON COLUMN location_group.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN location_group.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN location_group.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN location_group.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN location_group.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN location_group.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN location_group.update_date IS 'Дата обновления записи';
CREATE INDEX location_group_parent_id_idx ON location_group (parent_id);

--location
CREATE TABLE location
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    area               VARCHAR(64),
    building           VARCHAR(8),
    city               VARCHAR(64),
    country            VARCHAR(128)                        NOT NULL,
    district           VARCHAR(64),
    housing            VARCHAR(8),
    index              VARCHAR(16)                         NOT NULL,
    latitude           real                                NOT NULL,
    longitude          real                                NOT NULL,
    number             VARCHAR(8)                          NOT NULL,
    region             VARCHAR(64)                         NOT NULL,
    street             VARCHAR(128)                        NOT NULL,
    group_id           BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "location_group_id_fk" FOREIGN KEY (group_id) REFERENCES location_group (id),
    CONSTRAINT "location_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE location IS 'Каталог адресов';
COMMENT ON COLUMN location.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN location.area IS 'Область';
COMMENT ON COLUMN location.building IS 'Здание';
COMMENT ON COLUMN location.city IS 'Город';
COMMENT ON COLUMN location.country IS 'Страна';
COMMENT ON COLUMN location.district IS 'Район';
COMMENT ON COLUMN location.housing IS 'Признак жилого помещения';
COMMENT ON COLUMN location.index IS 'Правила сортировки при выводе списка значений на экран или в печатную форму';
COMMENT ON COLUMN location.latitude IS 'Широта';
COMMENT ON COLUMN location.longitude IS 'Долгота';
COMMENT ON COLUMN location.number IS 'Номер';
COMMENT ON COLUMN location.region IS 'Регион';
COMMENT ON COLUMN location.street IS 'Улица';
COMMENT ON COLUMN location.group_id IS 'Идентификатор группы';
COMMENT ON COLUMN location.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN location.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN location.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN location.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN location.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN location.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN location.unit_code IS 'Код юнита';
CREATE INDEX location_group_id_idx ON location (group_id);
CREATE INDEX location_unit_code_idx ON location (unit_code);

--workplace
CREATE TABLE workplace
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code               VARCHAR(32)                         NOT NULL,
    hash               VARCHAR(64)                         NOT NULL,
    is_base            BOOLEAN   DEFAULT FALSE             NOT NULL, -- TODO update entity
    number             BIGINT                              NOT NULL, -- todo update entity
    location_id        BIGINT,
    precursor_id       BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "workplace_location_id_fk" FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT "workplace_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES workplace (id),
    CONSTRAINT "workplace_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE workplace IS 'Каталог рабочих мест';
COMMENT ON COLUMN workplace.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN workplace.code IS 'Системный код записи';
COMMENT ON COLUMN workplace.hash IS 'Хэш';
COMMENT ON COLUMN workplace.is_base IS 'Признак базового рабочего места';
COMMENT ON COLUMN workplace.number IS 'Номер рабочего места';
COMMENT ON COLUMN workplace.location_id IS 'Ссылка на идентификатор location, расположение';
COMMENT ON COLUMN workplace.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN workplace.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN workplace.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN workplace.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN workplace.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN workplace.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN workplace.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN workplace.unit_code IS 'Код юнита';
CREATE INDEX workplace_location_id_idx ON workplace (location_id);
CREATE INDEX workplace_precursor_id_idx ON workplace (precursor_id);
CREATE INDEX workplace_unit_code_idx ON workplace (unit_code);

--position_category
CREATE TABLE position_category
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "position_category_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_category IS 'Категории должностей';
COMMENT ON COLUMN position_category.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_category.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN position_category.full_name IS 'Полное наименование';
COMMENT ON COLUMN position_category.short_name IS 'Краткое наименование';
COMMENT ON COLUMN position_category.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN position_category.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN position_category.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN position_category.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position_category.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position_category.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position_category.unit_code IS 'Код юнита';
CREATE INDEX position_category_unit_code_idx ON position_category (unit_code);

--structure_type
CREATE TABLE structure_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE structure_type IS 'Типы структур';
COMMENT ON COLUMN structure_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN structure_type."name" IS 'Наименование';
COMMENT ON COLUMN structure_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN structure_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN structure_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN structure_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN structure_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN structure_type.update_date IS 'Дата обновления записи';

--structure_status
CREATE TABLE structure_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE structure_status IS 'Статусы структур';
COMMENT ON COLUMN structure_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN structure_status."name" IS 'Наименование';
COMMENT ON COLUMN structure_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN structure_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN structure_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN structure_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN structure_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN structure_status.update_date IS 'Дата обновления записи';

--structure
CREATE TABLE structure
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    location_id        BIGINT,
    parent_id          BIGINT,
    precursor_id       BIGINT,
    status_id          BIGINT, -- todo update entity
    type_id            BIGINT, -- todo update entity
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "structure_type_id_fk" FOREIGN KEY (type_id) REFERENCES structure_type (id),
    CONSTRAINT "structure_status_id_fk" FOREIGN KEY (status_id) REFERENCES structure_status (id),
    CONSTRAINT "structure_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES structure (id),
    CONSTRAINT "structure_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES structure (id),
    CONSTRAINT "structure_location_id_fk" FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT "structure_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE structure IS 'Каталог территориальных структур';
COMMENT ON COLUMN structure.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN structure.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN structure.date_from IS 'Дата создания записи';
COMMENT ON COLUMN structure.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN structure.full_name IS 'Полное наименование';
COMMENT ON COLUMN structure.short_name IS 'Краткое наименование';
COMMENT ON COLUMN structure.location_id IS 'Ссылка на идентификатор location, расположение';
COMMENT ON COLUMN structure.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN structure.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN structure.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN structure.type_id IS 'Идентификатор типа территориальной структуры';
COMMENT ON COLUMN structure.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN structure.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN structure.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN structure.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN structure.unit_code IS 'Код юнита';
CREATE INDEX structure_type_id_idx ON structure (type_id);
CREATE INDEX structure_status_id_idx ON structure (status_id);
CREATE INDEX structure_parent_id_idx ON structure (parent_id);
CREATE INDEX structure_precursor_id_idx ON structure (precursor_id);
CREATE INDEX structure_location_id_idx ON structure (location_id);
CREATE INDEX structure_unit_code_idx ON structure (unit_code);

--division_status
CREATE TABLE division_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE division_status IS 'Статусы подразделений';
COMMENT ON COLUMN division_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_status."name" IS 'Наименование';
COMMENT ON COLUMN division_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN division_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN division_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN division_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN division_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN division_status.update_date IS 'Дата обновления записи';

--division_group
CREATE TABLE division_group
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE division_group IS 'Группы подразделений';
COMMENT ON COLUMN division_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_group."name" IS 'Наименование';
COMMENT ON COLUMN division_group.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN division_group.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN division_group.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN division_group.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN division_group.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN division_group.update_date IS 'Дата обновления записи';

--cost_center
CREATE TABLE cost_center
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    code               VARCHAR(128),
    full_name          VARCHAR(256)                        NOT NULL,
    short_name         VARCHAR(128)                        NOT NULL,
    abbreviation       VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "cost_center_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON COLUMN cost_center.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN cost_center.date_from IS 'Дата создания записи';
COMMENT ON COLUMN cost_center.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN cost_center.code IS 'Системный код записи';
COMMENT ON COLUMN cost_center.full_name IS 'Полное наименование';
COMMENT ON COLUMN cost_center.short_name IS 'Краткое наименование';
COMMENT ON COLUMN cost_center.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN cost_center.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN cost_center.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN cost_center.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN cost_center.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN cost_center.unit_code IS 'Код юнита';
CREATE INDEX cost_center_unit_code_idx ON cost_center (unit_code);

--division_kind
CREATE TABLE division_kind
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to               TIMESTAMP,
    "name"                VARCHAR(256)                        NOT NULL,
    description           VARCHAR(2048),
    unit_code             VARCHAR(128)                        NOT NULL DEFAULT 'default',
    external_id           VARCHAR(128) UNIQUE,
    is_system             boolean   DEFAULT FALSE             NOT NULL,
    is_editable_if_system boolean   DEFAULT FALSE             NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    CONSTRAINT "division_kind_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE division_kind IS 'Данные о видах разделения';
COMMENT ON COLUMN division_kind.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN division_kind.date_from IS 'Дата создания записи';
COMMENT ON COLUMN division_kind.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN division_kind."name" IS 'Наименование вида разделения';
COMMENT ON COLUMN division_kind.description IS 'Описание вида разделения';
COMMENT ON COLUMN division_kind.unit_code IS 'Код юнита';
COMMENT ON COLUMN division_kind.external_id IS 'Внешний идентификатор';
COMMENT ON COLUMN division_kind.is_system IS 'Флаг системности';
COMMENT ON COLUMN division_kind.is_editable_if_system IS 'Флаг доступности для редактирования';
COMMENT ON COLUMN division_kind.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN division_kind.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN division_kind.update_employee_id IS 'Автор последнего обновления записи';
CREATE INDEX division_kind_unit_code_idx ON division_kind (unit_code);

--division
CREATE TABLE division
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    group_id           BIGINT,
    legal_entity_id    BIGINT                              NOT NULL,
    parent_id          BIGINT,
    precursor_id       BIGINT,
    status_id          BIGINT, -- todo update entity
    structure_id       BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    cost_center_id     BIGINT,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    description        VARCHAR(2048),
    kind_id            BIGINT,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    date_start_confirm TIMESTAMP,
    date_end_confirm   TIMESTAMP,
    code               VARCHAR(128),
    CONSTRAINT "division_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES division (id),
    CONSTRAINT "division_structure_id_fk" FOREIGN KEY (structure_id) REFERENCES structure (id),
    CONSTRAINT "division_legal_entity_id_fk" FOREIGN KEY (legal_entity_id) REFERENCES legal_entity (id),
    CONSTRAINT "division_status_id_fk" FOREIGN KEY (status_id) REFERENCES division_status (id),
    CONSTRAINT "division_group_id_fk" FOREIGN KEY (group_id) REFERENCES division_group (id),
    CONSTRAINT "division_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES division (id),
    CONSTRAINT "division_cost_center_id_fk" FOREIGN KEY (cost_center_id) REFERENCES cost_center (id),
    CONSTRAINT "division_kind_id_fk" FOREIGN KEY (kind_id) REFERENCES division_kind (id)
);
COMMENT ON TABLE division IS 'Каталог подразделений';
COMMENT ON COLUMN division.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN division.date_from IS 'Дата создания записи';
COMMENT ON COLUMN division.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN division.full_name IS 'Полное наименование';
COMMENT ON COLUMN division.short_name IS 'Краткое наименование';
COMMENT ON COLUMN division.group_id IS 'Ссылка на идентификатор группировки';
COMMENT ON COLUMN division.legal_entity_id IS 'Ссылка на идентификатор legal_entity, каталог юридических лиц';
COMMENT ON COLUMN division.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN division.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN division.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN division.structure_id IS 'Ссылка на идентификатор территориальной структуры';
COMMENT ON COLUMN division.external_id IS 'Внешний код записи';
COMMENT ON COLUMN division.cost_center_id IS 'Ссылка на идентификатор cost_center, центр затрат';
COMMENT ON COLUMN division.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN division.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN division.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN division.description IS 'Описание подразделения';
COMMENT ON COLUMN division.kind_id IS 'Идентификатор вида разделения';
COMMENT ON COLUMN division.date_start IS 'Дата запуска';
COMMENT ON COLUMN division.date_end IS 'Дата завершения';
COMMENT ON COLUMN division.date_start_confirm IS 'Подтверждённая дата старта';
COMMENT ON COLUMN division.date_end_confirm IS 'Подтверждённая дата окончания';
COMMENT ON COLUMN division.code IS 'Системный код записи';
CREATE INDEX division_precursor_id_idx ON division (precursor_id);
CREATE INDEX division_structure_id_idx ON division (structure_id);
CREATE INDEX division_legal_entity_id_idx ON division (legal_entity_id);
CREATE INDEX division_status_id_idx ON division (status_id);
CREATE INDEX division_group_id_idx ON division (group_id);
CREATE INDEX division_parent_id_idx ON division (parent_id);
CREATE INDEX division_cost_center_id_idx ON division (cost_center_id);
CREATE INDEX division_kind_id_idx ON division (kind_id);

--position_status
CREATE TABLE position_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "position_status_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_status IS 'Статусы должностей';
COMMENT ON COLUMN position_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_status.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN position_status.full_name IS 'Полное наименование';
COMMENT ON COLUMN position_status.short_name IS 'Краткое наименование';
COMMENT ON COLUMN position_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN position_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN position_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN position_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position_status.unit_code IS 'Код юнита';
CREATE INDEX position_status_unit_code_idx ON position_status (unit_code);

--job_title_cluster
CREATE TABLE job_title_cluster
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE job_title_cluster IS 'Группировка эталонных должностей';
COMMENT ON COLUMN job_title_cluster.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN job_title_cluster."name" IS 'Наименование';
COMMENT ON COLUMN job_title_cluster.external_id IS 'Внешний код записи';
COMMENT ON COLUMN job_title_cluster.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN job_title_cluster.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN job_title_cluster.update_date IS 'Дата обновления записи';

--job_title
CREATE TABLE job_title
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation            VARCHAR(128)                        NOT NULL,
    code                    VARCHAR(32)                         NOT NULL,
    date_from               TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                 TIMESTAMP,
    full_name               VARCHAR(512)                        NOT NULL,
    hash                    VARCHAR(64)                         NOT NULL,
    is_required_certificate BOOLEAN   DEFAULT FALSE             NOT NULL, -- TODO update entity
    short_name              VARCHAR(256)                        NOT NULL,
    cluster_id              BIGINT,
    precursor_id            BIGINT,
    external_id             VARCHAR(128) UNIQUE,
    author_employee_id      BIGINT                              NOT NULL,
    update_employee_id      BIGINT                              NOT NULL,
    update_date             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code               VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "job_title_cluster_id_fk" FOREIGN KEY (cluster_id) REFERENCES job_title_cluster (id),
    CONSTRAINT "job_title_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES job_title (id),
    CONSTRAINT "job_title_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE job_title IS 'Эталонные должности (ОКПДТР)';
COMMENT ON COLUMN job_title.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN job_title.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN job_title.code IS 'Системный код записи';
COMMENT ON COLUMN job_title.date_from IS 'Дата создания записи';
COMMENT ON COLUMN job_title.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN job_title.full_name IS 'Полное наименование';
COMMENT ON COLUMN job_title.hash IS 'Хэш';
COMMENT ON COLUMN job_title.is_required_certificate IS 'Признак необходимости наличия сертификата для данной эталонной должности';
COMMENT ON COLUMN job_title.short_name IS 'Краткое наименование';
COMMENT ON COLUMN job_title.cluster_id IS 'Идентификатор группировки эталонных должностей';
COMMENT ON COLUMN job_title.precursor_id IS 'Идентификатор предыдущей записи, с которой происходит наследование данных';
COMMENT ON COLUMN job_title.external_id IS 'Внешний код записи';
COMMENT ON COLUMN job_title.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN job_title.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN job_title.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN job_title.unit_code IS 'Код юнита';
CREATE INDEX job_title_cluster_id_idx ON job_title (cluster_id);
CREATE INDEX job_title_precursor_id_idx ON job_title (precursor_id);
CREATE INDEX job_title_unit_code_idx ON job_title (unit_code);

--position_importance
CREATE TABLE position_importance
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY    NOT NULL PRIMARY KEY, -- TODO update entity
    "name"              VARCHAR(128)                           NOT NULL,
    successor_count_max BIGINT       DEFAULT 3                 NOT NULL,
    successor_count_rec BIGINT       DEFAULT 2                 NOT NULL,
    description         VARCHAR(256) DEFAULT ''::VARCHAR       NOT NULL,
    "index"             BIGINT       DEFAULT 0                 NOT NULL,
    external_id         VARCHAR(128) UNIQUE,
    date_from           TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to             TIMESTAMP,
    author_employee_id  BIGINT                                 NOT NULL,
    update_employee_id  BIGINT                                 NOT NULL,
    update_date         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code           VARCHAR(128)                           NOT NULL DEFAULT 'default',
    CONSTRAINT "position_importance_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_importance IS 'Критичности позиции';
COMMENT ON COLUMN position_importance.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_importance."name" IS 'Название уровня критичности';
COMMENT ON COLUMN position_importance.successor_count_max IS 'Максимальное кол-во преемников';
COMMENT ON COLUMN position_importance.successor_count_rec IS 'Рекомендованное кол-во преемников';
COMMENT ON COLUMN position_importance.description IS 'Описание';
COMMENT ON COLUMN position_importance."index" IS 'Правила сортировки при выводе списка значений на экран или в печатную форму';
COMMENT ON COLUMN position_importance.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN position_importance.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN position_importance.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN position_importance.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position_importance.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position_importance.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position_importance.unit_code IS 'Код юнита';
CREATE INDEX position_importance_unit_code_idx ON position_importance (unit_code);

--position
CREATE TABLE position
(
    id                              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation                    VARCHAR(128)                        NOT NULL,
    date_from                       TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                         TIMESTAMP,
    full_name                       VARCHAR(512)                        NOT NULL,
    is_key                          BOOLEAN   DEFAULT FALSE             NOT NULL, --TODO update entity
    is_variable                     BOOLEAN   DEFAULT FALSE             NOT NULL, --TODO update entity
    short_name                      VARCHAR(256)                        NOT NULL,
    stake                           real                                NOT NULL,
    category_id                     BIGINT,
    division_id                     BIGINT                              NOT NULL,
    job_title_id                    BIGINT,
    precursor_id                    BIGINT,
    rank_id                         BIGINT,
    status_id                       BIGINT,                                       -- TODO update entity
    work_function_id                BIGINT,
    workplace_id                    BIGINT,
    external_id                     VARCHAR(128) UNIQUE,
    position_type_id                BIGINT,
    position_importance_id          BIGINT,
    is_key_management               BOOLEAN   DEFAULT FALSE             NOT NULL, -- todo update entity
    profstandard_work_function_code VARCHAR(16),
    cost_center_id                  BIGINT,
    profstandard_code               VARCHAR(16),
    update_date                     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id              BIGINT                              NOT NULL,
    update_employee_id              BIGINT                              NOT NULL,
    is_head                         boolean   DEFAULT FALSE             NOT NULL,
    CONSTRAINT "position_rank_id_fk" FOREIGN KEY (rank_id) REFERENCES position_rank (id),
    CONSTRAINT "position_work_function_id_fk" FOREIGN KEY (work_function_id) REFERENCES work_function (id),
    CONSTRAINT "position_position_type_id_fk" FOREIGN KEY (position_type_id) REFERENCES position_type (id),
    CONSTRAINT "position_workplace_id_fk" FOREIGN KEY (workplace_id) REFERENCES workplace (id),
    CONSTRAINT "position_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES position (id),
    CONSTRAINT "position_category_id_fk" FOREIGN KEY (category_id) REFERENCES position_category (id),
    CONSTRAINT "position_division_id_fk" FOREIGN KEY (division_id) REFERENCES division (id),
    CONSTRAINT "position_status_id_fk" FOREIGN KEY (status_id) REFERENCES position_status (id),
    CONSTRAINT "position_job_title_id_fk" FOREIGN KEY (job_title_id) REFERENCES job_title (id),
    CONSTRAINT "position_position_importance_id_fk" FOREIGN KEY (position_importance_id) REFERENCES position_importance (id),
    CONSTRAINT "position_cost_center_id_fk" FOREIGN KEY (cost_center_id) REFERENCES cost_center (id)
);
COMMENT ON COLUMN position.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN position.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN position.full_name IS 'Полное наименование';
COMMENT ON COLUMN position.is_key IS 'Признак ключевой позиции';
COMMENT ON COLUMN position.is_variable IS 'Признак вариативности должности';
COMMENT ON COLUMN position.short_name IS 'Краткое наименование';
COMMENT ON COLUMN position.stake IS 'Доля занятости должности';
COMMENT ON COLUMN position.category_id IS 'Идентификатор категории должности';
COMMENT ON COLUMN position.division_id IS 'Ссылка на идентификатор division, подразделение';
COMMENT ON COLUMN position.job_title_id IS 'Идентификатор эталонной должности';
COMMENT ON COLUMN position.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN position.rank_id IS 'Ссылка на идентификатор rank, разряд';
COMMENT ON COLUMN position.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN position.work_function_id IS 'Ссылка на идентификатор work_function, справочник рабочих функций';
COMMENT ON COLUMN position.workplace_id IS 'Ссылка на идентификатор workplace, рабочее место';
COMMENT ON COLUMN position.external_id IS 'Внешний код записи';
COMMENT ON COLUMN position.position_type_id IS 'Ссылка на идентификатор position_type, справочник типов должностей';
COMMENT ON COLUMN position.position_importance_id IS 'Ссылка на идентификатор position_importance, справочник критичностей позиции';
COMMENT ON COLUMN position.is_key_management IS 'Признак руководящей позиции';
COMMENT ON COLUMN position.profstandard_work_function_code IS 'Код рабочей функции профстандарта';
COMMENT ON COLUMN position.cost_center_id IS 'Ссылка на идентификатор cost_center, центр затрат';
COMMENT ON COLUMN position.profstandard_code IS 'Код профстандарта должности';
COMMENT ON COLUMN position.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position.is_head IS 'Флаг руководящей позиции';
CREATE INDEX position_rank_id_idx ON position (rank_id);
CREATE INDEX position_work_function_id_idx ON position (work_function_id);
CREATE INDEX position_position_type_id_idx ON position (position_type_id);
CREATE INDEX position_workplace_id_idx ON position (workplace_id);
CREATE INDEX position_precursor_id_idx ON position (precursor_id);
CREATE INDEX position_category_id_idx ON position (category_id);
CREATE INDEX position_division_id_idx ON position (division_id);
CREATE INDEX position_status_id_idx ON position (status_id);
CREATE INDEX position_job_title_id_idx ON position (job_title_id);
CREATE INDEX position_position_importance_id_idx ON position (position_importance_id);
CREATE INDEX position_cost_center_id_idx ON position (cost_center_id);

--assignment_type
CREATE TABLE assignment_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "assignment_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE assignment_type IS 'Тип назначения';
COMMENT ON COLUMN assignment_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN assignment_type.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN assignment_type.full_name IS 'Полное наименование';
COMMENT ON COLUMN assignment_type.short_name IS 'Краткое наименование';
COMMENT ON COLUMN assignment_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN assignment_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN assignment_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN assignment_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN assignment_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN assignment_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN assignment_type.unit_code IS 'Код юнита';
CREATE INDEX assignment_type_unit_code_idx ON assignment_type (unit_code);

--assignment_status
CREATE TABLE assignment_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "assignment_status_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE assignment_status IS 'Статус назначения';
COMMENT ON COLUMN assignment_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN assignment_status.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN assignment_status.full_name IS 'Полное наименование';
COMMENT ON COLUMN assignment_status.short_name IS 'Краткое наименование';
COMMENT ON COLUMN assignment_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN assignment_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN assignment_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN assignment_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN assignment_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN assignment_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN assignment_status.unit_code IS 'Код юнита';
CREATE INDEX assignment_status_unit_code_fkey ON assignment_status (unit_code);

--substitution_type
CREATE TABLE substitution_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "substitution_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE substitution_type IS 'Типы замещения должности';
COMMENT ON COLUMN substitution_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN substitution_type.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN substitution_type.full_name IS 'Полное наименование';
COMMENT ON COLUMN substitution_type.short_name IS 'Краткое наименование';
COMMENT ON COLUMN substitution_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN substitution_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN substitution_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN substitution_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN substitution_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN substitution_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN substitution_type.unit_code IS 'Код юнита';
CREATE INDEX substitution_type_unit_code_idx ON substitution_type (unit_code);

--assignment_category
CREATE TABLE assignment_category
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "assignment_category_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE assignment_category IS 'Категории назначения на позицию';
COMMENT ON COLUMN assignment_category.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN assignment_category.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN assignment_category.full_name IS 'Полное наименование';
COMMENT ON COLUMN assignment_category.short_name IS 'Краткое наименование';
COMMENT ON COLUMN assignment_category.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN assignment_category.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN assignment_category.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN assignment_category.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN assignment_category.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN assignment_category.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN assignment_category.unit_code IS 'Код юнита';
CREATE INDEX assignment_category_unit_code_idx ON assignment_category (unit_code);

--placement
CREATE TABLE placement
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code               VARCHAR(32)                         NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(64)                         NOT NULL,
    precursor_id       BIGINT,
    workplace_id       BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "placement_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES placement (id),
    CONSTRAINT "placement_workplace_id_fk" FOREIGN KEY (workplace_id) REFERENCES workplace (id),
    CONSTRAINT "placement_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE placement IS 'Каталог мест размещения';
COMMENT ON COLUMN placement.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN placement.code IS 'Системный код записи';
COMMENT ON COLUMN placement.date_from IS 'Дата создания записи';
COMMENT ON COLUMN placement.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN placement."name" IS 'Наименование';
COMMENT ON COLUMN placement.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN placement.workplace_id IS 'Ссылка на идентификатор workplace, рабочее место';
COMMENT ON COLUMN placement.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN placement.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN placement.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN placement.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN placement.unit_code IS 'Код юнита';
CREATE INDEX placement_precursor_id_idx ON placement (precursor_id);
CREATE INDEX placement_workplace_id_idx ON placement (workplace_id);
CREATE INDEX placement_unit_code_idx ON placement (unit_code);

--position_assignment
CREATE TABLE position_assignment
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation         VARCHAR(128)                        NOT NULL,
    date_from            TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to              TIMESTAMP,
    full_name            VARCHAR(512)                        NOT NULL,
    probation_date_to    TIMESTAMP DEFAULT NOW()             NOT NULL,
    short_name           VARCHAR(256)                        NOT NULL,
    stake                real                                NOT NULL,
    category_id          BIGINT,
    employee_id          BIGINT                              NOT NULL,
    placement_id         BIGINT,
    position_id          BIGINT                              NOT NULL,
    precursor_id         BIGINT,
    status_id            BIGINT, -- TODO update entity
    substitution_type_id BIGINT, -- TODO update entity
    type_id              BIGINT, -- TODO update entity
    external_id          VARCHAR(128) UNIQUE,
    author_employee_id   BIGINT                              NOT NULL,
    update_employee_id   BIGINT                              NOT NULL,
    update_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    job_title_id         BIGINT,
    CONSTRAINT "position_assignment_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id),
    CONSTRAINT "position_assignment_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES position_assignment (id),
    CONSTRAINT "position_assignment_type_id_fk" FOREIGN KEY (type_id) REFERENCES assignment_type (id),
    CONSTRAINT "position_assignment_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "position_assignment_status_id_fk" FOREIGN KEY (status_id) REFERENCES assignment_status (id),
    CONSTRAINT "position_assignment_substitution_type_id_fk" FOREIGN KEY (substitution_type_id) REFERENCES substitution_type (id),
    CONSTRAINT "position_assignment_category_id_fk" FOREIGN KEY (category_id) REFERENCES assignment_category (id),
    CONSTRAINT "position_assignment_placement_id_fk" FOREIGN KEY (placement_id) REFERENCES placement (id),
    CONSTRAINT "position_assignment_job_title_id_fk" FOREIGN KEY (job_title_id) REFERENCES job_title (id)
);
COMMENT ON TABLE position_assignment IS 'Назначения на должность';
COMMENT ON COLUMN position_assignment.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_assignment.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN position_assignment.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_assignment.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN position_assignment.full_name IS 'Полное наименование';
COMMENT ON COLUMN position_assignment.probation_date_to IS 'Дата окончания испытательного срока';
COMMENT ON COLUMN position_assignment.short_name IS 'Краткое наименование';
COMMENT ON COLUMN position_assignment.stake IS 'Доля занятости должности при назначении';
COMMENT ON COLUMN position_assignment.category_id IS 'Идентификатор категории назначения на должности';
COMMENT ON COLUMN position_assignment.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN position_assignment.placement_id IS 'Идентификатор размещения назначения';
COMMENT ON COLUMN position_assignment.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_assignment.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN position_assignment.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN position_assignment.substitution_type_id IS 'Ссылка на идентификатор substitution_type, справочник типов замещения должности';
COMMENT ON COLUMN position_assignment.type_id IS 'Идентификатор типа назначения на должность';
COMMENT ON COLUMN position_assignment.external_id IS 'Внешний код записи';
COMMENT ON COLUMN position_assignment.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN position_assignment.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN position_assignment.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN position_assignment.job_title_id IS 'Идентификатор эталонной должности (ОКПДТР)';
CREATE INDEX position_assignment_position_id_idx ON position_assignment (position_id);
CREATE INDEX position_assignment_precursor_id_idx ON position_assignment (precursor_id);
CREATE INDEX position_assignment_type_id_idx ON position_assignment (type_id);
CREATE INDEX position_assignment_employee_id_idx ON position_assignment (employee_id);
CREATE INDEX position_assignment_status_id_idx ON position_assignment (status_id);
CREATE INDEX position_assignment_substitution_type_id_idx ON position_assignment (substitution_type_id);
CREATE INDEX position_assignment_category_id_idx ON position_assignment (category_id);
CREATE INDEX position_assignment_placement_id_idx ON position_assignment (placement_id);
CREATE INDEX position_assignment_job_title_id_idx ON position_assignment (job_title_id);

--division_team
CREATE TABLE division_team
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    is_head            BOOLEAN   DEFAULT FALSE             NOT NULL, -- todo update entity
    short_name         VARCHAR(256)                        NOT NULL,
    division_id        BIGINT,
    parent_id          BIGINT,
    precursor_id       BIGINT,
    status_id          BIGINT,                                       -- TODO update entity
    type_id            BIGINT,                                       -- TODO update entity
    external_id        VARCHAR(128) UNIQUE,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    description        VARCHAR(2048),
    CONSTRAINT "division_team_status_id_fk" FOREIGN KEY (status_id) REFERENCES team_status (id),
    CONSTRAINT "division_team_type_id_fk" FOREIGN KEY (type_id) REFERENCES team_type (id),
    CONSTRAINT "division_team_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES division_team (id),
    CONSTRAINT "division_team_division_id_fk" FOREIGN KEY (division_id) REFERENCES division (id),
    CONSTRAINT "division_team_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES division_team (id)
);
COMMENT ON TABLE division_team IS 'Каталог команд подразделения';
COMMENT ON COLUMN division_team.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_team.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN division_team.date_from IS 'Дата создания записи';
COMMENT ON COLUMN division_team.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN division_team.full_name IS 'Полное наименование';
COMMENT ON COLUMN division_team.is_head IS 'Признак команды-руководителя';
COMMENT ON COLUMN division_team.short_name IS 'Краткое наименование';
COMMENT ON COLUMN division_team.division_id IS 'Ссылка на идентификатор division, подразделение';
COMMENT ON COLUMN division_team.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN division_team.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN division_team.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN division_team.type_id IS 'Идентификатор типа команды';
COMMENT ON COLUMN division_team.external_id IS 'Внешний код записи';
COMMENT ON COLUMN division_team.description IS 'Описание команды';
CREATE INDEX division_team_status_id_idx ON division_team (status_id);
CREATE INDEX division_team_type_id_idx ON division_team (type_id);
CREATE INDEX division_team_precursor_id_idx ON division_team (precursor_id);
CREATE INDEX division_team_division_id_idx ON division_team (division_id);
CREATE INDEX division_team_parent_id_idx ON division_team (parent_id);

--system_role
CREATE TABLE system_role
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name"             VARCHAR(128)                        NOT NULL,
    is_assignable      BOOLEAN   DEFAULT FALSE             NOT NULL,             -- TODO update entity
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT system_role_name_unit_code_key UNIQUE (name, unit_code),
    CONSTRAINT "system_role_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE system_role IS 'Системные роли';
COMMENT ON COLUMN system_role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN system_role."name" IS 'Наименование';
COMMENT ON COLUMN system_role.is_assignable IS 'Признак назначаемой роли. 1 - назначаемая роль, 0 - системная.';
COMMENT ON COLUMN system_role.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN system_role.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN system_role.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN system_role.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN system_role.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN system_role.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN system_role.unit_code IS 'Код юнита';
CREATE INDEX system_role_unit_code_idx ON system_role (unit_code);

--role
CREATE TABLE role
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    system_role_id     BIGINT, -- TODO update entity
    code               VARCHAR(128),
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "role_system_role_id_fk" FOREIGN KEY (system_role_id) REFERENCES system_role (id),
    CONSTRAINT "role_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE role IS 'Роли';
COMMENT ON COLUMN role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN role.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN role.full_name IS 'Полное наименование';
COMMENT ON COLUMN role.short_name IS 'Краткое наименование';
COMMENT ON COLUMN role.system_role_id IS 'Идентификатор системной роли';
COMMENT ON COLUMN role.code IS 'Системный код записи';
COMMENT ON COLUMN role.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN role.unit_code IS 'Код юнита';
CREATE INDEX role_system_role_id_idx ON role (system_role_id);
CREATE INDEX role_system_unit_code_idx ON role (unit_code);

--division_team_role
CREATE TABLE division_team_role
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    division_team_id       BIGINT                              NOT NULL,
    role_id                BIGINT                              NOT NULL,
    position_importance_id BIGINT, -- TODO update entity
    external_id            VARCHAR(128) UNIQUE,
    author_employee_id     BIGINT                              NOT NULL,
    update_employee_id     BIGINT                              NOT NULL,
    update_date            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "division_team_role_division_team_id_fk" FOREIGN KEY (division_team_id) REFERENCES division_team (id),
    CONSTRAINT "division_team_role_position_importance_id_fk" FOREIGN KEY (position_importance_id) REFERENCES position_importance (id),
    CONSTRAINT "division_team_role_role_id_fk" FOREIGN KEY (role_id) REFERENCES role (id)
);
COMMENT ON COLUMN division_team_role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_team_role.division_team_id IS 'Ссылка на идентификатор division_team, каталог команд подразделений';
COMMENT ON COLUMN division_team_role.role_id IS 'Ссылка на идентификатор role, роли';
COMMENT ON COLUMN division_team_role.position_importance_id IS 'Ссылка на идентификатор position_importance, справочник критичностей позиции';
COMMENT ON COLUMN division_team_role.external_id IS 'Внешний код записи';
COMMENT ON COLUMN division_team_role.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN division_team_role.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN division_team_role.update_date IS 'Дата обновления записи';
CREATE INDEX division_team_role_division_team_id_idx ON division_team_role (division_team_id);
CREATE INDEX division_team_role_position_importance_id_idx ON division_team_role (position_importance_id);
CREATE INDEX division_team_role_role_id_idx ON division_team_role (role_id);

--division_team_assignment
CREATE TABLE division_team_assignment
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation          VARCHAR(128)                        NOT NULL,
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    full_name             VARCHAR(512)                        NOT NULL,
    short_name            VARCHAR(256)                        NOT NULL,
    employee_id           BIGINT                              NOT NULL,
    precursor_id          BIGINT,
    status_id             BIGINT, -- TODO update entity
    type_id               BIGINT, -- TODO update entity
    division_team_role_id BIGINT                              NOT NULL,
    external_id           VARCHAR(128) UNIQUE,
    author_employee_id    BIGINT                              NOT NULL,
    update_employee_id    BIGINT                              NOT NULL,
    update_date           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "division_team_assignment_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "division_team_assignment_status_id_fk" FOREIGN KEY (status_id) REFERENCES assignment_status (id),
    CONSTRAINT "division_team_assignment_type_id_fk" FOREIGN KEY (type_id) REFERENCES assignment_type (id),
    CONSTRAINT "division_team_assignment_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES division_team_assignment (id),
    CONSTRAINT "division_team_assignment_division_team_role_id_fk" FOREIGN KEY (division_team_role_id) REFERENCES division_team_role (id)
);
COMMENT ON COLUMN division_team_assignment.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_team_assignment.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN division_team_assignment.date_from IS 'Дата создания записи';
COMMENT ON COLUMN division_team_assignment.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN division_team_assignment.full_name IS 'Полное наименование';
COMMENT ON COLUMN division_team_assignment.short_name IS 'Краткое наименование';
COMMENT ON COLUMN division_team_assignment.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN division_team_assignment.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN division_team_assignment.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN division_team_assignment.type_id IS 'Идентификатор типа назначения в команду';
COMMENT ON COLUMN division_team_assignment.division_team_role_id IS 'Ссылка на идентификатор division_team_role, каталог ролей на позициях в командах';
COMMENT ON COLUMN division_team_assignment.external_id IS 'Внешний код записи';
COMMENT ON COLUMN division_team_assignment.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN division_team_assignment.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN division_team_assignment.update_date IS 'Дата обновления записи';
CREATE INDEX division_team_assignment_employee_id_idx ON division_team_assignment (employee_id);
CREATE INDEX division_team_assignment_status_id_idx ON division_team_assignment (status_id);
CREATE INDEX division_team_assignment_type_id_idx ON division_team_assignment (type_id);
CREATE INDEX division_team_assignment_precursor_id_idx ON division_team_assignment (precursor_id);
CREATE INDEX division_team_assignment_division_team_role_id_idx ON division_team_assignment (division_team_role_id);

--assignment_readiness
CREATE TABLE assignment_readiness
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name"    VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP                                    DEFAULT NOW() NOT NULL,
    date_to   TIMESTAMP,
    unit_code VARCHAR(128)                        NOT NULL DEFAULT 'default'
);
COMMENT ON TABLE assignment_readiness IS 'Уровни готовности к ротации работников в системе';
COMMENT ON COLUMN assignment_readiness.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN assignment_readiness."name" IS 'Название уровня готовности';
COMMENT ON COLUMN assignment_readiness.date_from IS 'Дата и время создания записи об уровне готовности';
COMMENT ON COLUMN assignment_readiness.date_to IS 'Дата и время удаления записи об уровне готовности. Если уровень актуальный, то значение null';
COMMENT ON COLUMN assignment_readiness.unit_code IS 'Код юнита';


--position_grade
CREATE TABLE position_grade
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"      VARCHAR(256)                        NOT NULL,
    description VARCHAR(256)                        NOT NULL,
    "index"     BIGINT                              NOT NULL,
    unit_code   VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "position_grade_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_grade IS 'Грэйды должностей';
COMMENT ON COLUMN position_grade.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_grade."name" IS 'Наименование';
COMMENT ON COLUMN position_grade.description IS 'Описание';
COMMENT ON COLUMN position_grade."index" IS 'Правила сортировки при выводе списка значений на экран или в печатную форму';
COMMENT ON COLUMN position_grade.unit_code IS 'Код юнита';
CREATE INDEX position_grade_unit_code_idx ON position_grade (unit_code);

--position_kr_level
CREATE TABLE position_kr_level
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"      VARCHAR(256)                        NOT NULL,
    description VARCHAR(256)                        NOT NULL,
    unit_code   VARCHAR(128)                        NOT NULL DEFAULT 'default'
);
COMMENT ON TABLE position_kr_level IS 'Уровни кадрового резерва';
COMMENT ON COLUMN position_kr_level.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_kr_level."name" IS 'Наименование';
COMMENT ON COLUMN position_kr_level.description IS 'Описание';
COMMENT ON COLUMN position_kr_level.unit_code IS 'Код юнита';

--position_position_grade
CREATE TABLE position_position_grade
(
    position_id       BIGINT                  NOT NULL,
    position_grade_id BIGINT                  NOT NULL,
    date_from         TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to           TIMESTAMP,
    CONSTRAINT "position_position_grade_position_grade_id_fk" FOREIGN KEY (position_grade_id) REFERENCES position_grade (id),
    CONSTRAINT "position_position_grade_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id)
);
COMMENT ON TABLE position_position_grade IS 'Связь должностей и грейдов';
COMMENT ON COLUMN position_position_grade.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_position_grade.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_position_grade.date_to IS 'Дата окончания записи';
CREATE INDEX position_position_grade_position_grade_id_idx ON position_position_grade (position_grade_id);
CREATE INDEX position_position_grade_position_id_idx ON position_position_grade (position_id);

--position_position_kr_level
CREATE TABLE position_position_kr_level
(
    position_id          BIGINT                  NOT NULL,
    position_kr_level_id BIGINT                  NOT NULL,
    date_from            TIMESTAMP DEFAULT NOW() NOT NULL,
    date_to              TIMESTAMP,
    CONSTRAINT "position_position_kr_level_position_kr_level_id_fk" FOREIGN KEY (position_kr_level_id) REFERENCES position_kr_level (id),
    CONSTRAINT "position_position_kr_level_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id)
);
COMMENT ON TABLE position_position_kr_level IS 'Связь должностей и уровней кадрового резерва';
COMMENT ON COLUMN position_position_kr_level.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_position_kr_level.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_position_kr_level.date_to IS 'Дата окончания записи';
CREATE INDEX position_position_kr_level_position_kr_level_id_idx ON position_position_kr_level (position_kr_level_id);
CREATE INDEX position_position_kr_level_position_id_idx ON position_position_kr_level (position_id);

--position_group
CREATE TABLE position_group
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"      VARCHAR(128)                        NOT NULL,
    description VARCHAR(1024),
    date_from   TIMESTAMP                                    DEFAULT NOW() NOT NULL,
    date_to     TIMESTAMP,
    unit_code   VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "position_group_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE position_group IS 'Группы должностей';
COMMENT ON COLUMN position_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_group."name" IS 'Наименование';
COMMENT ON COLUMN position_group.description IS 'Описание';
COMMENT ON COLUMN position_group.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_group.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN position_group.unit_code IS 'Код юнита';
CREATE INDEX position_group_unit_code_idx ON position_group (unit_code);

--position_successor
CREATE TABLE position_successor
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_commit_hr         TIMESTAMP,
    date_priority          TIMESTAMP,
    employee_id            BIGINT                              NOT NULL,
    position_id            BIGINT,
    position_group_id      BIGINT,
    reason_id_inclusion    BIGINT,
    reason_id_exclusion    BIGINT,
    comment_inclusion      VARCHAR(1024),
    comment_exclusion      VARCHAR(1024),
    document_url_inclusion VARCHAR(512),
    document_url_exclusion VARCHAR(512),
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP,
    CONSTRAINT "position_successor_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "position_successor_position_group_id_fk" FOREIGN KEY (position_group_id) REFERENCES position_group (id),
    CONSTRAINT "position_successor_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id)
);
COMMENT ON TABLE position_successor IS 'Список преемников на должности';
COMMENT ON COLUMN position_successor.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_successor.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN position_successor.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_successor.position_group_id IS 'Ссылка на идентификатор position_group, справочник групп должностей';
COMMENT ON COLUMN position_successor.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_successor.date_to IS 'Дата окончания записи';
CREATE INDEX position_successor_employee_id_idx ON position_successor (employee_id);
CREATE INDEX position_successor_position_group_id_idx ON position_successor (position_group_id);
CREATE INDEX position_successor_position_id_idx ON position_successor (position_id);
CREATE INDEX position_successor_reason_id_exclusion_idx ON position_successor (reason_id_exclusion);
CREATE INDEX position_successor_reason_id_inclusion_idx ON position_successor (reason_id_inclusion);

--position_successor_readiness
CREATE TABLE position_successor_readiness
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    position_successor_id BIGINT                              NOT NULL,
    readiness_id          BIGINT                              NOT NULL, -- TODO update entity
    date_from             TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to               TIMESTAMP,
    CONSTRAINT "position_successor_readiness_position_successor_id_fk" FOREIGN KEY (position_successor_id) REFERENCES position_successor (id),
    CONSTRAINT "position_successor_readiness_readiness_id_fk" FOREIGN KEY (readiness_id) REFERENCES assignment_readiness (id)
);
COMMENT ON TABLE position_successor_readiness IS 'Связь преемников и уровней готовности к ротации работников в системе';
COMMENT ON COLUMN position_successor_readiness.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_successor_readiness.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_successor_readiness.date_to IS 'Дата окончания записи';
CREATE INDEX position_successor_readiness_position_successor_id_idx ON position_successor_readiness (position_successor_id);
CREATE INDEX position_successor_readiness_readiness_id_idx ON position_successor_readiness (readiness_id);

--reason_type
CREATE TABLE reason_type
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name" VARCHAR(128)                        NOT NULL
);

--reason
CREATE TABLE reason
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    type_id     BIGINT                              NOT NULL,
    "name"      VARCHAR(128)                        NOT NULL,
    description VARCHAR(1024) DEFAULT ''::VARCHAR   NOT NULL,
    date_from   TIMESTAMP     DEFAULT NOW()         NOT NULL,
    date_to     TIMESTAMP,
    unit_code   VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "reason_type_id_fk" FOREIGN KEY (type_id) REFERENCES reason_type (id)
);
COMMENT ON COLUMN reason.unit_code IS 'Код юнита';
CREATE INDEX reason_type_id_idx ON reason (type_id);

--function_team_assignment
CREATE TABLE function_team_assignment
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    employee_id        BIGINT,
    function_team_id   BIGINT,
    precursor_id       BIGINT,
    role_id            BIGINT,
    status_id          BIGINT, -- TODO update entity
    type_id            BIGINT, -- TODO update entity
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "function_team_assignment_type_id_fk" FOREIGN KEY (type_id) REFERENCES assignment_type (id),
    CONSTRAINT "function_team_assignment_function_team_id_fk" FOREIGN KEY (function_team_id) REFERENCES function_team (id),
    CONSTRAINT "function_team_assignment_status_id_fk" FOREIGN KEY (status_id) REFERENCES assignment_status (id),
    CONSTRAINT "function_team_assignment_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "function_team_assignment_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES function_team_assignment (id),
    CONSTRAINT "function_team_assignment_role_id_fk" FOREIGN KEY (role_id) REFERENCES role (id)
);
COMMENT ON COLUMN function_team_assignment.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN function_team_assignment.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN function_team_assignment.date_from IS 'Дата создания записи';
COMMENT ON COLUMN function_team_assignment.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN function_team_assignment.full_name IS 'Полное наименование';
COMMENT ON COLUMN function_team_assignment.short_name IS 'Краткое наименование';
COMMENT ON COLUMN function_team_assignment.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN function_team_assignment.function_team_id IS 'Идентификатор функциональной команды';
COMMENT ON COLUMN function_team_assignment.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN function_team_assignment.role_id IS 'Ссылка на идентификатор role, роли';
COMMENT ON COLUMN function_team_assignment.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN function_team_assignment.type_id IS 'Идентификатор типа назначения в функциональную команду';
COMMENT ON COLUMN function_team_assignment.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN function_team_assignment.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN function_team_assignment.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN function_team_assignment.update_date IS 'Дата обновления записи';
CREATE INDEX function_team_assignment_type_id_idx ON function_team_assignment (type_id);
CREATE INDEX function_team_assignment_function_team_id_idx ON function_team_assignment (function_team_id);
CREATE INDEX function_team_assignment_status_id_idx ON function_team_assignment (status_id);
CREATE INDEX function_team_assignment_employee_id_idx ON function_team_assignment (employee_id);
CREATE INDEX function_team_assignment_precursor_id_idx ON function_team_assignment (precursor_id);
CREATE INDEX function_team_assignment_role_id_idx ON function_team_assignment (role_id);

--legal_entity_team
CREATE TABLE legal_entity_team
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation       VARCHAR(128)                        NOT NULL,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    legal_entity_id    BIGINT,
    parent_id          BIGINT,
    precursor_id       BIGINT,
    status_id          BIGINT, -- TODO update entity
    type_id            BIGINT, -- TODO update entity
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "legal_entity_team_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES legal_entity_team (id),
    CONSTRAINT "legal_entity_team_legal_entity_id_fk" FOREIGN KEY (legal_entity_id) REFERENCES legal_entity (id),
    CONSTRAINT "legal_entity_team_type_id_fk" FOREIGN KEY (type_id) REFERENCES team_type (id),
    CONSTRAINT "legal_entity_team_status_id_fk" FOREIGN KEY (status_id) REFERENCES team_status (id),
    CONSTRAINT "legal_entity_team_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES legal_entity_team (id)
);
COMMENT ON TABLE legal_entity_team IS 'Каталог команд по ЮЛ';
COMMENT ON COLUMN legal_entity_team.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN legal_entity_team.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN legal_entity_team.date_from IS 'Дата создания записи';
COMMENT ON COLUMN legal_entity_team.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN legal_entity_team.full_name IS 'Полное наименование';
COMMENT ON COLUMN legal_entity_team.short_name IS 'Краткое наименование';
COMMENT ON COLUMN legal_entity_team.legal_entity_id IS 'Ссылка на идентификатор legal_entity, каталог юридических лиц';
COMMENT ON COLUMN legal_entity_team.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN legal_entity_team.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN legal_entity_team.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN legal_entity_team.type_id IS 'Идентификатор типа команды ЮЛ';
COMMENT ON COLUMN legal_entity_team.external_id IS 'Внешний код записи';
COMMENT ON COLUMN legal_entity_team.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN legal_entity_team.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN legal_entity_team.update_date IS 'Дата обновления записи';
CREATE INDEX legal_entity_team_precursor_id_idx ON legal_entity_team (precursor_id);
CREATE INDEX legal_entity_team_legal_entity_id_idx ON legal_entity_team (legal_entity_id);
CREATE INDEX legal_entity_team_type_id_idx ON legal_entity_team (type_id);
CREATE INDEX legal_entity_team_status_id_idx ON legal_entity_team (status_id);
CREATE INDEX legal_entity_team_legal_parent_id_idx ON legal_entity_team (parent_id);

--legal_entity_team_assignment
CREATE TABLE legal_entity_team_assignment
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    abbreviation         VARCHAR(128)                        NOT NULL,
    date_from            TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to              TIMESTAMP,
    full_name            VARCHAR(512)                        NOT NULL,
    short_name           VARCHAR(256)                        NOT NULL,
    employee_id          BIGINT,
    legal_entity_team_id BIGINT,
    precursor_id         BIGINT,
    role_id              BIGINT,
    status_id            BIGINT, -- TODO update entity
    type_id              BIGINT, -- TODO update entity
    external_id          VARCHAR(128) UNIQUE,
    author_employee_id   BIGINT                              NOT NULL,
    update_employee_id   BIGINT                              NOT NULL,
    update_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "legal_entity_team_assignment_conflict_roles_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "legal_entity_team_assignmen_status_id_fk" FOREIGN KEY (status_id) REFERENCES assignment_status (id),
    CONSTRAINT "legal_entity_team_assignment_legal_entity_team_id_fk" FOREIGN KEY (legal_entity_team_id) REFERENCES legal_entity_team (id),
    CONSTRAINT "legal_entity_team_assignment_type_id_fk" FOREIGN KEY (type_id) REFERENCES assignment_type (id),
    CONSTRAINT "legal_entity_team_assignment_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES legal_entity_team_assignment (id),
    CONSTRAINT "legal_entity_team_assignment_role_id_fk" FOREIGN KEY (role_id) REFERENCES role (id)
);
COMMENT ON COLUMN legal_entity_team_assignment.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN legal_entity_team_assignment.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN legal_entity_team_assignment.date_from IS 'Дата создания записи';
COMMENT ON COLUMN legal_entity_team_assignment.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN legal_entity_team_assignment.full_name IS 'Полное наименование';
COMMENT ON COLUMN legal_entity_team_assignment.short_name IS 'Краткое наименование';
COMMENT ON COLUMN legal_entity_team_assignment.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN legal_entity_team_assignment.legal_entity_team_id IS 'Ссылка на идентификатор legal_entity_team, каталог команд по ЮЛ';
COMMENT ON COLUMN legal_entity_team_assignment.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN legal_entity_team_assignment.role_id IS 'Ссылка на идентификатор role, роли';
COMMENT ON COLUMN legal_entity_team_assignment.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN legal_entity_team_assignment.type_id IS 'Идентификатор типа назначения в команду ЮЛ';
COMMENT ON COLUMN legal_entity_team_assignment.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN legal_entity_team_assignment.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN legal_entity_team_assignment.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN legal_entity_team_assignment.update_date IS 'Дата обновления записи';
CREATE INDEX legal_entity_team_assignment_employee_id_idx ON legal_entity_team_assignment (employee_id);
CREATE INDEX legal_entity_team_assignment_status_id_idx ON legal_entity_team_assignment (status_id);
CREATE INDEX legal_entity_team_assignment_legal_entity_team_id_idx ON legal_entity_team_assignment (legal_entity_team_id);
CREATE INDEX legal_entity_team_assignment_type_id_idx ON legal_entity_team_assignment (type_id);
CREATE INDEX legal_entity_team_assignment_precursor_id_idx ON legal_entity_team_assignment (precursor_id);
CREATE INDEX legal_entity_team_assignment_role_id_idx ON legal_entity_team_assignment (role_id);

--personnel_document_type
CREATE TABLE personnel_document_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    code               VARCHAR(128),
    CONSTRAINT personnel_document_type_name_unit_code_key UNIQUE (name, unit_code),
    CONSTRAINT "personnel_document_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE personnel_document_type IS 'Типы документов работника';
COMMENT ON COLUMN personnel_document_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN personnel_document_type."name" IS 'Наименование';
COMMENT ON COLUMN personnel_document_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN personnel_document_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN personnel_document_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN personnel_document_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN personnel_document_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN personnel_document_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN personnel_document_type.unit_code IS 'Код юнита';
COMMENT ON COLUMN personnel_document_type.code IS 'Код типа документа';
CREATE INDEX personnel_document_type_unit_code_fkey ON personnel_document_type (unit_code);

--personnel_document_form
CREATE TABLE personnel_document_form
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(500)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    code               VARCHAR(128),
    CONSTRAINT personnel_document_form_name_unit_code_key UNIQUE (name, unit_code),
    CONSTRAINT personnel_document_form_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE personnel_document_form IS 'Формы документов работника';
COMMENT ON COLUMN personnel_document_form.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN personnel_document_form."name" IS 'Наименование';
COMMENT ON COLUMN personnel_document_form.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN personnel_document_form.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN personnel_document_form.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN personnel_document_form.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN personnel_document_form.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN personnel_document_form.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN personnel_document_form.unit_code IS 'Код юнита';
COMMENT ON COLUMN personnel_document_form.code IS 'Форма типа документа';
CREATE INDEX personnel_document_form_unit_code_idx ON personnel_document_form (unit_code);

--personnel_document
CREATE TABLE personnel_document
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    data               text,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128),
    employee_id        BIGINT,
    form_id            BIGINT,
    precursor_id       BIGINT,
    type_id            BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    vendor             VARCHAR(256),
    number             VARCHAR(128),
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    CONSTRAINT "personnel_document_form_id_fk" FOREIGN KEY (form_id) REFERENCES personnel_document_form (id),
    CONSTRAINT "personnel_document_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "personnel_document_type_id_fk" FOREIGN KEY (type_id) REFERENCES personnel_document_type (id),
    CONSTRAINT "personnel_document_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES personnel_document (id),
    CONSTRAINT "personnel_document_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE personnel_document IS 'Каталог документов работника';
COMMENT ON COLUMN personnel_document.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN personnel_document.data IS 'Дата документа';
COMMENT ON COLUMN personnel_document.date_from IS 'Дата создания записи';
COMMENT ON COLUMN personnel_document.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN personnel_document."name" IS 'Наименование';
COMMENT ON COLUMN personnel_document.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN personnel_document.form_id IS 'Идентификатор формы документа работника';
COMMENT ON COLUMN personnel_document.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN personnel_document.type_id IS 'Идентификатор типа документа работника';
COMMENT ON COLUMN personnel_document.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN personnel_document.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN personnel_document.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN personnel_document.unit_code IS 'Код юнита';
COMMENT ON COLUMN personnel_document.vendor IS 'Вендор сертификата';
COMMENT ON COLUMN personnel_document.number IS 'Номер документа/сертификата';
COMMENT ON COLUMN personnel_document.date_start IS 'Дата выдачи сертификата';
COMMENT ON COLUMN personnel_document.date_end IS 'Дата окончания сертификата';
CREATE INDEX personnel_document_form_id_idx ON personnel_document (form_id);
CREATE INDEX personnel_document_employee_id_idx ON personnel_document (employee_id);
CREATE INDEX personnel_document_type_id_idx ON personnel_document (type_id);
CREATE INDEX personnel_document_precursor_id_idx ON personnel_document (precursor_id);
CREATE INDEX personnel_document_unit_code_idx ON personnel_document (unit_code);

--work_experience_type
CREATE TABLE work_experience_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(128)                        NOT NULL,
    description        VARCHAR(1024),
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT work_experience_type_name_unit_code_key UNIQUE ("name", unit_code),
    CONSTRAINT "work_experience_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON COLUMN work_experience_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_experience_type."name" IS 'Наименование';
COMMENT ON COLUMN work_experience_type.description IS 'Описание типа стажа';
COMMENT ON COLUMN work_experience_type.external_id IS 'Внешний код записи';
COMMENT ON COLUMN work_experience_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN work_experience_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN work_experience_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN work_experience_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN work_experience_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN work_experience_type.unit_code IS 'Код юнита';
CREATE INDEX work_experience_type_unit_code_idx ON work_experience_type (unit_code);

--work_experience
CREATE TABLE work_experience
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    type_id            BIGINT                              NOT NULL,
    employee_id        BIGINT                              NOT NULL,
    duration_years     BIGINT    DEFAULT 0                 NOT NULL,
    duration_months    BIGINT    DEFAULT 0                 NOT NULL,
    duration_days      BIGINT    DEFAULT 0                 NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "work_experience_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "work_experience_type_id_fk" FOREIGN KEY (type_id) REFERENCES work_experience_type (id)
);
COMMENT ON COLUMN work_experience.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_experience.date_from IS 'Дата создания записи';
COMMENT ON COLUMN work_experience.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN work_experience.type_id IS 'Идентификатор типа стажа';
COMMENT ON COLUMN work_experience.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN work_experience.duration_years IS 'Продолжительность в годах';
COMMENT ON COLUMN work_experience.duration_months IS 'Продолжительность в месяцах';
COMMENT ON COLUMN work_experience.duration_days IS 'Продолжительность в днях';
COMMENT ON COLUMN work_experience.external_id IS 'Внешний код записи';
COMMENT ON COLUMN work_experience.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN work_experience.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN work_experience.update_date IS 'Дата обновления записи';
CREATE INDEX work_experience_employee_id_idx ON work_experience (employee_id);
CREATE INDEX work_experience_type_id_idx ON work_experience (type_id);

--disability_category
CREATE TABLE disability_category
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON COLUMN disability_category.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN disability_category."name" IS 'Наименование';
COMMENT ON COLUMN disability_category.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN disability_category.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN disability_category.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN disability_category.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN disability_category.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN disability_category.update_date IS 'Дата обновления записи';

--person_disability
CREATE TABLE person_disability
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    disability_description VARCHAR(64)                         NOT NULL,
    disability_name        VARCHAR(64)                         NOT NULL,
    person_id              BIGINT,
    date_from              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                TIMESTAMP,
    disability_category_id BIGINT                              NOT NULL,
    date_start             TIMESTAMP,
    date_end               TIMESTAMP,
    external_id            VARCHAR(128) UNIQUE,
    author_employee_id     BIGINT                              NOT NULL,
    update_employee_id     BIGINT                              NOT NULL,
    update_date            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "person_disability_person_id_fk" FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT "person_disability_disability_category_id_fk" FOREIGN KEY (disability_category_id) REFERENCES disability_category (id)
);
COMMENT ON TABLE person_disability IS 'Каталог ФЛ с инвалидностью';
COMMENT ON COLUMN person_disability.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN person_disability.disability_description IS 'Описание инвалидности ФЛ';
COMMENT ON COLUMN person_disability.disability_name IS 'Наименование инвалидности работника';
COMMENT ON COLUMN person_disability.person_id IS 'Ссылка на идентификатор person, физические лица';
COMMENT ON COLUMN person_disability.date_from IS 'Дата создания записи';
COMMENT ON COLUMN person_disability.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN person_disability.disability_category_id IS 'Идентификатор группы инвалидности';
COMMENT ON COLUMN person_disability.date_start IS 'Запланированная дата начала';
COMMENT ON COLUMN person_disability.date_end IS 'Запланированная дата закрытие';
COMMENT ON COLUMN person_disability.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN person_disability.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN person_disability.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN person_disability.update_date IS 'Дата обновления записи';
CREATE INDEX person_disability_person_id_idx ON person_disability (person_id);
CREATE INDEX person_disability_disability_category_id_idx ON person_disability (disability_category_id);

--division_team_successor
CREATE TABLE division_team_successor
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP,
    date_commit_hr         TIMESTAMP,
    division_team_role_id  BIGINT,
    employee_id            BIGINT,
    date_priority          TIMESTAMP,
    reason_id_inclusion    BIGINT, -- TODO update entity
    reason_id_exclusion    BIGINT, -- todo update entity
    comment_inclusion      VARCHAR(1024),
    comment_exclusion      VARCHAR(1024),
    document_url_inclusion VARCHAR(512),
    document_url_exclusion VARCHAR(512),
    CONSTRAINT "division_team_successor_division_team_role_id_fk" FOREIGN KEY (division_team_role_id) REFERENCES division_team_role (id),
    CONSTRAINT "division_team_successor_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id)
);
COMMENT ON TABLE division_team_successor IS 'Список преемников';
COMMENT ON COLUMN division_team_successor.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_team_successor.date_from IS 'Дата и время создания записи о преемнике';
COMMENT ON COLUMN division_team_successor.date_to IS 'Дата и время удаления записи о преемнике. Если преемник действителен, то значение null';
COMMENT ON COLUMN division_team_successor.date_commit_hr IS 'Дата и время согласование преемника сотрудником HR';
COMMENT ON COLUMN division_team_successor.division_team_role_id IS 'Ссылка на идентификационный номер таблицы division_team_role, роли работника в подразделении. Данная связь показывает на какую роль в подразделении работник является преемником';
COMMENT ON COLUMN division_team_successor.employee_id IS 'Ссылка на идентификационный номер преемника (employee_id)';
CREATE INDEX division_team_successor_division_team_role_id_idx ON division_team_successor (division_team_role_id);
CREATE INDEX division_team_successor_employee_id_idx ON division_team_successor (employee_id);
CREATE INDEX division_team_successor_reason_id_exclusion_idx ON division_team_successor (reason_id_exclusion);
CREATE INDEX division_team_successor_reason_id_inclusion_idx ON division_team_successor (reason_id_inclusion);

--division_team_successor_readiness
CREATE TABLE division_team_successor_readiness
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                  TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                    TIMESTAMP,
    division_team_successor_id BIGINT,
    readiness_id               BIGINT, -- TODO update entity
    CONSTRAINT "division_team_successor_readiness_dta_id_fk" FOREIGN KEY (division_team_successor_id) REFERENCES division_team_successor (id),
    CONSTRAINT "division_team_successor_readiness_readiness_id_fk" FOREIGN KEY (readiness_id) REFERENCES assignment_readiness (id)
);
COMMENT ON TABLE division_team_successor_readiness IS 'Связь назначений работников и их готовности';
COMMENT ON COLUMN division_team_successor_readiness.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_team_successor_readiness.date_from IS 'Дата и время создания записи о готовности';
COMMENT ON COLUMN division_team_successor_readiness.date_to IS 'Дата и время удаления записи о готовности. Если готовность актуальная, то значение null';
COMMENT ON COLUMN division_team_successor_readiness.division_team_successor_id IS 'Ссылка на идентификационный номер таблицы division_team_successor, преемника.';
COMMENT ON COLUMN division_team_successor_readiness.readiness_id IS 'Ссылка на идентификационный номер таблицы readiness, готовности преемника. Данная связь показывает какая готовность у преемника.';
CREATE INDEX division_team_successor_readiness_dts_id_idx ON division_team_successor_readiness (division_team_successor_id);
CREATE INDEX division_team_successor_readiness_readiness_id_idx ON division_team_successor_readiness (readiness_id);

--assignment_rotation
CREATE TABLE assignment_rotation
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name"    VARCHAR(128)                        NOT NULL,
    date_from TIMESTAMP                                    DEFAULT NOW() NOT NULL,
    date_to   TIMESTAMP,
    unit_code VARCHAR(128)                        NOT NULL DEFAULT 'default'
);
COMMENT ON TABLE assignment_rotation IS 'Уровни мобильности';
COMMENT ON COLUMN assignment_rotation.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN assignment_rotation."name" IS 'Название уровня готовности';
COMMENT ON COLUMN assignment_rotation.date_from IS 'Дата и время создания записи об уровне готовности';
COMMENT ON COLUMN assignment_rotation.date_to IS 'Дата и время удаления записи об уровне готовности. Если уровень актуальный, то значение null';
COMMENT ON COLUMN assignment_rotation.unit_code IS 'Код юнита';

--division_team_assignment_rotation
CREATE TABLE division_team_assignment_rotation
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                     TIMESTAMP,
    date_commit_hr              TIMESTAMP,
    division_team_assignment_id BIGINT                              NOT NULL,
    rotation_id                 BIGINT                              NOT NULL, -- TODO update entity
    comment_hr                  VARCHAR(512),
    comment_employee            VARCHAR(512),
    CONSTRAINT "division_team_assignment_rotation_dta_id_fk" FOREIGN KEY (division_team_assignment_id) REFERENCES division_team_assignment (id),
    CONSTRAINT "division_team_assignment_rotation_rotation_id_fk" FOREIGN KEY (rotation_id) REFERENCES assignment_rotation (id)
);
COMMENT ON TABLE division_team_assignment_rotation IS 'Связь назначений работников и готовности к ротации';
COMMENT ON COLUMN division_team_assignment_rotation.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_team_assignment_rotation.date_from IS 'Дата и время создания записи о готовности';
COMMENT ON COLUMN division_team_assignment_rotation.date_to IS 'Дата и время удаления записи о готовности. Если готовность актуальная, то значение null';
COMMENT ON COLUMN division_team_assignment_rotation.date_commit_hr IS 'Дата и время согласование готовности сотрудником HR';
COMMENT ON COLUMN division_team_assignment_rotation.division_team_assignment_id IS 'Ссылка на идентификационный номер таблицы division_team_assigment, назначения работника в подразделение. Данная связь показывает назначение работника, по которому есть информация о готовности';
COMMENT ON COLUMN division_team_assignment_rotation.rotation_id IS 'Ссылка на идентификационный номер таблицы assignment_rotation, уровня готовности к ротации';
COMMENT ON COLUMN division_team_assignment_rotation.comment_hr IS 'Текстовый комментарий HR';
CREATE INDEX division_team_assignment_rotation_dta_id_idx ON division_team_assignment_rotation (division_team_assignment_id);
CREATE INDEX division_team_assignment_rotation_rotation_id_idx ON division_team_assignment_rotation (rotation_id);

--position_position_importance
CREATE TABLE position_position_importance
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    position_id            BIGINT                              NOT NULL,
    position_importance_id BIGINT, -- TODO update entity
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP,
    author_employee_id     BIGINT,
    system_role_id         BIGINT                              NOT NULL,
    CONSTRAINT "position_position_importance_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id),
    CONSTRAINT "position_position_importance_position_importance_id_fk" FOREIGN KEY (position_importance_id) REFERENCES position_importance (id),
    CONSTRAINT "position_position_importance_system_role_id_fk" FOREIGN KEY (system_role_id) REFERENCES system_role (id),
    CONSTRAINT "position_position_importance_author_employee_id_fk" FOREIGN KEY (author_employee_id) REFERENCES employee (id)
);
COMMENT ON COLUMN position_position_importance.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_position_importance.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_position_importance.position_importance_id IS 'Ссылка на идентификатор position_importance, справочник критичностей позиции';
COMMENT ON COLUMN position_position_importance.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_position_importance.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN position_position_importance.author_employee_id IS 'Пользователь создавший запись';
COMMENT ON COLUMN position_position_importance.system_role_id IS 'Идентификатор системной роли';
CREATE INDEX position_position_importance_position_id_idx ON position_position_importance (position_id);
CREATE INDEX position_position_importance_position_importance_id_idx ON position_position_importance (position_importance_id);
CREATE INDEX position_position_importance_system_role_id_idx ON position_position_importance (system_role_id);
CREATE INDEX position_position_importance_author_employee_id_idx ON position_position_importance (author_employee_id);

--calculation_method
CREATE TABLE calculation_method
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code        VARCHAR(64)                         NOT NULL,
    "name"      VARCHAR(128)                        NOT NULL,
    description VARCHAR(256)                        NOT NULL
);
COMMENT ON TABLE calculation_method IS 'Метод расчета риска по позиции';
COMMENT ON COLUMN calculation_method.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN calculation_method.code IS 'Системный код записи';
COMMENT ON COLUMN calculation_method."name" IS 'Наименование';
COMMENT ON COLUMN calculation_method.description IS 'Описание';

--importance_criteria_group_type
CREATE TABLE importance_criteria_group_type
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    "name" VARCHAR(256) UNIQUE                 NOT NULL
);
COMMENT ON TABLE importance_criteria_group_type IS 'Типы групп рисков позиций';
COMMENT ON COLUMN importance_criteria_group_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN importance_criteria_group_type."name" IS 'Наименование';

--importance_criteria_group
CREATE TABLE importance_criteria_group
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO: update entity
    type_id     BIGINT                              NOT NULL,             -- todo update entity
    "name"      VARCHAR(256)                        NOT NULL,
    description VARCHAR(1024) DEFAULT ''::VARCHAR   NOT NULL,
    is_editable BOOLEAN       DEFAULT FALSE         NOT NULL,             -- TODO: update entity
    unit_code   VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "importance_criteria_group_type_id_fk" FOREIGN KEY (type_id) REFERENCES importance_criteria_group_type (id)
);
COMMENT ON TABLE importance_criteria_group IS 'Группы рисков по позициям оргструктуры';
COMMENT ON COLUMN importance_criteria_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN importance_criteria_group.type_id IS 'Ссылка на идентификатор importance_criteria_group_type, типы рисков по позиции';
COMMENT ON COLUMN importance_criteria_group."name" IS 'Наименование';
COMMENT ON COLUMN importance_criteria_group.description IS 'Описание';
COMMENT ON COLUMN importance_criteria_group.is_editable IS 'Можно редактировать';
COMMENT ON COLUMN importance_criteria_group.unit_code IS 'Код юнита';
CREATE INDEX importance_criteria_group_type_id_idx ON importance_criteria_group (type_id);

--importance_criteria
CREATE TABLE importance_criteria
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- todo update entity
    group_id              BIGINT                              NOT NULL,             -- todo update entity
    "name"                VARCHAR(256)                        NOT NULL,
    description           VARCHAR(1024) DEFAULT ''::VARCHAR   NOT NULL,
    weight                numeric(6, 2) DEFAULT 0             NOT NULL,
    is_enabled            boolean       DEFAULT FALSE         NOT NULL,
    calculation_method_id BIGINT,
    CONSTRAINT "importance_criteria_calculation_method_id_fk" FOREIGN KEY (calculation_method_id) REFERENCES calculation_method (id),
    CONSTRAINT "importance_criteria_group_id_fk" FOREIGN KEY (group_id) REFERENCES importance_criteria_group (id)
);
COMMENT ON TABLE importance_criteria IS 'Типы рисков по позициям оргструктуры';
COMMENT ON COLUMN importance_criteria.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN importance_criteria.group_id IS 'Ссылка на идентификатор importance_criteria_group, группу рисков по позиции';
COMMENT ON COLUMN importance_criteria."name" IS 'Наименование';
COMMENT ON COLUMN importance_criteria.description IS 'Описание';
COMMENT ON COLUMN importance_criteria.weight IS 'Вес';
COMMENT ON COLUMN importance_criteria.is_enabled IS 'Признак включения строки (включена когда true)';
COMMENT ON COLUMN importance_criteria.calculation_method_id IS 'Ссылка на идентификатор calculation_method, метода расчета риска';
CREATE INDEX importance_criteria_calculation_method_id_idx ON importance_criteria (calculation_method_id);
CREATE INDEX importance_criteria_group_id_idx ON importance_criteria (group_id);

--position_importance_criteria
CREATE TABLE position_importance_criteria
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY, -- todo update entity
    date_from              TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                TIMESTAMP,
    position_id            BIGINT                                  NOT NULL,             -- todo update entity
    importance_criteria_id BIGINT                                  NOT NULL,             -- todo update entity
    weight                 numeric(6, 2) DEFAULT 0                 NOT NULL,
    value                  numeric(6, 2) DEFAULT 0                 NOT NULL,
    CONSTRAINT "position_importance_criteria_importance_criteria_id_fk" FOREIGN KEY (importance_criteria_id) REFERENCES importance_criteria (id),
    CONSTRAINT "position_importance_criteria_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id)
);
COMMENT ON TABLE position_importance_criteria IS 'Связь должностей и рисков';
COMMENT ON COLUMN position_importance_criteria.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_importance_criteria.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_importance_criteria.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN position_importance_criteria.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_importance_criteria.weight IS 'Вес';
COMMENT ON COLUMN position_importance_criteria.value IS 'Значение параметра';
CREATE INDEX position_importance_criteria_importance_criteria_id_idx ON position_importance_criteria (importance_criteria_id);
CREATE INDEX position_importance_criteria_position_id_idx ON position_importance_criteria (position_id);

--division_links
CREATE TABLE division_links
(
    parent_id       BIGINT       NOT NULL,
    child_id        BIGINT       NOT NULL,
    level_to_parent BIGINT       NOT NULL, -- TODO update entity
    unit_code       VARCHAR(128) NOT NULL DEFAULT 'default',
    PRIMARY KEY (parent_id, child_id),
    CONSTRAINT "division_links_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON COLUMN division_links.unit_code IS 'Код юнита';
CREATE INDEX division_links_unit_code_idx ON division_links (unit_code);

--employee_status
CREATE TABLE employee_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    full_name          VARCHAR(512)                        NOT NULL,
    short_name         VARCHAR(256)                        NOT NULL,
    system_name        VARCHAR(64)                         NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_stake_free      boolean   DEFAULT FALSE,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT employee_status_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE employee_status IS 'Статусы состояния работников';
COMMENT ON COLUMN employee_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN employee_status.full_name IS 'Полное наименование';
COMMENT ON COLUMN employee_status.short_name IS 'Краткое наименование';
COMMENT ON COLUMN employee_status.system_name IS 'Наименование внутреннего системного статуса';
COMMENT ON COLUMN employee_status.external_id IS 'Внешний код записи';
COMMENT ON COLUMN employee_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN employee_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN employee_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN employee_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN employee_status.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_status.is_stake_free IS 'Признак свободной занятости, при которой не занимается штатная ставка';
COMMENT ON COLUMN employee_status.unit_code IS 'Код юнита';
CREATE INDEX employee_status_unit_code_idx ON employee_status (unit_code);

--employee_condition
CREATE TABLE employee_condition
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    employee_id        BIGINT,
    status_id          BIGINT,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "employee_condition_status_id_fk" FOREIGN KEY (status_id) REFERENCES employee_status (id),
    CONSTRAINT "employee_condition_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT employee_condition_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE employee_condition IS 'Каталог состояния работников';
COMMENT ON COLUMN employee_condition.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN employee_condition.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_condition.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN employee_condition.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN employee_condition.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN employee_condition.external_id IS 'Внешний код записи';
COMMENT ON COLUMN employee_condition.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN employee_condition.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN employee_condition.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_condition.unit_code IS 'Код юнита';
CREATE INDEX employee_condition_status_id_idx ON employee_condition (status_id);
CREATE INDEX employee_condition_employee_id_idx ON employee_condition (employee_id);
CREATE INDEX employee_condition_unit_code_idx ON employee_condition (unit_code);

--education_type
CREATE TABLE education_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR,
    external_id        VARCHAR(128) UNIQUE,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "education_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON COLUMN education_type.id IS 'Код образования';
COMMENT ON COLUMN education_type."name" IS 'Текстовое наименования';
COMMENT ON COLUMN education_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN education_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN education_type.unit_code IS 'Код юнита';
CREATE INDEX education_type_unit_code_idx ON education_type (unit_code);

--employee_education
CREATE TABLE employee_education
(
    id                         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    employee_id                BIGINT                              NOT NULL,
    education_type_id          BIGINT,
    education_institution_name VARCHAR(256),
    specialization             VARCHAR(256),
    year_graduation            VARCHAR(4),
    qualification              VARCHAR(256),
    update_date                TIMESTAMP,
    date_from                  TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                    TIMESTAMP,
    author_employee_id         BIGINT                              NOT NULL,
    update_employee_id         BIGINT                              NOT NULL,
    external_id                VARCHAR(128) UNIQUE,
    country_id                 VARCHAR(128),
    date_start                 TIMESTAMP,
    date_end                   TIMESTAMP,
    CONSTRAINT "employee_education_education_type_id_fk" FOREIGN KEY (education_type_id) REFERENCES education_type (id),
    CONSTRAINT "employee_education_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id)
);
COMMENT ON COLUMN employee_education.id IS 'Порядковый номер строки';
COMMENT ON COLUMN employee_education.employee_id IS 'ID пользователя по мэппингу из таблицы employee, где employee.external_id = employee_id ';
COMMENT ON COLUMN employee_education.education_type_id IS 'Код типа образования, из справочника education_type';
COMMENT ON COLUMN employee_education.education_institution_name IS 'Текстовое название образовательного учреждения';
COMMENT ON COLUMN employee_education.specialization IS 'Текстовое название специализации';
COMMENT ON COLUMN employee_education.year_graduation IS 'Год завершения обучения';
COMMENT ON COLUMN employee_education.qualification IS 'Текстовое название квалификации';
COMMENT ON COLUMN employee_education.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_education.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_education.date_to IS 'Дата закрытия записи (по умолчанию null)';
COMMENT ON COLUMN employee_education.author_employee_id IS 'Кто создал запись (тех.юзер, или юзер интеграции или null)';
COMMENT ON COLUMN employee_education.update_employee_id IS 'Кто обновил запись (тех.юзер, или юзер интеграции или null)';
COMMENT ON COLUMN employee_education.country_id IS 'Идентификатор страны';
COMMENT ON COLUMN employee_education.date_start IS 'Дата начала обучения';
COMMENT ON COLUMN employee_education.date_end IS 'Дата завершения обучения';
CREATE INDEX employee_education_education_type_id_idx ON employee_education (education_type_id);
CREATE INDEX employee_education_education_employee_id_idx ON employee_education (employee_id);

--academic_degree
CREATE TABLE academic_degree
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE academic_degree IS 'Ученые степени';
COMMENT ON COLUMN academic_degree.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN academic_degree.date_from IS 'Дата создания записи';
COMMENT ON COLUMN academic_degree.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN academic_degree."name" IS 'Наименование ученой степени';
COMMENT ON COLUMN academic_degree.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN academic_degree.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN academic_degree.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN academic_degree.external_id IS 'Внешний идентификатор';

--business_process_status
CREATE TABLE business_process_status
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name" VARCHAR(64)                         NOT NULL UNIQUE
);
COMMENT ON COLUMN business_process_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN business_process_status."name" IS 'Наименование';

--citizenship_integration
CREATE TABLE citizenship_integration
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"            VARCHAR(256)                        NOT NULL,
    abbreviation      VARCHAR(128),
    designation       VARCHAR(256),
    in_english        VARCHAR(256),
    responsible       VARCHAR(256),
    basis             VARCHAR(512),
    date_introduction TIMESTAMP,
    date_expiration   TIMESTAMP,
    last_modified     VARCHAR(256),
    reason_for_change VARCHAR(512)
);
COMMENT ON TABLE citizenship_integration IS 'Cправочник о гражданстве. Справочник позволяет интерпретировать информацию получаемую от заказчика по интеграции и выводить в той форме которая требуется (только код).';
COMMENT ON COLUMN citizenship_integration.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN citizenship_integration."name" IS 'Наименование';
COMMENT ON COLUMN citizenship_integration.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN citizenship_integration.designation IS 'Обозначение';
COMMENT ON COLUMN citizenship_integration.in_english IS 'По-английски';
COMMENT ON COLUMN citizenship_integration.responsible IS 'Ответственный';
COMMENT ON COLUMN citizenship_integration.basis IS 'Основание';
COMMENT ON COLUMN citizenship_integration.date_introduction IS 'Дата введения';
COMMENT ON COLUMN citizenship_integration.date_expiration IS 'Дата окончания';
COMMENT ON COLUMN citizenship_integration.last_modified IS 'Последнее изменение';
COMMENT ON COLUMN citizenship_integration.reason_for_change IS 'Основание изменения';

--contract_type
CREATE TABLE contract_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    description        VARCHAR(2048),
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "contract_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE contract_type IS 'Справочник типов договоров';
COMMENT ON COLUMN contract_type.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN contract_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN contract_type.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN contract_type."name" IS 'Наименование типа договора';
COMMENT ON COLUMN contract_type.description IS 'Описание типа договора';
COMMENT ON COLUMN contract_type.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN contract_type.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN contract_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN contract_type.external_id IS 'Внешний идентификатор';
COMMENT ON COLUMN contract_type.unit_code IS 'Юнит код, ссылается на таблицу unit';
CREATE INDEX contract_type_unit_code_idx ON contract_type (unit_code);

--country
CREATE TABLE country
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    short_name         VARCHAR(128)                        NOT NULL,
    full_name          VARCHAR(256),
    code_number        VARCHAR(128),
    code_alpha_2       VARCHAR(4)                          NOT NULL,
    code_alpha_3       VARCHAR(4)                          NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128)
);
COMMENT ON TABLE country IS 'Справочник стран';
COMMENT ON COLUMN country.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN country.date_from IS 'Дата создания записи';
COMMENT ON COLUMN country.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN country.short_name IS 'Наименование страны короткое';
COMMENT ON COLUMN country.full_name IS 'Наименование страны полное';
COMMENT ON COLUMN country.code_number IS 'Числовой код страны';
COMMENT ON COLUMN country.code_alpha_2 IS 'Строчной код страны (Альфа-2)';
COMMENT ON COLUMN country.code_alpha_3 IS 'Строчной код страны (Альфа-3)';
COMMENT ON COLUMN country.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN country.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN country.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN country.external_id IS 'Внешний идентификатор';

--dismissal_reason
CREATE TABLE dismissal_reason
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE dismissal_reason IS 'Справочник причин увольнения сотрудника';
COMMENT ON COLUMN dismissal_reason.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN dismissal_reason.date_from IS 'Дата создания записи';
COMMENT ON COLUMN dismissal_reason.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN dismissal_reason."name" IS 'Наименование причины увольнения';
COMMENT ON COLUMN dismissal_reason.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN dismissal_reason.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN dismissal_reason.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN dismissal_reason.external_id IS 'Внешний идентификатор';

--dismissal_type
CREATE TABLE dismissal_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE dismissal_type IS 'Справочник инициатив увольнения сотрудника';
COMMENT ON COLUMN dismissal_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN dismissal_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN dismissal_type.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN dismissal_type."name" IS 'Наименование инициативы';
COMMENT ON COLUMN dismissal_type.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN dismissal_type.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN dismissal_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN dismissal_type.external_id IS 'Внешний идентификатор';

--division_function
CREATE TABLE division_function
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    function_id        BIGINT                              NOT NULL,
    division_id        BIGINT                              NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "division_function_division_id_fk" FOREIGN KEY (division_id) REFERENCES division (id),
    CONSTRAINT "division_function_function_id_fk" FOREIGN KEY (function_id) REFERENCES function (id)
);
COMMENT ON TABLE division_function IS 'Каталог присвоения функций подразделениям';
COMMENT ON COLUMN division_function.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN division_function.function_id IS 'Ссылка на идентификатор function, функция';
COMMENT ON COLUMN division_function.division_id IS 'Ссылка на идентификатор division, подразделение';
COMMENT ON COLUMN division_function.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN division_function.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN division_function.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN division_function.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN division_function.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN division_function.update_date IS 'Дата обновления записи';
CREATE UNIQUE INDEX division_function_function_id_division_id_idx ON division_function USING btree (function_id, division_id);

--employee_academic_degree
CREATE TABLE employee_academic_degree
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "date"             TIMESTAMP                           NOT NULL,
    employee_id        BIGINT                              NOT NULL,
    academic_degree_id BIGINT                              NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    CONSTRAINT employee_academic_degreeacademic_degree_id_fkey FOREIGN KEY (academic_degree_id) REFERENCES academic_degree (id),
    CONSTRAINT employee_academic_degree_employee_id_fkey FOREIGN KEY (employee_id) REFERENCES employee (id)
);
COMMENT ON TABLE employee_academic_degree IS 'Данные об ученых степенях сотрудников';
COMMENT ON COLUMN employee_academic_degree.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN employee_academic_degree.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_academic_degree.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN employee_academic_degree."date" IS 'Дата получения ученой степени';
COMMENT ON COLUMN employee_academic_degree.employee_id IS 'Идентификатор сотрудника, ссылается на таблицу employee.id';
COMMENT ON COLUMN employee_academic_degree.academic_degree_id IS 'Идентификатор ученой степени, ссылается на таблицу academic_degree.id';
COMMENT ON COLUMN employee_academic_degree.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN employee_academic_degree.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN employee_academic_degree.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_academic_degree.external_id IS 'Внешний идентификатор';
CREATE INDEX employee_academic_degree_academic_degree_id_idx ON employee_academic_degree (academic_degree_id);
CREATE INDEX employee_academic_degree_employee_id_idx ON employee_academic_degree (employee_id);

--employee_contract
CREATE TABLE employee_contract
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY      NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    employee_id        BIGINT                                   NOT NULL,
    contract_type_id   BIGINT                                   NOT NULL,
    contract_sum       numeric(10, 2) DEFAULT 0.00,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    author_employee_id BIGINT                                   NOT NULL,
    update_employee_id BIGINT                                   NOT NULL,
    update_date        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    CONSTRAINT employee_contract_contract_type_id_fkey FOREIGN KEY (contract_type_id) REFERENCES contract_type (id),
    CONSTRAINT employee_contract_employee_id_fkey FOREIGN KEY (employee_id) REFERENCES employee (id)
);
COMMENT ON TABLE employee_contract IS 'Таблица данных об договорах с сотрудником';
COMMENT ON COLUMN employee_contract.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN employee_contract.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_contract.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN employee_contract.employee_id IS 'Идентификатор сотрудника, ссылается на таблицу employee';
COMMENT ON COLUMN employee_contract.contract_type_id IS 'Описание типа договора, ссылается на таблицу contract_type.id';
COMMENT ON COLUMN employee_contract.contract_sum IS 'Сумма договора';
COMMENT ON COLUMN employee_contract.date_start IS 'Дата начала договора';
COMMENT ON COLUMN employee_contract.date_end IS 'Дата окончания договора';
COMMENT ON COLUMN employee_contract.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN employee_contract.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN employee_contract.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_contract.external_id IS 'Внешний идентификатор';
CREATE INDEX employee_contract_contract_type_id_idx ON employee_contract (contract_type_id);
CREATE INDEX employee_contract_employee_id_idx ON employee_contract (employee_id);

--employee_deputy
CREATE TABLE employee_deputy
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    employee_vice       BIGINT,
    employee_substitute BIGINT,
    date_from           TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to             TIMESTAMP,
    CONSTRAINT "employee_deputy_employee_substitute_fk" FOREIGN KEY (employee_substitute) REFERENCES employee (id),
    CONSTRAINT "employee_deputy_employee_vice_fk" FOREIGN KEY (employee_vice) REFERENCES employee (id)
);
COMMENT ON TABLE employee_deputy IS 'Данные о заместителях';
CREATE INDEX employee_deputy_employee_substitute_idx ON employee_deputy (employee_substitute);
CREATE INDEX employee_deputy_employee_vice_idx ON employee_deputy (employee_vice);

--employee_dismissal
CREATE TABLE employee_dismissal
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to             TIMESTAMP,
    employee_id         BIGINT                              NOT NULL,
    dismissal_type_id   BIGINT                              NOT NULL,
    dismissal_reason_id BIGINT                              NOT NULL,
    comment_hr          VARCHAR(2048),
    comment_ruk         VARCHAR(2048),
    author_employee_id  BIGINT                              NOT NULL,
    update_employee_id  BIGINT                              NOT NULL,
    update_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id         VARCHAR(128) UNIQUE,
    unit_code           VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT employee_dismissaldismissal_reason_id_fkey FOREIGN KEY (dismissal_reason_id) REFERENCES dismissal_reason (id),
    CONSTRAINT employee_dismissal_dismissal_type_id_fkey FOREIGN KEY (dismissal_type_id) REFERENCES dismissal_type (id),
    CONSTRAINT employee_dismissal_employee_id_fkey FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT employee_dismissal_unit_code_fkey FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE employee_dismissal IS 'Данные о причинах увольнения сотрудника с возможностью указания комментария от руководителя и/или HR-а';
COMMENT ON COLUMN employee_dismissal.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN employee_dismissal.date_from IS 'Дата создания записи';
COMMENT ON COLUMN employee_dismissal.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN employee_dismissal.employee_id IS 'Идентификатор сотрудника, ссылается на таблицу employee';
COMMENT ON COLUMN employee_dismissal.dismissal_type_id IS 'Идентификатор типа инициативы увольнения, ссылается на таблицу dismissal_type';
COMMENT ON COLUMN employee_dismissal.dismissal_reason_id IS 'Идентификатор причины увольнения сотрудника, ссылается на таблицу dismissal_reason';
COMMENT ON COLUMN employee_dismissal.comment_hr IS 'Комментарий HR';
COMMENT ON COLUMN employee_dismissal.comment_ruk IS 'Комментарий Руководителя';
COMMENT ON COLUMN employee_dismissal.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN employee_dismissal.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN employee_dismissal.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN employee_dismissal.external_id IS 'Внешний идентификатор';
COMMENT ON COLUMN employee_dismissal.unit_code IS 'Юнит код, ссылается на таблицу unit';
CREATE INDEX employee_dismissal_dismissal_reason_id_idx ON employee_dismissal (dismissal_reason_id);
CREATE INDEX employee_dismissal_dismissal_type_id_idx ON employee_dismissal (dismissal_type_id);
CREATE INDEX employee_dismissal_employee_id_idx ON employee_dismissal (employee_id);
CREATE INDEX employee_dismissal_unit_code_idx ON employee_dismissal (unit_code);

--language
CREATE TABLE language
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE language IS 'Справочник языков';
COMMENT ON COLUMN language.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN language.date_from IS 'Дата создания записи';
COMMENT ON COLUMN language.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN language."name" IS 'Наименования языка';
COMMENT ON COLUMN language.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN language.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN language.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN language.external_id IS 'Внешний идентификатор';

--language_level
CREATE TABLE language_level
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE language_level IS 'Справочник уровня владения языком';
COMMENT ON COLUMN language_level.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN language_level.date_from IS 'Дата создания записи';
COMMENT ON COLUMN language_level.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN language_level."name" IS 'Наименование уровня владения языком';
COMMENT ON COLUMN language_level.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN language_level.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN language_level.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN language_level.external_id IS 'Внешний идентификатор';

--language_person
CREATE TABLE language_person
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    person_id          BIGINT                              NOT NULL,
    language_id        BIGINT                              NOT NULL,
    language_level_id  BIGINT                              NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    CONSTRAINT language_person_language_id_fkey FOREIGN KEY (language_id) REFERENCES language (id),
    CONSTRAINT language_person_language_level_id_fkey FOREIGN KEY (language_level_id) REFERENCES language_level (id),
    CONSTRAINT language_person_person_id_fkey FOREIGN KEY (person_id) REFERENCES person (id)
);
COMMENT ON TABLE language_person IS 'Данные об уровне владения языком физ.лиц';
COMMENT ON COLUMN language_person.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN language_person.date_from IS 'Дата создания записи';
COMMENT ON COLUMN language_person.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN language_person.person_id IS 'Идентификатор физ.лица, ссылается на таблицу person';
COMMENT ON COLUMN language_person.language_id IS 'Идентификатор языка, ссылается на таблицу language';
COMMENT ON COLUMN language_person.language_level_id IS 'Идентификатор уровня владения языком, ссылается на таблицу language_level';
COMMENT ON COLUMN language_person.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN language_person.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN language_person.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN language_person.external_id IS 'Внешний идентификатор';
CREATE INDEX language_person_language_id_idx ON language_person (language_id);
CREATE INDEX language_person_language_level_id_idx ON language_person (language_level_id);
CREATE INDEX language_person_person_id_idx ON language_person (person_id);

--legal_entity_team_assignment_conflict_roles
CREATE TABLE legal_entity_team_assignment_conflict_roles
(
    id                                              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    legal_entity_team_assignment_role_id_assigned   BIGINT,
    legal_entity_team_assignment_role_id_conflicted BIGINT                              NOT NULL,
    division_team_assignment_role_id_assigned       BIGINT,
    CONSTRAINT legal_entity_team_assignment_conflict_roles_all_id_key UNIQUE (legal_entity_team_assignment_role_id_assigned,
                                                                             legal_entity_team_assignment_role_id_conflicted,
                                                                             division_team_assignment_role_id_assigned),
    CONSTRAINT legal_entity_team_assignment_conflict_roles_dta_fkey FOREIGN KEY (division_team_assignment_role_id_assigned) REFERENCES role (id),
    CONSTRAINT legal_entity_team_assignment_conflict_roles_letaria_fkey FOREIGN KEY (legal_entity_team_assignment_role_id_assigned) REFERENCES role (id),
    CONSTRAINT legal_entity_team_assignment_conflict_roles_letaric_fkey FOREIGN KEY (legal_entity_team_assignment_role_id_conflicted) REFERENCES role (id)
);
COMMENT ON TABLE legal_entity_team_assignment_conflict_roles IS 'Конфликтные роли';
COMMENT ON COLUMN legal_entity_team_assignment_conflict_roles.legal_entity_team_assignment_role_id_assigned IS 'Назначенная работнику "назначаемая" роль в legal_entity_team_assignment';
COMMENT ON COLUMN legal_entity_team_assignment_conflict_roles.legal_entity_team_assignment_role_id_conflicted IS 'Роль, которую назначают через интерфейс "Управления ролями"';
COMMENT ON COLUMN legal_entity_team_assignment_conflict_roles.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN legal_entity_team_assignment_conflict_roles.division_team_assignment_role_id_assigned IS 'Назначенная работнику системная роль в division_team_role';
CREATE INDEX legal_entity_team_assignment_conflict_roles_dta_idx ON legal_entity_team_assignment_conflict_roles (division_team_assignment_role_id_assigned);
CREATE INDEX legal_entity_team_assignment_conflict_roles_letaria_idx ON legal_entity_team_assignment_conflict_roles (legal_entity_team_assignment_role_id_assigned);
CREATE INDEX legal_entity_team_assignment_conflict_roles_letaric_idx ON legal_entity_team_assignment_conflict_roles (legal_entity_team_assignment_role_id_conflicted);

--privilege
CREATE TABLE privilege
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    unit_code          VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "privilege_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON COLUMN privilege.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN privilege."name" IS 'Наименование';
COMMENT ON COLUMN privilege.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN privilege.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN privilege.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN privilege.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN privilege.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN privilege.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN privilege.unit_code IS 'Код юнита';
CREATE INDEX privilege_unit_code_idx ON privilege (unit_code);

--person_privilege
CREATE TABLE person_privilege
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    person_id          BIGINT,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    privilege_id       BIGINT                              NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "person_privilege_person_id_fk" FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT "person_privilege_privilege_id_fk" FOREIGN KEY (privilege_id) REFERENCES privilege (id)
);
COMMENT ON TABLE person_privilege IS 'Каталог льгот работника';
COMMENT ON COLUMN person_privilege.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN person_privilege.person_id IS 'Ссылка на идентификатор person, физические лица';
COMMENT ON COLUMN person_privilege.date_from IS 'Дата создания записи';
COMMENT ON COLUMN person_privilege.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN person_privilege.privilege_id IS 'Идентификатор льготы работника';
COMMENT ON COLUMN person_privilege.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN person_privilege.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN person_privilege.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN person_privilege.update_date IS 'Дата обновления записи';
CREATE INDEX person_privilege_person_id_idx ON person_privilege (person_id);
CREATE INDEX person_privilege_privilege_id_idx ON person_privilege (privilege_id);

--person_surname_change
CREATE TABLE person_surname_change
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    person_id          BIGINT                              NOT NULL,
    last_from          VARCHAR(128)                        NOT NULL,
    last_to            VARCHAR(128)                        NOT NULL,
    date               TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    CONSTRAINT "person_surname_change_person_id_fk" FOREIGN KEY (person_id) REFERENCES person (id)
);
COMMENT ON TABLE person_surname_change IS 'Данные о предыдущей фамилии физ.лица';
COMMENT ON COLUMN person_surname_change.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN person_surname_change.date_from IS 'Дата создания записи';
COMMENT ON COLUMN person_surname_change.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN person_surname_change.person_id IS 'Идентификатор физ.лица, ссылается на таблицу person';
COMMENT ON COLUMN person_surname_change.last_from IS 'Предыдущее фамилия физ.лица';
COMMENT ON COLUMN person_surname_change.last_to IS 'Новая фамилия физ.лица';
COMMENT ON COLUMN person_surname_change.date IS 'Дата смены фамилии';
COMMENT ON COLUMN person_surname_change.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN person_surname_change.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN person_surname_change.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN person_surname_change.external_id IS 'Внешний идентификатор';
CREATE INDEX person_surname_change_person_id_idx ON person_surname_change (person_id);

--person_work_experience
CREATE TABLE person_work_experience
(
    id                        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to                   TIMESTAMP,
    person_id                 BIGINT                              NOT NULL,
    date_start                TIMESTAMP,
    date_end                  TIMESTAMP,
    company_name              VARCHAR(256),
    is_current_company        boolean   DEFAULT FALSE             NOT NULL,
    job_title_name            VARCHAR(256),
    city                      VARCHAR(64),
    country_id                BIGINT,
    work_function_name        VARCHAR(256),
    work_function_description VARCHAR(1024),
    author_employee_id        BIGINT                              NOT NULL,
    update_employee_id        BIGINT                              NOT NULL,
    update_date               TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id               VARCHAR(128) UNIQUE,
    CONSTRAINT "person_work_experience_country_id_fk" FOREIGN KEY (country_id) REFERENCES country (id),
    CONSTRAINT "person_work_experience_person_id_fk" FOREIGN KEY (person_id) REFERENCES person (id)
);
COMMENT ON TABLE person_work_experience IS 'Данные о предыдущем опыте работы физ.лица';
COMMENT ON COLUMN person_work_experience.id IS 'Уникальный идентификатор';
COMMENT ON COLUMN person_work_experience.date_from IS 'Дата создания записи';
COMMENT ON COLUMN person_work_experience.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN person_work_experience.person_id IS 'Идентификатор физ.лица, ссылается на таблицу person.id';
COMMENT ON COLUMN person_work_experience.date_start IS 'Дата начала работы';
COMMENT ON COLUMN person_work_experience.date_end IS 'Дата окончания работы';
COMMENT ON COLUMN person_work_experience.company_name IS 'Наименование компании';
COMMENT ON COLUMN person_work_experience.is_current_company IS 'Флаг текущей компании';
COMMENT ON COLUMN person_work_experience.job_title_name IS 'Наименование должности';
COMMENT ON COLUMN person_work_experience.city IS 'Город';
COMMENT ON COLUMN person_work_experience.country_id IS 'Идентификатор страны, ссылается на таблицу country.id';
COMMENT ON COLUMN person_work_experience.work_function_name IS 'Наименование рабочей функции/отрасли';
COMMENT ON COLUMN person_work_experience.work_function_description IS 'Описание должностных обязанностей';
COMMENT ON COLUMN person_work_experience.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN person_work_experience.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN person_work_experience.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN person_work_experience.external_id IS 'Внешний идентификатор';
CREATE INDEX person_work_experience_country_id_idx ON person_work_experience (country_id);
CREATE INDEX person_work_experience_person_id_idx ON person_work_experience (person_id);

--position_group_position
CREATE TABLE position_group_position
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    position_id       BIGINT                              NOT NULL,
    position_group_id BIGINT                              NOT NULL,
    date_from         TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to           TIMESTAMP,
    CONSTRAINT "position_group_position_position_group_id_fk" FOREIGN KEY (position_group_id) REFERENCES position_group (id),
    CONSTRAINT "position_group_position_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id)
);
COMMENT ON COLUMN position_group_position.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_group_position.position_id IS 'Ссылка на идентификатор position, позиция';
COMMENT ON COLUMN position_group_position.position_group_id IS 'Ссылка на идентификатор position_group, справочник групп должностей';
COMMENT ON COLUMN position_group_position.date_from IS 'Дата создания записи';
COMMENT ON COLUMN position_group_position.date_to IS 'Дата окончания записи';
CREATE INDEX position_group_position_position_group_id_idx ON position_group_position (position_group_id);
CREATE INDEX position_group_position_position_id_idx ON position_group_position (position_id);

--position_importance_reason_group
CREATE TABLE position_importance_reason_group
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"        VARCHAR(256)                        NOT NULL,
    description   VARCHAR(256)                        NOT NULL,
    is_changeable BOOLEAN DEFAULT FALSE               NOT NULL -- TODO update entity
);
COMMENT ON COLUMN position_importance_reason_group.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN position_importance_reason_group."name" IS 'Наименование';
COMMENT ON COLUMN position_importance_reason_group.description IS 'Описание';

--position_profstandard
CREATE TABLE position_profstandard
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    code        VARCHAR(128)                        NOT NULL,
    date_from   TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to     TIMESTAMP,
    position_id BIGINT                              NOT NULL,
    CONSTRAINT "position_profstandard_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id)
);
COMMENT ON TABLE position_profstandard IS 'Связь должности с профстандартами';
COMMENT ON COLUMN position_profstandard.id IS 'Уникальный индентификатор записи';
COMMENT ON COLUMN position_profstandard.code IS 'ИД записи из таблицы pst_profstandard БД rostalent';
COMMENT ON COLUMN position_profstandard.date_from IS 'Системная дата о начале действия записи';
COMMENT ON COLUMN position_profstandard.date_to IS 'Системная дата об окончании действия записи';
COMMENT ON COLUMN position_profstandard.position_id IS 'ИД записи из таблицы position';
CREATE INDEX position_profstandard_position_id_idx ON position_profstandard (position_id);

--product_status
CREATE TABLE product_status
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name" VARCHAR(64) UNIQUE                  NOT NULL
);
COMMENT ON COLUMN product_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN product_status."name" IS 'Наименование';

--project_status
CREATE TABLE project_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_status IS 'Статусы проекта';
COMMENT ON COLUMN project_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_status."name" IS 'Наименование';
COMMENT ON COLUMN project_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_status.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN project_status.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN project_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_status.update_date IS 'Дата обновления записи';

--project_type
CREATE TABLE project_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name"             VARCHAR(64) UNIQUE                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_type IS 'Типы проектов';
COMMENT ON COLUMN project_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_type."name" IS 'Наименование';
COMMENT ON COLUMN project_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_type.date_from IS 'Системная дата о появлении записи';
COMMENT ON COLUMN project_type.date_to IS 'Системная дата о закрытии записи';
COMMENT ON COLUMN project_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_type.update_date IS 'Дата обновления записи';

--project
CREATE TABLE project
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    parent_id          BIGINT,
    date_from          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    date_start_confirm TIMESTAMP,
    date_end_confirm   TIMESTAMP,
    type_id            BIGINT                                  NOT NULL, -- TODO update entity
    full_name          VARCHAR(256)                            NOT NULL,
    short_name         VARCHAR(128)                            NOT NULL,
    abbreviation       VARCHAR(64)                             NOT NULL,
    description        VARCHAR(1024) DEFAULT ''::VARCHAR       NOT NULL,
    status_id          BIGINT                                  NOT NULL, -- TODO update entity
    code               VARCHAR(128),
    external_id        VARCHAR(128) UNIQUE,
    cost_center_id     BIGINT,
    author_employee_id BIGINT                                  NOT NULL,
    update_employee_id BIGINT                                  NOT NULL,
    update_date        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    legal_entity_id    BIGINT,                                           -- todo update entity
    CONSTRAINT "project_cost_center_id_fk" FOREIGN KEY (cost_center_id) REFERENCES cost_center (id),
    CONSTRAINT "project_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES project (id),
    CONSTRAINT "project_status_id_fk" FOREIGN KEY (status_id) REFERENCES project_status (id),
    CONSTRAINT "project_type_id_fk" FOREIGN KEY (type_id) REFERENCES project_type (id),
    CONSTRAINT "project_legal_entity_id_fk" FOREIGN KEY (legal_entity_id) REFERENCES legal_entity (id)
);
COMMENT ON TABLE project IS 'Каталог проектов';
COMMENT ON COLUMN project.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN project.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project.date_start IS 'Запланированная дата начала';
COMMENT ON COLUMN project.date_end IS 'Запланированная дата закрытие';
COMMENT ON COLUMN project.date_start_confirm IS 'Подтверждённая дата старта';
COMMENT ON COLUMN project.date_end_confirm IS 'Подтверждённая дата окончания';
COMMENT ON COLUMN project.type_id IS 'Идентификатор типа проекта';
COMMENT ON COLUMN project.full_name IS 'Полное наименование';
COMMENT ON COLUMN project.short_name IS 'Краткое наименование';
COMMENT ON COLUMN project.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN project.description IS 'Описание';
COMMENT ON COLUMN project.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN project.code IS 'Системный код записи';
COMMENT ON COLUMN project.external_id IS 'Внешний код записи';
COMMENT ON COLUMN project.cost_center_id IS 'Ссылка на идентификатор cost_center, центр затрат';
COMMENT ON COLUMN project.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN project.legal_entity_id IS 'Ссылается на ID в таблице legal_entity';
CREATE INDEX project_cost_center_id_idx ON project (cost_center_id);
CREATE INDEX project_parent_id_idx ON project (parent_id);
CREATE INDEX project_status_id_idx ON project (status_id);
CREATE INDEX project_type_id_idx ON project (type_id);
CREATE INDEX project_full_name_idx ON project (full_name);
CREATE INDEX project_legal_entity_id_idx ON project (legal_entity_id);

--project_team_status
CREATE TABLE project_team_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_team_status IS 'Статусы команды проекта';
COMMENT ON COLUMN project_team_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_status.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_status.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_status."name" IS 'Наименование';
COMMENT ON COLUMN project_team_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_team_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_status.update_date IS 'Дата обновления записи';

--project_team_type
CREATE TABLE project_team_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_team_type IS 'Типы команд проекта';
COMMENT ON COLUMN project_team_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_type.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_type."name" IS 'Наименование';
COMMENT ON COLUMN project_team_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_team_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_type.update_date IS 'Дата обновления записи';

--project_team
CREATE TABLE project_team
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    parent_id          BIGINT,
    date_from          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    project_id         BIGINT                                  NOT NULL,
    type_id            BIGINT                                  NOT NULL,
    full_name          VARCHAR(256)                            NOT NULL,
    short_name         VARCHAR(128)                            NOT NULL,
    abbreviation       VARCHAR(64)                             NOT NULL,
    description        VARCHAR(1024) DEFAULT ''::VARCHAR       NOT NULL,
    status_id          BIGINT                                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    date_start         TIMESTAMP,
    date_end           TIMESTAMP,
    author_employee_id BIGINT                                  NOT NULL,
    update_employee_id BIGINT                                  NOT NULL,
    update_date        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "project_team_parent_id_fk" FOREIGN KEY (parent_id) REFERENCES project_team (id),
    CONSTRAINT "project_team_project_id_fk" FOREIGN KEY (project_id) REFERENCES project (id),
    CONSTRAINT "project_team_status_id_fk" FOREIGN KEY (status_id) REFERENCES project_team_status (id),
    CONSTRAINT "project_team_type_id_fk" FOREIGN KEY (type_id) REFERENCES project_team_type (id)
);
COMMENT ON TABLE project_team IS 'Каталог команда проектов';
COMMENT ON COLUMN project_team.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team.parent_id IS 'Ссылка на уникальный идентификатор записи из данной таблицы (родительская запись)';
COMMENT ON COLUMN project_team.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team.project_id IS 'Ссылка на идентификатор project, проект';
COMMENT ON COLUMN project_team.type_id IS 'Идентификатор типа проектной команды';
COMMENT ON COLUMN project_team.full_name IS 'Полное наименование';
COMMENT ON COLUMN project_team.short_name IS 'Краткое наименование';
COMMENT ON COLUMN project_team.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN project_team.description IS 'Описание';
COMMENT ON COLUMN project_team.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN project_team.external_id IS 'Внешний код записи';
COMMENT ON COLUMN project_team.date_start IS 'Дата начала фазы';
COMMENT ON COLUMN project_team.date_end IS 'Дата окончания фазы';
COMMENT ON COLUMN project_team.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team.update_date IS 'Дата обновления записи';
CREATE INDEX project_team_parent_id_idx ON project_team (parent_id);
CREATE INDEX project_team_project_id_idx ON project_team (project_id);
CREATE INDEX project_team_status_id_idx ON project_team (status_id);
CREATE INDEX project_team_type_id_idx ON project_team (type_id);

--project_team_role_status
CREATE TABLE project_team_role_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_team_role_status IS 'Статусы должностей команды проекта';
COMMENT ON COLUMN project_team_role_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_role_status.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_role_status.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_role_status."name" IS 'Наименование';
COMMENT ON COLUMN project_team_role_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_team_role_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_role_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_role_status.update_date IS 'Дата обновления записи';

--project_team_role_type
CREATE TABLE project_team_role_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_team_role_type IS 'Типы должностей команды проекта';
COMMENT ON COLUMN project_team_role_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_role_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_role_type.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_role_type."name" IS 'Наименование';
COMMENT ON COLUMN project_team_role_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_team_role_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_role_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_role_type.update_date IS 'Дата обновления записи';

--project_team_role
CREATE TABLE project_team_role
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY     NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    project_team_id    BIGINT                                  NOT NULL,
    role_id            BIGINT                                  NOT NULL,
    type_id            BIGINT                                  NOT NULL,
    full_name          VARCHAR(256)                            NOT NULL,
    short_name         VARCHAR(128)                            NOT NULL,
    abbreviation       VARCHAR(64)                             NOT NULL,
    description        VARCHAR(1024) DEFAULT ''::VARCHAR       NOT NULL,
    status_id          BIGINT                                  NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    plan_fte           numeric(5, 2) DEFAULT 0.00              NOT NULL,
    author_employee_id BIGINT                                  NOT NULL,
    update_employee_id BIGINT                                  NOT NULL,
    update_date        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "project_team_role_project_team_id_fk" FOREIGN KEY (project_team_id) REFERENCES project_team (id),
    CONSTRAINT "project_team_role_status_id_fk" FOREIGN KEY (status_id) REFERENCES project_team_role_status (id),
    CONSTRAINT "project_team_role_type_id_fk" FOREIGN KEY (type_id) REFERENCES project_team_role_type (id),
    CONSTRAINT "project_team_role_role_id_fk" FOREIGN KEY (role_id) REFERENCES role (id)
);
COMMENT ON COLUMN project_team_role.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_role.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_role.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_role.project_team_id IS 'Ссылка на идентификатор project_team, проектная команда';
COMMENT ON COLUMN project_team_role.role_id IS 'Ссылка на идентификатор role, роли';
COMMENT ON COLUMN project_team_role.type_id IS 'Идентификатор типа роли в проектной команде';
COMMENT ON COLUMN project_team_role.full_name IS 'Полное наименование';
COMMENT ON COLUMN project_team_role.short_name IS 'Краткое наименование';
COMMENT ON COLUMN project_team_role.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN project_team_role.description IS 'Описание';
COMMENT ON COLUMN project_team_role.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN project_team_role.external_id IS 'Внешний код записи';
COMMENT ON COLUMN project_team_role.plan_fte IS 'FTE позиции';
COMMENT ON COLUMN project_team_role.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_role.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_role.update_date IS 'Дата обновления записи';
CREATE INDEX project_team_role_project_team_id_idx ON project_team_role (project_team_id);
CREATE INDEX project_team_role_status_id_idx ON project_team_role (status_id);
CREATE INDEX project_team_role_type_id_idx ON project_team_role (type_id);
CREATE INDEX project_team_role_role_id_idx ON project_team_role (role_id);

--project_team_assignment_status
CREATE TABLE project_team_assignment_status
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_team_assignment_status IS 'Статусы назначения в команду проекта';
COMMENT ON COLUMN project_team_assignment_status.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_assignment_status.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_assignment_status.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_assignment_status."name" IS 'Наименование';
COMMENT ON COLUMN project_team_assignment_status.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_team_assignment_status.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_assignment_status.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_assignment_status.update_date IS 'Дата обновления записи';

--project_team_assignment_type
CREATE TABLE project_team_assignment_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(128)                        NOT NULL,
    external_id        VARCHAR(128) UNIQUE,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
COMMENT ON TABLE project_team_assignment_type IS 'Типы назначения в команду проекта';
COMMENT ON COLUMN project_team_assignment_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_assignment_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_assignment_type.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_assignment_type."name" IS 'Наименование';
COMMENT ON COLUMN project_team_assignment_type.external_id IS 'Уникальный идентификатор внешней системы';
COMMENT ON COLUMN project_team_assignment_type.author_employee_id IS 'Идентификатор пользователя, создавшего запись';
COMMENT ON COLUMN project_team_assignment_type.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_assignment_type.update_date IS 'Дата обновления записи';

--project_team_assignment
CREATE TABLE project_team_assignment
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    precursor_id         BIGINT,
    date_from            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to              TIMESTAMP,
    type_id              BIGINT                              NOT NULL,
    project_team_role_id BIGINT                              NOT NULL,
    employee_id          BIGINT                              NOT NULL,
    full_name            VARCHAR(256)                        NOT NULL,
    short_name           VARCHAR(128)                        NOT NULL,
    abbreviation         VARCHAR(64)                         NOT NULL,
    status_id            BIGINT                              NOT NULL,
    external_id          VARCHAR(128) UNIQUE,
    percent_fte          BIGINT,
    comment_hr           VARCHAR(512),
    plan_employment_date TIMESTAMP,
    author_employee_id   BIGINT                              NOT NULL,
    update_employee_id   BIGINT                              NOT NULL,
    update_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT "project_team_assignment_precursor_id_fk" FOREIGN KEY (precursor_id) REFERENCES project_team_assignment (id),
    CONSTRAINT "project_team_assignment_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "project_team_assignment_project_team_role_id_fk" FOREIGN KEY (project_team_role_id) REFERENCES project_team_role (id),
    CONSTRAINT "project_team_assignment_status_id_fk" FOREIGN KEY (status_id) REFERENCES project_team_assignment_status (id),
    CONSTRAINT "project_team_assignment_type_id_fk" FOREIGN KEY (type_id) REFERENCES project_team_assignment_type (id)
);
COMMENT ON COLUMN project_team_assignment.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN project_team_assignment.precursor_id IS 'Ссылка на предшествующую запись';
COMMENT ON COLUMN project_team_assignment.date_from IS 'Дата создания записи';
COMMENT ON COLUMN project_team_assignment.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN project_team_assignment.type_id IS 'Идентификатор типа назначения в проектную команду';
COMMENT ON COLUMN project_team_assignment.project_team_role_id IS 'Идентификатор роли проектной команды';
COMMENT ON COLUMN project_team_assignment.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN project_team_assignment.full_name IS 'Полное наименование';
COMMENT ON COLUMN project_team_assignment.short_name IS 'Краткое наименование';
COMMENT ON COLUMN project_team_assignment.abbreviation IS 'Аббревиатура';
COMMENT ON COLUMN project_team_assignment.status_id IS 'Ссылка на идентификатор status, статус';
COMMENT ON COLUMN project_team_assignment.external_id IS 'Внешний код записи';
COMMENT ON COLUMN project_team_assignment.percent_fte IS 'Процент занятости';
COMMENT ON COLUMN project_team_assignment.comment_hr IS 'Комментарий HR';
COMMENT ON COLUMN project_team_assignment.plan_employment_date IS 'Предполагаемая дата вывода работника';
COMMENT ON COLUMN project_team_assignment.author_employee_id IS 'Идентификатор работника, сделавшего назначение';
COMMENT ON COLUMN project_team_assignment.update_employee_id IS 'Идентификатор пользователя, обновившего запись';
COMMENT ON COLUMN project_team_assignment.update_date IS 'Дата обновления записи';
CREATE INDEX project_team_assignment_precursor_id_idx ON project_team_assignment (precursor_id);
CREATE INDEX project_team_assignment_employee_id_idx ON project_team_assignment (employee_id);
CREATE INDEX project_team_assignment_project_team_role_id_idx ON project_team_assignment (project_team_role_id);
CREATE INDEX project_team_assignment_status_id_idx ON project_team_assignment (status_id);
CREATE INDEX project_team_assignment_type_id_idx ON project_team_assignment (type_id);

--project_team_role_position
CREATE TABLE project_team_role_position
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from            TIMESTAMP                           NOT NULL,
    date_to              TIMESTAMP,
    project_team_role_id BIGINT                              NOT NULL,
    position_id          BIGINT                              NOT NULL,
    external_id          VARCHAR(128) UNIQUE,
    author_employee_id   BIGINT,
    update_employee_id   BIGINT,
    update_date          TIMESTAMP,
    CONSTRAINT "project_team_role_position_position_id_fk" FOREIGN KEY (position_id) REFERENCES position (id),
    CONSTRAINT "project_team_role_position_project_team_role_id_fk" FOREIGN KEY (project_team_role_id) REFERENCES project_team_role (id)
);
COMMENT ON TABLE project_team_role_position IS 'Таблица для связи запланированных позиций с пришедшими позициями по интеграции';
COMMENT ON COLUMN project_team_role_position.id IS 'Идентификатор';
COMMENT ON COLUMN project_team_role_position.date_from IS 'Дата начала';
COMMENT ON COLUMN project_team_role_position.date_to IS 'Дата окончания';
COMMENT ON COLUMN project_team_role_position.project_team_role_id IS 'Идентификатор роли в команде';
COMMENT ON COLUMN project_team_role_position.position_id IS 'Идентификатор позиции';
COMMENT ON COLUMN project_team_role_position.external_id IS 'Внешний код записи';
COMMENT ON COLUMN project_team_role_position.author_employee_id IS 'Идентификатор создавшего пользователя';
COMMENT ON COLUMN project_team_role_position.update_employee_id IS 'Идентификатор пользователя последнего изменения';
COMMENT ON COLUMN project_team_role_position.update_date IS 'Дата и время последнего изменения';
CREATE INDEX project_team_role_position_position_id_idx ON project_team_role_position (position_id);
CREATE INDEX project_team_role_position_project_team_role_id_idx ON project_team_role_position (project_team_role_id);

--relationship_contact
CREATE TABLE relationship_contact
(
    id                   BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from            TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to              TIMESTAMP,
    person_id            BIGINT                              NOT NULL,
    relationship_type_id BIGINT,
    full_name            VARCHAR(128),
    phone                VARCHAR(32),
    email                VARCHAR(64),
    address              VARCHAR(256),
    messenger            VARCHAR(128),
    author_employee_id   BIGINT                              NOT NULL,
    update_employee_id   BIGINT                              NOT NULL,
    update_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id          VARCHAR(128) UNIQUE,
    home_phone           VARCHAR(128),
    CONSTRAINT "relationship_contact_person_id_fk" FOREIGN KEY (person_id) REFERENCES person (id)
);
COMMENT ON TABLE relationship_contact IS 'Данные экстремальных контактов';
COMMENT ON COLUMN relationship_contact.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN relationship_contact.date_from IS 'Дата создания записи';
COMMENT ON COLUMN relationship_contact.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN relationship_contact.person_id IS 'Идентификатор физ.лица, ссылается на таблицу person';
COMMENT ON COLUMN relationship_contact.relationship_type_id IS 'Идентификатор степень родства, ссылается на таблицу relationship_type';
COMMENT ON COLUMN relationship_contact.full_name IS 'Полное имя экстренного контакта';
COMMENT ON COLUMN relationship_contact.phone IS 'Телефон экстренного контакта';
COMMENT ON COLUMN relationship_contact.email IS 'Почтовый адрес экстренного контакта';
COMMENT ON COLUMN relationship_contact.address IS 'Адрес экстренного контакта';
COMMENT ON COLUMN relationship_contact.messenger IS 'Мессенджер экстренного контакта';
COMMENT ON COLUMN relationship_contact.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN relationship_contact.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN relationship_contact.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN relationship_contact.external_id IS 'Внешний идентификатор';
COMMENT ON COLUMN relationship_contact.home_phone IS 'Домашний телефон контакта';
CREATE INDEX relationship_contact_person_id_idx ON relationship_contact (person_id);

--relationship_type
CREATE TABLE relationship_type
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to            TIMESTAMP,
    "name"             VARCHAR(256)                        NOT NULL,
    author_employee_id BIGINT                              NOT NULL,
    update_employee_id BIGINT                              NOT NULL,
    update_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    external_id        VARCHAR(128) UNIQUE
);
COMMENT ON TABLE relationship_type IS 'Справочников степеней родства';
COMMENT ON COLUMN relationship_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN relationship_type.date_from IS 'Дата создания записи';
COMMENT ON COLUMN relationship_type.date_to IS 'Дата удаления записи';
COMMENT ON COLUMN relationship_type."name" IS 'Наименование степени родства';
COMMENT ON COLUMN relationship_type.author_employee_id IS 'Автор записи';
COMMENT ON COLUMN relationship_type.update_employee_id IS 'Автор последнего обновления записи';
COMMENT ON COLUMN relationship_type.update_date IS 'Дата обновления записи';
COMMENT ON COLUMN relationship_type.external_id IS 'Внешний идентификатор';

--slot_rules
CREATE TABLE slot_rules
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    module      VARCHAR(100)                        NOT NULL,
    slot_id     VARCHAR(200)                        NOT NULL,
    rule_id     BIGINT                              NOT NULL,
    left_rule   VARCHAR(200)                        NOT NULL,
    lr_ds_or_kv VARCHAR(20)                         NOT NULL,
    operator    VARCHAR(20)                         NOT NULL,
    right_rule  VARCHAR(200)                        NOT NULL,
    rr_ds_or_kv VARCHAR(20)                         NOT NULL,
    is_enabled  boolean DEFAULT TRUE                NOT NULL
);
COMMENT ON COLUMN slot_rules.module IS 'Модуль';
COMMENT ON COLUMN slot_rules.slot_id IS 'Слот/поле в форме';
COMMENT ON COLUMN slot_rules.rule_id IS 'Порядковый номер';
COMMENT ON COLUMN slot_rules.left_rule IS 'Левое правило';
COMMENT ON COLUMN slot_rules.lr_ds_or_kv IS 'Левый операнд';
COMMENT ON COLUMN slot_rules.operator IS 'Оператор правила';
COMMENT ON COLUMN slot_rules.right_rule IS 'Правое правило';
COMMENT ON COLUMN slot_rules.rr_ds_or_kv IS 'Правый операнд';
COMMENT ON COLUMN slot_rules.is_enabled IS 'Вкл/выкл';
CREATE INDEX slot_rules_slot_id_idx ON slot_rules (slot_id);
CREATE INDEX slot_rules_rule_id_idx ON slot_rules (rule_id);
CREATE INDEX slot_rules_module_slot_id_idx ON slot_rules (module, slot_id);

--work_award_type
CREATE TABLE work_award_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    "name"    VARCHAR(128)                        NOT NULL,
    unit_code VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "work_award_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON COLUMN work_award_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_award_type."name" IS 'Наименование';
COMMENT ON COLUMN work_award_type.unit_code IS 'Код юнита';
CREATE INDEX work_award_type_unit_code_idx ON work_award_type (unit_code);

--work_award
CREATE TABLE work_award
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    date_from     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_to       TIMESTAMP,
    type_id       BIGINT                              NOT NULL,
    employee_id   BIGINT                              NOT NULL,
    date_start    TIMESTAMP                           NOT NULL,
    decree_number VARCHAR(128)                        NOT NULL,
    decree_date   TIMESTAMP                           NOT NULL,
    CONSTRAINT "work_award_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "work_award_type_id_fk" FOREIGN KEY (type_id) REFERENCES work_award_type (id)
);
COMMENT ON COLUMN work_award.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_award.date_from IS 'Дата создания записи';
COMMENT ON COLUMN work_award.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN work_award.type_id IS 'Идентификатор типа награды';
COMMENT ON COLUMN work_award.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN work_award.date_start IS 'Запланированная дата начала';
COMMENT ON COLUMN work_award.decree_number IS 'Номер приказа';
COMMENT ON COLUMN work_award.decree_date IS 'Дата приказа';
CREATE INDEX work_award_employee_id_idx ON work_award (employee_id);
CREATE INDEX work_award_type_id_idx ON work_award (type_id);

--work_week_type
CREATE TABLE work_week_type
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY, -- TODO update entity
    "name"    VARCHAR(64)                         NOT NULL,
    unit_code VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT work_week_type_name_unit_code_key UNIQUE ("name", unit_code),
    CONSTRAINT "work_week_type_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);

COMMENT ON TABLE work_week_type IS 'Типы рабочей недели';
COMMENT ON COLUMN work_week_type.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_week_type."name" IS 'Наименование';
COMMENT ON COLUMN work_week_type.unit_code IS 'Код юнита';
CREATE INDEX work_week_type_unit_code_idx ON work_week_type (unit_code);

--work_condition
CREATE TABLE work_condition
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    break_duration_minutes BIGINT                              NOT NULL, -- todo update entity
    date_from              TIMESTAMP DEFAULT NOW()             NOT NULL,
    date_to                TIMESTAMP DEFAULT NOW()             NOT NULL,
    is_has_watch           BOOLEAN   DEFAULT FALSE             NOT NULL,-- todo update entity
    is_irregular_hours     BOOLEAN   DEFAULT FALSE             NOT NULL, -- todo update entity
    salary                 real                                NOT NULL,
    shift_count            real                                NOT NULL,
    work_duration_minutes  BIGINT                              NOT NULL, -- todo update entity
    employee_id            BIGINT,
    personnel_document_id  BIGINT,
    work_week_type_id      BIGINT,                                       -- TODO update entity
    unit_code              VARCHAR(128)                        NOT NULL DEFAULT 'default',
    CONSTRAINT "work_condition_employee_id_fk" FOREIGN KEY (employee_id) REFERENCES employee (id),
    CONSTRAINT "work_condition_personnel_document_id_fk" FOREIGN KEY (personnel_document_id) REFERENCES personnel_document (id),
    CONSTRAINT "work_condition_work_week_type_id_fk" FOREIGN KEY (work_week_type_id) REFERENCES work_week_type (id),
    CONSTRAINT "work_condition_unit_code_fk" FOREIGN KEY (unit_code) REFERENCES unit (code)
);
COMMENT ON TABLE work_condition IS 'Каталог рабочего времени';
COMMENT ON COLUMN work_condition.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN work_condition.break_duration_minutes IS 'Перерыв ( в минутах)';
COMMENT ON COLUMN work_condition.date_from IS 'Дата создания записи';
COMMENT ON COLUMN work_condition.date_to IS 'Дата окончания записи';
COMMENT ON COLUMN work_condition.is_has_watch IS 'Часы работы';
COMMENT ON COLUMN work_condition.is_irregular_hours IS 'Ненормированные часы';
COMMENT ON COLUMN work_condition.salary IS 'Оплата';
COMMENT ON COLUMN work_condition.shift_count IS 'Кол-во ставок';
COMMENT ON COLUMN work_condition.work_duration_minutes IS 'Рабочее время (минуты)';
COMMENT ON COLUMN work_condition.employee_id IS 'Ссылка на идентификатор employee, работник';
COMMENT ON COLUMN work_condition.personnel_document_id IS 'Ссылка на идентификатор personnel_document, документы физического лица';
COMMENT ON COLUMN work_condition.work_week_type_id IS 'Идентификатор рабочей недели';
COMMENT ON COLUMN work_condition.unit_code IS 'Код юнита';
CREATE INDEX work_condition_employee_id_idx ON work_condition (employee_id);
CREATE INDEX work_condition_personnel_document_id_idx ON work_condition (personnel_document_id);
CREATE INDEX work_condition_work_week_type_id_idx ON work_condition (work_week_type_id);
CREATE INDEX work_condition_unit_code_idx ON work_condition (unit_code);

