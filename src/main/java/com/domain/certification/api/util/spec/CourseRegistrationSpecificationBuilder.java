package com.domain.certification.api.util.spec;

import com.domain.certification.api.data.CourseRegistration;
import com.domain.certification.api.util.SearchCriteria;
import com.domain.certification.api.util.enumerator.CourseResult;
import com.domain.certification.api.util.enumerator.SearchOperation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class CourseRegistrationSpecificationBuilder {

    private final List<SearchCriteria> params;

    public CourseRegistrationSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public CourseRegistrationSpecificationBuilder with(String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }

            switch (key) {
                case "result":
                    value = CourseResult.valueOf((String) value);
                    break;
            }

            params.add(new SearchCriteria(key, op, value));
        }

        return this;
    }

    public Specification<CourseRegistration> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<CourseRegistration>> commonSpecs = new ArrayList<>();

        for (SearchCriteria param : params) {
            commonSpecs.add(new CourseRegistrationSpecification(param));
        }

        Specification<CourseRegistration> result = null;
        if (commonSpecs.size() > 0) {
            result = commonSpecs.get(0);
            for (int i = 1; i < commonSpecs.size(); i++) {
                result = Specifications.where(result).and(commonSpecs.get(i));
            }
        }

        result = Specifications.where(result);
        return result;
    }
}
