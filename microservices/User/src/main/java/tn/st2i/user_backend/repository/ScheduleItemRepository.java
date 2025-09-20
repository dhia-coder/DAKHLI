package tn.st2i.user_backend.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.st2i.user_backend.entity.*;

import java.time.*;
import java.util.*;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {

  // Class view for a given day (ordered)
  @Query("""
    select si from ScheduleItem si
    where si.schedule.id = :scheduleId
      and si.dayOfWeek = :day
    order by si.startTime
  """)
  List<ScheduleItem> findByScheduleAndDay(Long scheduleId, DayOfWeek day);

  // Teacher weekly view (ordered)
  @Query("""
    select si from ScheduleItem si
    where si.teacher.id = :teacherId
      and si.schedule.academicYear = :year
    order by si.dayOfWeek, si.startTime
  """)
  List<ScheduleItem> findForTeacher(Long teacherId, String year);

  /* =========================
     Overlap checks (EXCLUDING)
     ========================= */

  // Class overlap (exclude a specific item id; pass null when creating)
  @Query("""
    select (count(si) > 0) from ScheduleItem si
    where si.schedule.id = :scheduleId
      and si.dayOfWeek = :day
      and (si.startTime < :end and si.endTime > :start)
      and (:excludeId is null or si.id <> :excludeId)
  """)
  boolean existsOverlapForClassExcluding(Long scheduleId,
                                         DayOfWeek day,
                                         LocalTime start,
                                         LocalTime end,
                                         Long excludeId);

  // Teacher overlap (exclude a specific item id; pass null when creating)
  @Query("""
    select (count(si) > 0) from ScheduleItem si
    where si.teacher.id = :teacherId
      and si.schedule.academicYear = :year
      and si.dayOfWeek = :day
      and (si.startTime < :end and si.endTime > :start)
      and (:excludeId is null or si.id <> :excludeId)
  """)
  boolean existsOverlapForTeacherExcluding(Long teacherId,
                                           String year,
                                           DayOfWeek day,
                                           LocalTime start,
                                           LocalTime end,
                                           Long excludeId);
  @Query("""
  select (count(si) > 0) from ScheduleItem si
  where si.dayOfWeek = :day
    and si.room = :room
    and (si.startTime < :end and si.endTime > :start)
    and (:excludeId is null or si.id <> :excludeId)
""")
  boolean existsOverlapForRoomExcluding(DayOfWeek day,
                                        LocalTime start,
                                        LocalTime end,
                                        String room,
                                        Long excludeId);

}
