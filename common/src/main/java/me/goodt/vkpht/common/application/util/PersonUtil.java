package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.goodt.drive.rtcore.dto.orgstructure.EmployeeInfoDto;
import com.goodt.drive.rtcore.model.orgstructure.entities.PersonEntity;

@UtilityClass
public class PersonUtil {
    public static String getPhoto(PersonEntity personEntity) {
        String photo = null;
        if (personEntity != null) {
            photo = personEntity.getPhoto() != null
                ? personEntity.getPhoto()
                : (personEntity.getSurname() != null && personEntity.getName() != null)
                ? String.format("%s%s", personEntity.getSurname().charAt(0), personEntity.getName().charAt(0))
                : null;
        }
        return photo;
    }

    public static String getPhotoOrInitials(String photo, String surname, String name) {
        if (photo != null) {
            return photo;
        }
        return String.format("%s%s", getInitials(surname), getInitials(name));
    }

    public static String getFullNameInitialsLast(String surname, String name, String patronymic) {
        return String.format(
            "%s %s%s",
            StringUtils.isEmpty(surname) ? "" : surname,
            getInitials(name),
            getInitials(patronymic)
        );
    }

    public static String getFullName(String surname, String name, String patronymic) {
        return Stream.of(surname, name, patronymic)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(" "));
    }

    private static String getInitials(String string) {
        if (StringUtils.isEmpty(string)) {
            return "";
        } else {
            return string.toUpperCase().charAt(0) + ".";
        }
    }

    /**
     * Метод возвращает имя сотрудника в формате: Фамилия Имя Отчество
     */
    public static String getEmployeeFullName(EmployeeInfoDto employee) {
        return Stream.of(employee.getLastName(), employee.getFirstName(), employee.getMiddleName())
            .filter(StringUtils::isNotEmpty)
            .map(StringUtils::trim)
            .collect(Collectors.joining(" "));
    }

    /**
     * Метод возвращает имя employee в формате: Фамилия И.О.
     */
    public static String getEmployeeShortName(EmployeeInfoDto employee) {
        return getEmployeeShortName(employee.getLastName(), employee.getFirstName(), employee.getMiddleName());
    }

    public static String getEmployeeShortName(String lastName, String firstName, String middleName) {
        String initials = Stream.of(firstName, middleName)
            .filter(StringUtils::isNotEmpty)
            .map(StringUtils::trim)
            .map(full -> StringUtils.abbreviate(full, ".", 2))
            .collect(Collectors.joining(""));

        return Stream.of(lastName, initials)
            .filter(StringUtils::isNotEmpty)
            .map(StringUtils::trim)
            .collect(Collectors.joining(" "));
    }
}
