package tn.st2i.calendrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {}