package com.domain.certification.api.repository;

import com.domain.certification.api.data.Course;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long>, JpaSpecificationExecutor<Course>  {

    Course findByName(String name);

    Course findByIdNotAndName(Long id, String name);
}
