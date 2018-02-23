package com.domain.certification.api.repository;

import com.domain.certification.api.data.CourseRegistration;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRegistrationRepository extends PagingAndSortingRepository<CourseRegistration, Long>,
        JpaSpecificationExecutor<CourseRegistration> {
}
