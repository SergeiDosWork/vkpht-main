package me.goodt.vkpht.module.orgstructure.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.entity.*;

public class EmployeeSpecification implements Specification<EmployeeEntity> {

    private static final String EMPLOYEE = "employee";
    private static final String POSITION = "position";
    private static final String ID = "id";

    private final List<SearchCriteria> list;

    public EmployeeSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<EmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));

            } else if (criteria.getOperation().equals(SearchOperation.IN_LIST_DIVISION_ID)) {
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<PositionAssignmentEntity> subRoot = subQuery.from(PositionAssignmentEntity.class);
                Join<PositionAssignmentEntity, PositionEntity> join = subRoot.join(POSITION);
                Predicate predicate = criteriaBuilder.isTrue(join.get("division").in((List<?>) criteria.getValue()));
                subQuery.select(subRoot.get(EMPLOYEE)).where(predicate);
                predicates.add(criteriaBuilder.isTrue(root.get(ID).in(subQuery)));

            } else if (criteria.getOperation().equals(SearchOperation.IN_LIST_FUNCTION_ID)) {
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<PositionAssignmentEntity> subRoot = subQuery.from(PositionAssignmentEntity.class);
                Join<PositionAssignmentEntity, PositionEntity> join = subRoot.join(POSITION);
                Predicate predicate = criteriaBuilder.isTrue(join.get("workFunction").in((List<?>) criteria.getValue()));
                subQuery.select(subRoot.get(EMPLOYEE)).where(predicate);
                predicates.add(criteriaBuilder.isTrue(root.get(ID).in(subQuery)));

            } else if (criteria.getOperation().equals(SearchOperation.EQUAL_JOB_TITLE_ID)) {
                Long value = (Long) criteria.getValue();
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<PositionAssignmentEntity> subRoot = subQuery.from(PositionAssignmentEntity.class);
                Join<PositionAssignmentEntity, PositionEntity> joinPosition = subRoot.join(POSITION);
                Join<PositionEntity, JobTitleEntity> joinJobTitle = joinPosition.join("jobTitle");
                Predicate predicate = criteriaBuilder.equal(joinJobTitle.get(ID), value);
                subQuery.select(subRoot.get(EMPLOYEE)).where(predicate);
                predicates.add(criteriaBuilder.isTrue(root.get(ID).in(subQuery)));

            } else if ((criteria.getOperation().equals(SearchOperation.IN_LIST_LEGAL_ENTITY_ID))) {
                Subquery<Long> subQuery = query.subquery(Long.class);
                Root<PositionAssignmentEntity> subRoot = subQuery.from(PositionAssignmentEntity.class);
                Join<PositionAssignmentEntity, PositionEntity> joinPos = subRoot.join(POSITION);
                Join<PositionEntity, DivisionEntity> join2 = joinPos.join("division");
                Predicate predicate = criteriaBuilder.isTrue(join2.get("legalEntityEntity").in((List<?>) criteria.getValue()));
                subQuery.select(subRoot.get(EMPLOYEE)).where(predicate);
                predicates.add(criteriaBuilder.isTrue(root.get(ID).in(subQuery)));

            } else if (criteria.getOperation().equals(SearchOperation.IN_VALUE)) {
                List<Predicate> predicatesTemp = new ArrayList<>();
                List<Predicate> predicates1 = new ArrayList<>();
                Join<EmployeeEntity, PersonEntity> employeePerson = root.join("person");
                String[] searchValue = ((String) criteria.getValue()).toLowerCase().split(" ");
                for (String value : searchValue) {
                    predicates1.clear();
                    predicates1.add(criteriaBuilder.like(criteriaBuilder.lower(employeePerson.get("surname")), "%" + value + "%"));
                    predicates1.add(criteriaBuilder.like(criteriaBuilder.lower(employeePerson.get("name")), "%" + value + "%"));
                    predicates1.add(criteriaBuilder.like(criteriaBuilder.lower(employeePerson.get("patronymic")), "%" + value + "%"));
                    predicates1.add(criteriaBuilder.like(root.get("number"), "%" + value + "%"));
                    predicatesTemp.add(criteriaBuilder.or(predicates1.toArray(Predicate[]::new)));
                }
                predicates.add(criteriaBuilder.and(predicatesTemp.toArray(Predicate[]::new)));

            } else if (criteria.getOperation().equals(SearchOperation.IN_VALUE_NO_PATR)) {
                List<Predicate> predicatesTemp = new ArrayList<>();
                List<Predicate> predicates1 = new ArrayList<>();
                Join<EmployeeEntity, PersonEntity> employeePerson = root.join("person");
                String[] searchValue = ((String) criteria.getValue()).toLowerCase().split(" ");
                for (String value : searchValue) {
                    predicates1.clear();
                    predicates1.add(criteriaBuilder.like(criteriaBuilder.lower(employeePerson.get("surname")), "%" + value + "%"));
                    predicates1.add(criteriaBuilder.like(criteriaBuilder.lower(employeePerson.get("name")), "%" + value + "%"));
                    predicates1.add(criteriaBuilder.like(root.get("number"), "%" + value + "%"));
                    predicatesTemp.add(criteriaBuilder.or(predicates1.toArray(Predicate[]::new)));
                }
                predicates.add(criteriaBuilder.and(predicatesTemp.toArray(Predicate[]::new)));
            }
        }
        root.fetch("person", JoinType.LEFT);
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
