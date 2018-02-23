package com.domain.certification.api.web.hateoasAssembler;

import com.domain.certification.api.data.CourseRegistration;
import com.domain.certification.api.util.dto.Course.CourseRegistrationDTO;
import com.domain.certification.api.web.CourseRegistrationController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistrationAssembler extends ResourceAssemblerSupport<CourseRegistration, CourseRegistrationDTO> {

    private final CourseAssembler courseAssembler;
    private final UserAssembler userAssembler;

    public CourseRegistrationAssembler(CourseAssembler courseAssembler, UserAssembler userAssembler) {
        super(CourseRegistrationController.class, CourseRegistrationDTO.class);
        this.courseAssembler = courseAssembler;
        this.userAssembler = userAssembler;
    }

    @Override
    public CourseRegistrationDTO toResource(CourseRegistration courseRegistration) {
        CourseRegistrationDTO courseRegistrationDTO = null;
        if (courseRegistration != null) {
            courseRegistrationDTO = createResourceWithId(courseRegistration.getId(), courseRegistration);
            courseRegistrationDTO.removeLinks();
        }

        return courseRegistrationDTO;
    }

    @Override
    public CourseRegistrationDTO instantiateResource(CourseRegistration courseRegistration) {
        CourseRegistrationDTO courseRegistrationDTO = null;
        if (courseRegistration != null) {
            courseRegistrationDTO = new CourseRegistrationDTO();
            courseRegistrationDTO.setCourseRegistrationId(courseRegistration.getId());
            courseRegistrationDTO.setCourse(courseAssembler.toResource(courseRegistration.getCourse()));
            courseRegistrationDTO.setUser(userAssembler.toResource(courseRegistration.getUser()));
            courseRegistrationDTO.setResult(courseRegistration.getResult());
            courseRegistrationDTO.setCreatedAt(courseRegistration.getCreatedAt());
            courseRegistrationDTO.setLastModifiedAt(courseRegistration.getLastModifiedAt());
        }

        return courseRegistrationDTO;
    }
}
