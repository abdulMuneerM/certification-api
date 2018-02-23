package com.domain.certification.api.web;

import com.domain.certification.api.data.Course;
import com.domain.certification.api.data.User;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.service.CourseService;
import com.domain.certification.api.util.dto.Course.CourseDTO;
import com.domain.certification.api.util.dto.response.PageDTO;
import com.domain.certification.api.util.dto.response.ResponseDTO;
import com.domain.certification.api.util.dto.user.UserDTO;
import com.domain.certification.api.util.dto.user.UserLoginDTO;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import com.domain.certification.api.util.enumerator.RoleType;
import com.domain.certification.api.web.hateoasAssembler.CourseAssembler;
import com.domain.certification.api.web.validator.CourseValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CourseController {

    private final CourseService courseService;
    private final CourseAssembler courseAssembler;
    private final CourseValidator courseValidator;

    public CourseController(CourseService courseService, CourseAssembler courseAssembler, CourseValidator courseValidator) {
        this.courseService = courseService;
        this.courseAssembler = courseAssembler;
        this.courseValidator = courseValidator;
    }

    @GetMapping(value = "/courses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> findOneCourse(@PathVariable(name = "id") Long id) {
        Course course = courseService.findOne(id);

        Map<String, CourseDTO> data = new HashMap<>();
        data.put("course", courseAssembler.toResource(course));

        ResponseDTO<Map<String, CourseDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @InitBinder("courseDTO")
    protected void initCourseBinder(WebDataBinder binder) {
        binder.setValidator(courseValidator);
    }

    @PostMapping(
            value = "/courses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        Course course = courseService.save(courseDTO);

        Map<String, CourseDTO> data = new HashMap<>();
        data.put("course", courseAssembler.toResource(course));

        ResponseDTO<Map<String, CourseDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping(
            value = "/courses/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> updateCourse(@PathVariable(name = "id") Long id,
                                             @Valid @RequestBody CourseDTO courseDTO) {
        Course course = courseService.update(id, courseDTO);

        Map<String, CourseDTO> data = new HashMap<>();
        data.put("course", courseAssembler.toResource(course));

        ResponseDTO<Map<String, CourseDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> getAllCourses(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String sort
    ) {

        String sortFieldName = "id";
        String sortDir = "asc";
        if (!StringUtils.isEmpty(sort)) {
            String[] arr = sort.split(",");
            if (arr.length == 2) {
                sortFieldName = arr[0];
                sortDir = arr[1];
            }
        }

        if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
            throw new UnProcessableEntityException("Invalid sort order");
        }

        Sort sortObj = new Sort(Sort.Direction.fromString(sortDir), sortFieldName);
        Pageable pageable = new PageRequest(page, size, sortObj);

        Page<Course>  coursePage = courseService.findAll(search, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("courses", courseAssembler.toResources(coursePage.getContent()));
        PageDTO pageDTO = new PageDTO(coursePage.getSize(), coursePage.getTotalElements(),
                coursePage.getTotalPages(), coursePage.getNumber());
        data.put("page", pageDTO);

        ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
