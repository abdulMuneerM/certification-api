package com.domain.certification.api.repository;

import com.domain.certification.api.data.CourseRegistration;
import com.domain.certification.api.util.enumerator.CourseResult;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRegistrationRepository extends PagingAndSortingRepository<CourseRegistration, Long>,
        JpaSpecificationExecutor<CourseRegistration> {

    CourseRegistration findByCourseIdAndUserIdAndResult(Long courseId, String userId, CourseResult result);
}
