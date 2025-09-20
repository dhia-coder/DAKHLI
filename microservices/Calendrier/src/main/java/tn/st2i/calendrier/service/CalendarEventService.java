package tn.st2i.calendrier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.calendrier.entity.CalendarEvent;
import tn.st2i.calendrier.repository.CalendarEventRepository;

import java.util.List;

@Service
public class CalendarEventService {

    private final CalendarEventRepository repository;

    @Autowired
    public CalendarEventService(CalendarEventRepository repository) {
        this.repository = repository;
    }

    public List<CalendarEvent> getAll() {
        return repository.findAll();
    }

    public CalendarEvent getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public CalendarEvent save(CalendarEvent event) {
        return repository.save(event);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}