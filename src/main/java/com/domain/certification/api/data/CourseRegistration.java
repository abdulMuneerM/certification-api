package com.domain.certification.api.data;

import com.domain.certification.api.util.enumerator.CourseResult;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class CourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "user cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "course cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull(message = "createdAt cannot be null.")
    private Long createdAt;

    @Enumerated(EnumType.ORDINAL)
    private CourseResult result;

    @NotNull(message = "lastModifiedAt cannot be null.")
    private Long lastModifiedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public CourseResult getResult() {
        return result;
    }

    public void setResult(CourseResult result) {
        this.result = result;
    }

    public Long getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Long lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
