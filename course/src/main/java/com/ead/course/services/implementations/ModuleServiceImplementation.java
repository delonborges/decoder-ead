package com.ead.course.services.implementations;

import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImplementation implements ModuleService {

    final ModuleRepository moduleRepository;

    public ModuleServiceImplementation(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }
}
