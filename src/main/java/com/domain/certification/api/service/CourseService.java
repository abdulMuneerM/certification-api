package com.domain.certification.api.service;

import com.domain.certification.api.data.Course;
import com.domain.certification.api.util.dto.Course.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    Course findOne(Long id);

    Course save(CourseDTO courseDTO);

    Course update(Long id, CourseDTO courseDTO);

    Page<Course> findAll(String searchFilter, Pageable pageable);
}
