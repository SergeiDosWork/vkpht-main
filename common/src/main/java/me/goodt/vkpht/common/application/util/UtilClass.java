package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.PageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.filter.FilterDto;
import com.goodt.drive.rtcore.dto.tasksetting2.input.PageRequest;
import com.goodt.drive.rtcore.model.tasksetting2.entities.BaseIdentityEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@UtilityClass
public class UtilClass {

    /**
     * Метод превращает Фамилию Имя и Отчество в Фамилию И.О.
     * @param lastName Фамилия
     * @param firstName Имя
     * @param middleName Отчество
     * @return Фамилия И.О.
     */
	public static String getFIOFromEmployee(String lastName, String firstName, String middleName) {
        if (StringUtils.hasLength(middleName)) {
            return String.format("%s %s.%s.", lastName,
                firstName.charAt(0),
                middleName.charAt(0));

        } else
            return String.format("%s %s.", lastName, firstName.charAt(0));	}

    /**
     * Метод проверяет входящую строку
     *
     * @param str входящая строка
     * @return true - строка не является числом, false - строка является числом
     */
    public static boolean isString(String str) {
        boolean isString = false;
        for (char ch : str.toCharArray()) {
            if (Character.isLetter(ch)) {
                isString = true;
            }
        }
        return isString;
    }

    public static <T> Collector<T, ?, List<T>> toSortedList(Comparator<? super T> c) {
        return Collectors.collectingAndThen(
                Collectors.toCollection(ArrayList::new), l -> {
                    l.sort(c);
                    return l;
                });
    }

	public static <T extends BaseIdentityEntity> Map<Long, T> toMapTaskSetting2(Collection<T> collection) {
		return collection.stream().collect(Collectors.toMap(T::getId, Function.identity()));
	}

    public static <T extends DomainObject> Map<Long, T> toMap(Collection<T> collection) {
        return collection.stream().collect(Collectors.toMap(T::getId, Function.identity()));
    }

    /**
     * Метод вычисляет симметрическую разницу двух множеств
     * @param set1 множество 1
     * @param set2 множество 2
     * @param <T> объект, содержащийся в множестве
     * @return симметрическая разница
     */
    public static <T> Set<T> simmetricDifference(Set<T> set1, Set<T> set2) {
        Set<T> newSet = new HashSet<>(set1);
        newSet.removeAll(set2);
        set2.removeAll(set1);
        newSet.addAll(set2);
        return newSet;
    }

    public static <T, U extends com.goodt.drive.rtcore.dto.tasksetting2.input.PageRequest> PageResponse<T> wrapWithPageResponse(U request,
                                                                                                                                List<T> data, Comparator<T> comparator) {
        List<T> content = data.stream()
            .skip((long) (request.getPage()) * request.getSize())
            .limit(request.getSize())
            .collect(Collectors.toList());

        if (comparator != null) {
            content.sort(comparator);
        }

        int totalElements = data.size();
        return new PageResponse<>(
            totalElements,
            totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / request.getSize()),
            request.getPage(),
            content.size(),
            content
        );
    }

    public static <T, U extends com.goodt.drive.rtcore.dto.tasksetting2.input.PageRequest> PageResponse<T> wrapWithPageResponse(U request, List<T> data) {
        return wrapWithPageResponse(request, data, null);
    }

    public <T> FilterAwarePageResponse<T> wrapWithFilterAwarePageResponse(PageRequest request,
                                                                          List<T> data,
                                                                          List<FilterDto> filters) {
        PageResponse<T> pageResponse = wrapWithPageResponse(request, data);
        var response = new FilterAwarePageResponse<>(pageResponse);
        response.setFilters(filters);
        return response;
    }

    public static String roundAsString(Double value, int places) {
        if (value == null) {
            return null;
        }
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        StringBuilder pattern = new StringBuilder("#");
        if (places > 0) {
            pattern.append(".");
            pattern.append("#".repeat(places));
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern.toString(), formatSymbols);
        return decimalFormat.format(value);
    }

    public static Double round(Double value, int places) {
        if (value == null) {
            return null;
        }
        return Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
    }

    public static boolean between(LocalDate date, LocalDate start, LocalDate end, boolean inclusive) {
        if (start == null || end == null) {
            return false;
        }
        if (inclusive) {
            if ((date.isAfter(start) || date.equals(start))
                && (date.isBefore(end) || date.equals(end))) {
                return true;
            }
        } else {
            if (date.isAfter(start) && date.isBefore(end)) {
                return true;
            }
        }
        return false;
    }
}
