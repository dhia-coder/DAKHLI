// EventService.java
package tn.st2i.calendrier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.calendrier.entity.Event;
import tn.st2i.calendrier.repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> getAll() {
        return repository.findAll();
    }

    public Event getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Event save(Event event) {
        return repository.save(event);
    }

    public Event update(Long id, Event event) {
        if (repository.existsById(id)) {
            event.setId(id);
            return repository.save(event);
        }
        return null; // or throw an exception
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
