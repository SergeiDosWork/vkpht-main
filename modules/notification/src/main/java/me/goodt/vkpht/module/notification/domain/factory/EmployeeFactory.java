package me.goodt.vkpht.module.notification.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeePersonDto;

@UtilityClass
public class EmployeeFactory {

	public static EmployeeInfoDto create(EmployeePersonDto employeePerson) {
		return new EmployeeInfoDto(
			employeePerson.getId(),
			employeePerson.getExternalId(),
			employeePerson.getPerson().getName(),
			employeePerson.getPerson().getSurname(),
			employeePerson.getPerson().getPatronymic(),
			employeePerson.getPerson().getPhoto(),
			employeePerson.getNumber(),
			employeePerson.getEmail()
		);
	}
}
