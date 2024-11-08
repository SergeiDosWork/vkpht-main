package me.goodt.vkpht.common.application.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public final class CoreUtils {

    public static Pageable getPageable(Integer page, Integer size) {
        if (page == null || page < 0 || size == null || size <= 0) {
            return Pageable.unpaged();
        } else {
            return PageRequest.of(page, size, Sort.by("id").ascending());
        }
    }

    public static Pageable asPageable(int page, int size, String sortBy, Sort.Direction sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = Sort.by(sortDirection, sortBy);
        }
        return PageRequest.of(page, size, sort);
    }
}
