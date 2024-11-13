package me.goodt.vkpht.module.notification.api.dto.rtcore;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalCommissionRoleDto {
    private Long id;
    @NotNull
    @Size(max = 256)
    private String name;
    @JsonSetter(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
}
