package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.passport.PersonInfoDto;

/**
 * Сервис для получения информации о физическом лице с учетом всех сотрудников,
 * которые принадлежат этому физическому лицу.
 * Методы, предоставляемые сервисом, работают в разрезе ВСЕХ юнитов, никаких проверок
 * на юнитозависимость не производится.
 */
public interface EmployeePassportService {

    /**
     * Получает информацию о физическом лице по уникальному идентификатору сотрудника.
     *
     * @param employeeId           уникальный идентификатор сотрудника.
     * @param includeSecondaryInfo флаг, указывающий, нужно ли включать дополнительную информацию.
     * @return информация о физическом лице.
     */
    PersonInfoDto getPersonInfo(Long employeeId, boolean includeSecondaryInfo);

    /**
     * Получает информацию о физическом лице по СНИЛС (страховой номер индивидуального лицевого счета).
     *
     * @param snils                СНИЛС физического лица, по которому будет осуществлен поиск.
     * @param includeSecondaryInfo флаг, указывающий, нужно ли включать дополнительную информацию.
     * @return список объектов с информацией о физическом лице. Пустой список, если такие данные не найдены.
     */
    List<PersonInfoDto> getPersonInfo(String snils, boolean includeSecondaryInfo);
}
