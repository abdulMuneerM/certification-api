package com.domain.certification.api.service;

import com.domain.certification.api.data.Course;
import com.domain.certification.api.data.CourseRegistration;
import com.domain.certification.api.data.User;
import com.domain.certification.api.exception.EntityNotFoundException;
import com.domain.certification.api.exception.ForbiddenException;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.repository.CourseRegistrationRepository;
import com.domain.certification.api.util.CoreService;
import com.domain.certification.api.util.dto.Course.CourseRegistrationDTO;
import com.domain.certification.api.util.enumerator.CourseResult;
import com.domain.certification.api.util.spec.CourseRegistrationSpecificationBuilder;
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
public class CourseRegistrationServiceImpl implements CourseRegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseRegistrationServiceImpl.class);
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final CoreService coreService;

    public CourseRegistrationServiceImpl(CourseRegistrationRepository courseRegistrationRepository,
                                         CourseService courseService, UserService userService, CoreService coreService) {
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.coreService = coreService;
    }

    @Transactional(readOnly = true)
    @Override
    public CourseRegistration findOne(Long id) {
        if (StringUtils.isEmpty(id)) {
            LOG.error("id is empty. Please enter valid id.");
            throw new UnProcessableEntityException("id is empty. Please enter valid id.");
        }

        CourseRegistration courseRegistration = courseRegistrationRepository.findOne(id);
        if (courseRegistration == null) {
            LOG.error("Course registration does not exist with id {}", id);
            throw new EntityNotFoundException("Course registration does not exist with id " + id);
        }

        return courseRegistration;
    }

    @Transactional
    @Override
    public CourseRegistration save(CourseRegistrationDTO courseRegistrationDTO) {
        Course course = courseService.findOne(courseRegistrationDTO.getCourseId());
        User user = userService.findOne(courseRegistrationDTO.getUserId());

        CourseRegistration courseRegistration =
                courseRegistrationRepository.findByCourseIdAndUserIdAndResult(courseRegistrationDTO.getCourseId(), courseRegistrationDTO.getUserId(), CourseResult.PASSED);
        if (courseRegistration != null) {
            LOG.error("User with id " + courseRegistrationDTO.getUserId() + " have already competed the course with id " + courseRegistrationDTO.getCourseId());
            throw new UnProcessableEntityException("User have already competed the course.");
        }

        courseRegistration = new CourseRegistration();
        courseRegistration.setCourse(course);
        courseRegistration.setUser(user);
        courseRegistration.setResult(null);
        courseRegistration.setCreatedAt(coreService.getCurrentEpochSeconds());
        courseRegistration.setLastModifiedAt(coreService.getCurrentEpochSeconds());
        return courseRegistrationRepository.save(courseRegistration);
    }

    @Transactional
    @Override
    public CourseRegistration updateCourseResult(Long courseRegistrationId, CourseResult result) {
        CourseRegistration courseRegistration = courseRegistrationRepository.findOne(courseRegistrationId);
        if (courseRegistration == null) {
            LOG.error("course registration does not exist with id {}", courseRegistrationId);
            throw new EntityNotFoundException("course registration does not exist with id " + courseRegistrationId);
        }

        if (CourseResult.PASSED == courseRegistration.getResult()) {
            LOG.error("Cannot modify the course result, its already completed.");
            throw new ForbiddenException("Cannot modify the course result. Access denied.");
        }

        if (result == courseRegistration.getResult()) {
            LOG.error("Result is already modified.");
            throw new UnProcessableEntityException("Result is already modified.");
        }

        courseRegistration.setResult(result);
        courseRegistration.setLastModifiedAt(coreService.getCurrentEpochSeconds());
        return courseRegistrationRepository.save(courseRegistration);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CourseRegistration> findAll(String filter, Pageable pageable) {
        CourseRegistrationSpecificationBuilder builder = new CourseRegistrationSpecificationBuilder();
        Matcher matcher = coreService.searchPatternMatcher(filter);
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        Specification<CourseRegistration> spec = builder.build();

        Page<CourseRegistration> courses;
        try {
            courses = courseRegistrationRepository.findAll(spec, pageable);
        } catch (InvalidDataAccessApiUsageException e) {
            LOG.error("Exception: {}", e);
            throw new UnProcessableEntityException("Could not validate request");
        }

        return courses;
    }
}
