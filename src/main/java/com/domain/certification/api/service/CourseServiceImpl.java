package com.domain.certification.api.service;

import com.domain.certification.api.data.Course;
import com.domain.certification.api.exception.AuthorizationRequiredException;
import com.domain.certification.api.exception.DuplicateEntityException;
import com.domain.certification.api.exception.EntityNotFoundException;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.repository.CourseRegistrationRepository;
import com.domain.certification.api.repository.CourseRepository;
import com.domain.certification.api.security.SecurityHelper;
import com.domain.certification.api.util.CoreService;
import com.domain.certification.api.util.dto.Course.CourseDTO;
import com.domain.certification.api.util.spec.CourseSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final UserService userService;
    private final CoreService coreService;

    public CourseServiceImpl(CourseRepository courseRepository, CourseRegistrationRepository courseRegistrationRepository,
                             UserService userService, CoreService coreService) {
        this.courseRepository = courseRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.userService = userService;
        this.coreService = coreService;
    }

    @Transactional(readOnly = true)
    @Override
    public Course findOne(Long id) {
        if (StringUtils.isEmpty(id)) {
            LOG.error("id is empty. Please enter valid id.");
            throw new UnProcessableEntityException("id is empty. Please enter valid id.");
        }

        Course course = courseRepository.findOne(id);
        if (course == null) {
            LOG.error("Course does not exist with id {}", id);
            throw new EntityNotFoundException("Course does not exist with id " + id);
        }

        return course;
    }

    @Transactional
    @Override
    public Course save(CourseDTO courseDTO) {
        if (!SecurityHelper.isSignedInUserAdmin()) {
            LOG.info("Current user don't have access to create course");
            throw new AuthorizationRequiredException("Access denied.");
        }

        Course course = courseRepository.findByName(courseDTO.getName());
        if (course != null) {
            LOG.error("Course already exists {}", course.getName());
            throw new DuplicateEntityException("Course already exists.");
        }

        course = new Course();
        course.setName(courseDTO.getName());
        course.setCost(courseDTO.getCost());
        course.setCreatedAt(coreService.getCurrentEpochSeconds());
        course.setLastModifiedAt(coreService.getCurrentEpochSeconds());
        return courseRepository.save(course);
    }

    @Override
    public Course update(Long id, CourseDTO courseDTO) {
        if (!SecurityHelper.isSignedInUserAdmin()) {
            LOG.info("Current user don't have access to create course");
            throw new AuthorizationRequiredException("Access denied.");
        }

        Course course = findOne(id);

        if (courseRepository.findByIdNotAndName(id, courseDTO.getName()) != null) {
            LOG.error("Course already exists {}", course.getName());
            throw new DuplicateEntityException("Course already exists.");
        }

        course.setName(courseDTO.getName());
        course.setCost(courseDTO.getCost());
        course.setLastModifiedAt(coreService.getCurrentEpochSeconds());
        return courseRepository.save(course);
    }

    @Override
    public Page<Course> findAll(String searchFilter, Pageable pageable) {
        CourseSpecificationBuilder builder = new CourseSpecificationBuilder();
        Matcher matcher = coreService.searchPatternMatcher(searchFilter);
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        Specification<Course> spec = builder.build();

        Page<Course> courses;
        try {
            courses = courseRepository.findAll(spec, pageable);
        } catch (InvalidDataAccessApiUsageException e) {
            LOG.error("Exception: {}", e);
            throw new UnProcessableEntityException("Could not validate request");
        }

        return courses;
    }
}
