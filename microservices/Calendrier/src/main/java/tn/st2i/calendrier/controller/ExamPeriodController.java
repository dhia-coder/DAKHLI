// ExamPeriodController.java
package tn.st2i.calendrier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.st2i.calendrier.entity.ExamPeriod;
import tn.st2i.calendrier.service.ExamPeriodService;

@RestController
@RequestMapping("/api/exam-periods")
@CrossOrigin(origins = "http://localhost:4200") 

public class ExamPeriodController {

    private final ExamPeriodService service;

    @Autowired
    public ExamPeriodController(ExamPeriodService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExamPeriod> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ExamPeriod getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ExamPeriod save(@RequestBody ExamPeriod examPeriod) {
        return service.save(examPeriod);
    }

    @PutMapping("/{id}")
    public ExamPeriod update(@PathVariable Long id, @RequestBody ExamPeriod examPeriod) {
        return service.update(id, examPeriod);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
