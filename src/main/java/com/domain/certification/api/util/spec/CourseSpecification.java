package com.domain.certification.api.util.spec;

import com.domain.certification.api.data.Course;
import com.domain.certification.api.util.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CourseSpecification implements Specification<Course> {

    private SearchCriteria criteria;

    public CourseSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Expression<String> key = root.get(criteria.getKey());

        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(key, criteria.getValue());
            case NEGATION:
                return builder.notEqual(key, criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(key, criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(key, criteria.getValue().toString());
            case LIKE:
                return builder.like(key, criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(key, criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(key, "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(key, "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}
