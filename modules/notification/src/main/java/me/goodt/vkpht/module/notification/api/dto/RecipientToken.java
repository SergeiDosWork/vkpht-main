package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class RecipientToken {
    @NonNull
    private String name;
    @NonNull
    private String description;
    private List<RecipientToken> specialCases = new ArrayList<>();
}
