package me.goodt.nsi

import lombok.Getter
import lombok.Setter

class FilterGroup : Filter() {
    var conditions: List<Filter> = emptyList()
}
