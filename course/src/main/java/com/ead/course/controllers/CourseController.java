package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private static final String COURSE_NOT_FOUND = "Course ID not found";
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(COURSE_NOT_FOUND);
        } else {
            courseService.delete(courseModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Course deleted successfully");
        }
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid CourseDto courseDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(COURSE_NOT_FOUND);
        } else {
            var courseModel = courseModelOptional.get();
            BeanUtils.copyProperties(courseDto, courseModel);
            courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(courseService.save(courseModel));
        }
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec courseSpec,
                                                           @PageableDefault(sort = "courseId",
                                                                            direction = Sort.Direction.ASC)
                                                           Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(courseService.findAll(courseSpec, pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        return courseModelOptional.<ResponseEntity<Object>>map(courseModel -> ResponseEntity.status(HttpStatus.OK)
                                                                                            .body(courseModel))
                                  .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                                 .body(COURSE_NOT_FOUND));
    }
}
