package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private static final String MODULE_NOT_FOUND = "Module not found for this course";
    private final ModuleService moduleService;
    private final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDto moduleDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Course ID not found");
        }
        ModuleModel moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleFromCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(MODULE_NOT_FOUND);
        } else {
            moduleService.delete(moduleModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Module deleted successfully");
        }
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleFromCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(MODULE_NOT_FOUND);
        } else {
            var moduleModel = moduleModelOptional.get();
            BeanUtils.copyProperties(moduleDto, moduleModel);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(moduleService.save(moduleModel));
        }
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModulesFromCourse(@PathVariable(value = "courseId") UUID courseId,
                                                                     SpecificationTemplate.ModuleSpec moduleSpec,
                                                                     @PageableDefault(sort = "moduleId",
                                                                                      direction = Sort.Direction.ASC)
                                                                     Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(moduleService.findAllModulesFromCourse(SpecificationTemplate.moduleCourseId(courseId)
                                                                                               .and(moduleSpec),
                                                                          pageable));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getModuleById(@PathVariable(value = "courseId") UUID courseId,
                                                @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleFromCourse(courseId, moduleId);
        return moduleModelOptional.<ResponseEntity<Object>>map(moduleModel -> ResponseEntity.status(HttpStatus.OK)
                                                                                            .body(moduleModel))
                                  .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                                 .body(MODULE_NOT_FOUND));
    }
}