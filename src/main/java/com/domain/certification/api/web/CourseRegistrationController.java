package com.domain.certification.api.web;

import com.domain.certification.api.data.CourseRegistration;
import com.domain.certification.api.exception.UnProcessableEntityException;
import com.domain.certification.api.service.CourseRegistrationService;
import com.domain.certification.api.util.dto.Course.CourseRegistrationDTO;
import com.domain.certification.api.util.dto.response.PageDTO;
import com.domain.certification.api.util.dto.response.ResponseDTO;
import com.domain.certification.api.util.enumerator.CourseResult;
import com.domain.certification.api.web.hateoasAssembler.CourseRegistrationAssembler;
import com.domain.certification.api.web.validator.CourseRegistrationValidator;
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
public class CourseRegistrationController {

    private final CourseRegistrationService courseRegistrationService;
    private final CourseRegistrationAssembler courseRegistrationAssembler;
    private final CourseRegistrationValidator courseRegistrationValidator;

    public CourseRegistrationController(CourseRegistrationService courseRegistrationService,
                                        CourseRegistrationAssembler courseRegistrationAssembler,
                                        CourseRegistrationValidator courseRegistrationValidator) {
        this.courseRegistrationService = courseRegistrationService;
        this.courseRegistrationAssembler = courseRegistrationAssembler;
        this.courseRegistrationValidator = courseRegistrationValidator;
    }

    @InitBinder("courseRegistrationDTO")
    protected void initCourseRegistrationBinder(WebDataBinder binder) {
        binder.setValidator(courseRegistrationValidator);
    }

    @PostMapping(
            value = "/courseRegistrations",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> registerCourse(@Valid @RequestBody CourseRegistrationDTO courseRegistrationDTO) {
        CourseRegistration courseRegistration = courseRegistrationService.save(courseRegistrationDTO);

        Map<String, CourseRegistrationDTO> data = new HashMap<>();
        data.put("courseRegistration", courseRegistrationAssembler.toResource(courseRegistration));

        ResponseDTO<Map<String, CourseRegistrationDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(
            value = "/courseRegistrations/{id}/result/{result}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<ResponseDTO> updateCourseRegistrationResult(@PathVariable(name = "id") Long id,
                                                               @PathVariable(name = "result") CourseResult result) {
        CourseRegistration courseRegistration = courseRegistrationService.updateCourseResult(id, result);

        Map<String, CourseRegistrationDTO> data = new HashMap<>();
        data.put("courseRegistration", courseRegistrationAssembler.toResource(courseRegistration));

        ResponseDTO<Map<String, CourseRegistrationDTO>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/courseRegistrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> findAllCourseRegistrations(
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

        Page<CourseRegistration> courseRegistrations = courseRegistrationService.findAll(search, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("courseRegistrations", courseRegistrationAssembler.toResources(courseRegistrations.getContent()));
        PageDTO pageDTO = new PageDTO(courseRegistrations.getSize(), courseRegistrations.getTotalElements(),
                courseRegistrations.getTotalPages(), courseRegistrations.getNumber());
        data.put("page", pageDTO);

        ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
