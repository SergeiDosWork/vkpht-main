package me.goodt.nsi

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class SpecificationBuilder<T> {
    fun createSpecification(filter: Filter?): Specification<T> {
        return Specification { root: Root<T>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            if (filter is FilterCondition) {
                val condition = filter
                val field = condition.field
                val value = condition.value
                val operator = condition.operator
                var predicate: Predicate? = null
                when (operator) {
                    "like" -> predicate = criteriaBuilder.like(root.get(field), "%$value%")
                    "eq" -> predicate = criteriaBuilder.equal(root.get<Any>(field), value)
                    "neq" -> predicate = criteriaBuilder.notEqual(root.get<Any>(field), value)
                    "lt" -> predicate = criteriaBuilder.lt(root.get(field), value?.toBigDecimal())
                    "gt" -> predicate = criteriaBuilder.gt(root.get(field), value?.toBigDecimal())
                }
                return@Specification predicate
            } else if (filter is FilterGroup) {
                val group = filter
                val predicates: MutableList<Predicate> = ArrayList()
                for (subFilter in group.conditions) {
                    predicates.add(createSpecification(subFilter).toPredicate(root, query, criteriaBuilder))
                }
                if ("and".equals(group.operator, ignoreCase = true)) {
                    return@Specification criteriaBuilder.and(*predicates.toTypedArray())
                } else if ("or".equals(group.operator, ignoreCase = true)) {
                    return@Specification criteriaBuilder.or(*predicates.toTypedArray())
                }
            }
            null
        }
    }
}
