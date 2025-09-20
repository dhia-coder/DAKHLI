package tn.st2i.calendrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
}
