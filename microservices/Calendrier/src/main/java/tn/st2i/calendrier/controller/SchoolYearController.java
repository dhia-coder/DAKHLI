package tn.st2i.calendrier.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import tn.st2i.calendrier.dto.SchoolYearDto;
import tn.st2i.calendrier.service.SchoolYearService;

@RestController
@RequestMapping("/api/school-years")
@CrossOrigin(origins = "http://localhost:4200") 

public class    SchoolYearController {
    private final SchoolYearService service;

    public SchoolYearController(SchoolYearService service) {
        this.service = service;
    }

    @PostMapping
    public SchoolYearDto create(@RequestBody SchoolYearDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<SchoolYearDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SchoolYearDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public SchoolYearDto update(@PathVariable Long id, @RequestBody SchoolYearDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/by-classe/{classeId}")
    public ResponseEntity<SchoolYearDto> getSchoolYearByClasseId(@PathVariable Long classeId) {
        try {
            SchoolYearDto dto = service.findSchoolYearByClasseId(classeId); // <-- your service method
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}

