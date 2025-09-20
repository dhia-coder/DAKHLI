package tn.st2i.calendrier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.st2i.calendrier.entity.CalendarEvent;
import tn.st2i.calendrier.service.CalendarEventService;

@RestController
@RequestMapping("/api/calendar-events")
@CrossOrigin(origins = "http://localhost:4200") 

public class CalendarEventController {

    private final CalendarEventService service;

    @Autowired
    public CalendarEventController(CalendarEventService service) {
        this.service = service;
    }

    @GetMapping
    public List<CalendarEvent> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CalendarEvent getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public CalendarEvent save(@RequestBody CalendarEvent event) {
        return service.save(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
