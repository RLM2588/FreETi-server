package ru.parovozik.backend.service;

import org.springframework.stereotype.Service;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.repostitory.PushTemplateRepository;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.Duration;
import java.time.Period;
import java.util.NoSuchElementException;

@Service
public class PushTemplateService {
    private final PushTemplateRepository pushTemplateRepository;

    public PushTemplateService(PushTemplateRepository pushTemplateRepository) {
        this.pushTemplateRepository = pushTemplateRepository;
    }

    public boolean create(Period period, Duration duration) {
        try {
            PushTemplate pushTemplate = pushTemplateRepository.findPushTemplateByBeforeHowDaysAndBeforeHowHours(period, duration);
            if(pushTemplate != null)
                throw new KeyAlreadyExistsException();
            pushTemplate = new PushTemplate();
            pushTemplate.setBeforeHowDays(period);
            pushTemplate.setBeforeHowHours(duration);
            pushTemplateRepository.save(pushTemplate);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PushTemplate getByPeriodAndDuration(Period period, Duration duration) {
        try {
            PushTemplate pushTemplate = pushTemplateRepository.findPushTemplateByBeforeHowDaysAndBeforeHowHours(period, duration);
            return pushTemplate;
        } catch (Exception e) {
            throw new NoSuchElementException(e);
        }
    }

    public boolean delete(Period period, Duration duration) {
        try
        {
            PushTemplate pushTemplate = pushTemplateRepository.findPushTemplateByBeforeHowDaysAndBeforeHowHours(period, duration);
            pushTemplateRepository.delete(pushTemplate);
            return true;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
