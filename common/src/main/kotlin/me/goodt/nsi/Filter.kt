package me.goodt.nsi

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import lombok.Getter
import lombok.Setter

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "operator")
@JsonSubTypes(JsonSubTypes.Type(value = FilterCondition::class, name = "condition"), JsonSubTypes.Type(value = FilterGroup::class, name = "group"))
@Getter
@Setter
abstract class Filter {
    var operator: String? = null
}
