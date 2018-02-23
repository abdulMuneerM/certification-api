package com.domain.certification.api.service;

import com.domain.certification.api.data.CourseRegistration;
import com.domain.certification.api.util.dto.Course.CourseRegistrationDTO;
import com.domain.certification.api.util.enumerator.CourseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRegistrationService {

    CourseRegistration findOne(Long id);

    CourseRegistration save(CourseRegistrationDTO courseRegistrationDTO);

    CourseRegistration updateCourseResult(Long courseRegistrationId, CourseResult result);

    Page<CourseRegistration> findAll(String filter, Pageable pageable);
}
