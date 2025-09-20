package tn.st2i.calendrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.Semester;
import tn.st2i.calendrier.entity.Trimesters;

public interface TrimestersRepository extends JpaRepository<Trimesters, Long> {}