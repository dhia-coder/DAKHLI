package tn.st2i.calendrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {}