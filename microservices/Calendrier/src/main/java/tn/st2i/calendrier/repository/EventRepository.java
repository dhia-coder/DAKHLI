package tn.st2i.calendrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.calendrier.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> { }

