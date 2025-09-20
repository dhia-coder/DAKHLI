package tn.st2i.calendrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.ExamPeriod;

public interface ExamPeriodRepository extends JpaRepository<ExamPeriod, Long> { }
