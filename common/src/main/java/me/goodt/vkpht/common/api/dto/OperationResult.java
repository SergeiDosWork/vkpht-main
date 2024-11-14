package me.goodt.vkpht.common.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OperationResult {
    private final Object result;
    private final String message;
}
