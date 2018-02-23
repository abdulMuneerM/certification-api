package com.domain.certification.api.util.dto.Course;

import com.domain.certification.api.util.dto.user.UserDTO;
import com.domain.certification.api.util.enumerator.CourseResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseRegistrationDTO extends ResourceSupport {

    @JsonProperty("id")
    private Long courseRegistrationId;

    private CourseDTO course;

    private Long courseId;

    private UserDTO user;

    private String userId;

    private CourseResult result;

    private Long createdAt;

    private Long lastModifiedAt;

    public Long getCourseRegistrationId() {
        return courseRegistrationId;
    }

    public void setCourseRegistrationId(Long courseRegistrationId) {
        this.courseRegistrationId = courseRegistrationId;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CourseResult getResult() {
        return result;
    }

    public void setResult(CourseResult result) {
        this.result = result;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Long lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
