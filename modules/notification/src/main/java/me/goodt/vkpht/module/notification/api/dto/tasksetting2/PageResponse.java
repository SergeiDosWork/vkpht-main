package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private Integer totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer size;
    private List<T> data;
}
