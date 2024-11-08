package me.goodt.vkpht.module.orgstructure.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class CurrentUnitRequest {

    @NotEmpty
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]*$",
            message = "не является допустимым значением бизнес-единицы (юнита)"
    )
    @Schema(description = "Код бизнес-единицы (юнита)")
    private String unitCode;
}
