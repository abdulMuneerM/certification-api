package com.domain.certification.api.web.hateoasAssembler;

import com.domain.certification.api.data.Course;
import com.domain.certification.api.util.dto.Course.CourseDTO;
import com.domain.certification.api.web.CourseController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CourseAssembler extends ResourceAssemblerSupport<Course, CourseDTO> {

    public CourseAssembler() {
        super(CourseController.class, CourseDTO.class);
    }

    @Override
    public CourseDTO toResource(Course course) {
         CourseDTO courseDTO = null;
        if (course != null) {
            courseDTO = createResourceWithId(course.getId(), course);
            courseDTO.removeLinks();
        }

        return courseDTO;
    }

    @Override
    public CourseDTO instantiateResource(Course course) {
        CourseDTO courseDTO = null;
        if (course != null) {
            courseDTO = new CourseDTO();
            courseDTO.setCourseId(course.getId());
            courseDTO.setName(course.getName());
            courseDTO.setCost(course.getCost());
            courseDTO.setCreatedAt(course.getCreatedAt());
            courseDTO.setLastModifiedAt(course.getLastModifiedAt());
        }

        return courseDTO;
    }
}
