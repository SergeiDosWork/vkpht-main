package me.goodt.vkpht.module.notification.application.impl;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientEmailDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientParameterEntity;

/**
 * Агрегированная информация о получателях уведомления с методами для упрощенного получения данных.
 */
@Slf4j
public class NotificationRecipientAggregate {

	private final Collection<NotificationRecipientEntity> recipients;
	private final List<NotificationRecipientParameterEntity> parameters;
	private final Map<Long, EmployeeInfoDto> employees;
	private final Map<Long, DivisionDto> divisions;
    private final Map<Long, List<NotificationRecipientEmailDto>> emails;

	public NotificationRecipientAggregate(Collection<NotificationRecipientEntity> recipients,
										  List<NotificationRecipientParameterEntity> parameters,
										  List<EmployeeInfoDto> employees,
										  List<DivisionDto> divisions,
                                          List<NotificationRecipientEmailDto> emails) {
		this.recipients = recipients;
		this.parameters = parameters;

		this.employees = new HashMap<>(employees.size());
		for (EmployeeInfoDto employee : employees) {
			this.employees.put(employee.getId(), employee);
		}

		this.divisions = new HashMap<>(divisions.size());
		for (DivisionDto division : divisions) {
			this.divisions.put(division.getId(), division);
		}

        this.emails = emails.stream().collect(Collectors.groupingBy(NotificationRecipientEmailDto::getNotificationRecipientId));
	}

	public Set<Long> getParameterValues(NotificationRecipientType type) {
		Set<Long> values = new HashSet<>();
		for (NotificationRecipientParameterEntity parameter : this.parameters) {
			if (type.getName().equals(parameter.getParent().getName())) {
				values.add(parameter.getValue());
			}
		}

		return values;
	}

	public List<NotificationRecipientParameterEntity> getParameters(Long recipientId) {
		List<NotificationRecipientParameterEntity> result = new ArrayList<>();
		for (NotificationRecipientParameterEntity parameter : this.parameters) {
			if (recipientId.equals(parameter.getParent().getId())) {
				result.add(parameter);
			}
		}
		return result;
	}

    public List<NotificationRecipientEmailDto> getEmails(Long recipientId) {
        return this.emails.get(recipientId);
    }

	public EmployeeInfoDto getEmployee(Long employeeId) {
		return this.employees.get(employeeId);
	}

	public DivisionDto getDivision(Long divisionId) {
		return this.divisions.get(divisionId);
	}

	public List<EmployeeInfoDto> getStaticEmployees(Long recipientId) {
		List<NotificationRecipientParameterEntity> parameters = getParameters(recipientId);
		if (parameters == null || parameters.isEmpty()) {
			log.warn("Отсутствуют параметры для получатель ID={} c токеном STATIC_EMPLOYEE", recipientId);
			return Collections.emptyList();
		}

		List<EmployeeInfoDto> employees = new ArrayList<>(parameters.size());
		for (NotificationRecipientParameterEntity parameter : parameters) {
			EmployeeInfoDto employee = this.employees.get(parameter.getValue());
			if (employee == null) {
				log.warn("Сотрудник ID={}, указанный в параметрах получателя ID={} не получен из orgstructure",
						 parameter.getValue(), recipientId);
				continue;
			}
			employees.add(employee);
		}

		return employees;
	}

	public List<DivisionDto> getStaticDivisions(Long recipientId) {
		List<NotificationRecipientParameterEntity> parameters = getParameters(recipientId);
		if (parameters == null || parameters.isEmpty()) {
			return Collections.emptyList();
		}

		List<DivisionDto> divisions = new ArrayList<>(parameters.size());
		for (NotificationRecipientParameterEntity parameter : parameters) {
			DivisionDto division = this.divisions.get(parameter.getValue());
			if (division == null) {
				log.warn("Подразделение ID={}, указанное в параметрах получателя ID={} не получено из orgstructure",
						 parameter.getValue(), recipientId);
				continue;
			}
			divisions.add(division);
		}

		return divisions;
	}
}
