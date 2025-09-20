package tn.st2i.calendrier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.SchoolYear;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {

    Optional<SchoolYear> findTopByClasseIdOrderByStartDateDesc(Long classeId);

}
