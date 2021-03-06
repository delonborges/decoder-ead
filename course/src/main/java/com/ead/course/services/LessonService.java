package com.ead.course.services;

import com.ead.course.models.LessonModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    LessonModel save(LessonModel lessonModel);

    Optional<LessonModel> findLessonFromModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonModel);

    List<LessonModel> findAllLessonsFromModules(UUID moduleId);
}
