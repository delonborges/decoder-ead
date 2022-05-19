package com.ead.course.services.implementations;

import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImplementation implements LessonService {

    final LessonRepository lessonRepository;

    public LessonServiceImplementation(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
}
