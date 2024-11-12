package me.goodt.vkpht.module.notification.application.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;

@UtilityClass
public class DataUtils {

	private static final char PLUS = '+';
	private static final char MINUS = '-';
	private static final String MALE = "m";
	private static final String FEMALE = "f";
	private static final String DEAR_MALE = "Уважаемый";
	private static final String DEAR_FEMALE = "Уважаемая";

	/**
	 * The method returns name of employee by format: Surname Name Patronymic
	 *
	 * @param employee employee data
	 * @return full name by format above
	 */
	public static String getEmployeeFullName(EmployeeInfoDto employee) {
		return Stream.of(employee.getLastName(), employee.getFirstName(), employee.getMiddleName())
			.filter(StringUtils::isNotEmpty)
			.map(StringUtils::trim)
			.collect(Collectors.joining(" "));
	}

	/**
	 * Метод возвращает имя employee в формате: Фамилия И.О.
	 *
	 * @param employee employee data
	 * @return full name by format above
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

	/**
	 * Метод возвращает имя employee в формате: Фамилия Имя
	 *
	 * @param employee employee data
	 * @return name by format above
	 */
	public static String getEmployeeSurnameAndName(EmployeeInfoDto employee) {
		return Stream.of(employee.getLastName(), employee.getFirstName())
			.filter(StringUtils::isNotEmpty)
			.map(StringUtils::trim)
			.collect(Collectors.joining(" "));
	}

	/**
	 * The method returns greeting of employee by format: DEAR_MALE Surname Name Patronymic
	 *
	 * @param employee employee data
	 * @return Greeting by format above
	 */
	public static String getGreetingEmployeeFullName(EmployeeExtendedInfoDto employee) {
		String greetingEmployeeFullName = getEmployeeFullName(employee);

		if (employee.getPerson().getSex().equalsIgnoreCase(MALE)) {
			greetingEmployeeFullName = "%s %s".formatted(DEAR_MALE, greetingEmployeeFullName);
		}
		if (employee.getPerson().getSex().equalsIgnoreCase(FEMALE)) {
			greetingEmployeeFullName = "%s %s".formatted(DEAR_FEMALE, greetingEmployeeFullName);
		}

		return greetingEmployeeFullName;
	}

	/**
	 * The method returns greeting of employee by format: Surname N.P.
	 *
	 * @param employee employee data
	 * @return Greeting by format above
	 */
	public static String getGreetingEmployeeShorName(EmployeeExtendedInfoDto employee) {
		String greetingEmployeeShortName = getEmployeeShortName(employee);

		if (employee.getPerson().getSex().equalsIgnoreCase(MALE)) {
			greetingEmployeeShortName = "%s %s".formatted(DEAR_MALE, greetingEmployeeShortName);
		}
		if (employee.getPerson().getSex().equalsIgnoreCase(FEMALE)) {
			greetingEmployeeShortName = "%s %s".formatted(DEAR_FEMALE, greetingEmployeeShortName);
		}

		return greetingEmployeeShortName;
	}

	/**
	 * The method processes Date object by pattern
	 *
	 * @param pattern pattern
	 * @param date    Date object
	 * @return date as string by pattern above
	 */
	public static String getStringDateByPattern(String pattern, Date date) {
		try {
			return new SimpleDateFormat(pattern).format(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * The method processes Date object by pattern
	 *
	 * @param pattern pattern
	 * @param date    string date
	 * @return date from string by pattern above
	 */
	public static Date getDateByPattern(String pattern, String date) {
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * The method returns a signed number that depends on the input expression
	 *
	 * @param expression input expression containing numbers
	 * @return signed number
	 */
	public static int extractNumberOfDays(String expression) {
		try {
			if (expression.charAt(0) == PLUS) {
				return Integer.parseInt(expression.substring(1));
			}
			if (expression.charAt(0) == MINUS) {
				return -Integer.parseInt(expression.substring(1));
			}
			return 0;
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * The method changes the input date depending on input amount
	 *
	 * @param currentDate date to be changed
	 * @param amount      amount days
	 * @return changed date
	 */
	public static Date getChangedDate(Date currentDate, int field, int amount) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(field, amount);
			return calendar.getTime();
		} catch (Exception ex) {
			return null;
		}
	}

	public static String mapToString(Map<String, Object> tokenValues) {
		try {
			return tokenValues.entrySet().stream()
				.map(e -> "'%s'='%s'".formatted(
					StringUtils.defaultString(e.getKey(), "null"),
					Objects.toString(e.getValue(), "null")))
				.collect(Collectors.joining(",", "{", "}"));
		} catch (Exception e) {
			return "[]";
		}
	}
}
