package com.ead.course.services;

import com.ead.course.models.LessonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    LessonModel save(LessonModel lessonModel);

    Optional<LessonModel> findLessonFromModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonModel);

    Page<LessonModel> findAllLessonsFromModules(Specification<LessonModel> specification, Pageable pageable);
}
