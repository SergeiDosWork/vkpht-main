package me.goodt.vkpht.module.orgstructure.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date birthDate;
    private Long familyStatusId;
    private String sex;
    private Long parentId;
    private Long spouseId;
    private String photo;
    private String snils;
    private String inn;
    private String address;
    private String phone;
    private String email;
    private String externalId;
    private Long citizenshipId;
}
