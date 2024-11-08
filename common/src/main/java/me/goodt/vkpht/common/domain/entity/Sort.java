package me.goodt.vkpht.common.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.goodt.vkpht.common.api.dto.Order;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sort {
    private String columnName;
    private Order order;
}


