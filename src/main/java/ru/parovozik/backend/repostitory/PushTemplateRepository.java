package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.entity.RepeatTask;

import java.time.Duration;
import java.time.Period;
import java.util.UUID;

@RepositoryRestResource(path = "push_template")
public interface PushTemplateRepository extends CrudRepository<PushTemplate, Integer> {
    PushTemplate findPushTemplateByBeforeHowDaysAndBeforeHowHours(Period beforeHowDays, Duration beforeHowHours);
}
