// ExamPeriodService.java
package tn.st2i.calendrier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.calendrier.entity.ExamPeriod;
import tn.st2i.calendrier.repository.ExamPeriodRepository;

import java.util.List;

@Service
public class ExamPeriodService {

    private final ExamPeriodRepository repository;

    @Autowired
    public ExamPeriodService(ExamPeriodRepository repository) {
        this.repository = repository;
    }

    public List<ExamPeriod> getAll() {
        return repository.findAll();
    }

    public ExamPeriod getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public ExamPeriod save(ExamPeriod examPeriod) {
        return repository.save(examPeriod);
    }

    public ExamPeriod update(Long id, ExamPeriod examPeriod) {
        if (repository.existsById(id)) {
            examPeriod.setId(id);
            return repository.save(examPeriod);
        }
        return null; // or throw an exception
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
