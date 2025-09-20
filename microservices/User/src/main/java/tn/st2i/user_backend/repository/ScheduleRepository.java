package tn.st2i.user_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.st2i.user_backend.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  Optional<Schedule> findByClasseIdAndAcademicYear(Long classeId, String academicYear);

    Optional<Schedule> getSchedulesByClasseId(Long classId);

  @Query("SELECT COUNT(DISTINCT s) FROM Schedule s " +
          "JOIN s.items si " +
          "JOIN si.teacher t " +
          "WHERE t.id = :teacherId " +
          "AND s.academicYear = :academicYear " +
          "AND s.classe.id != :classId")
  long countByTeacherIdAndAcademicYearExcludingClasse(@Param("teacherId") Long teacherId,
                                                      @Param("academicYear") String academicYear,
                                                      @Param("classId") Long classId);
  @Query("SELECT COUNT(DISTINCT s) FROM Schedule s " +
          "JOIN s.items si " +
          "JOIN si.teacher t " +
          "WHERE t.id = :teacherId " +
          "AND s.academicYear = :academicYear")
  long countByTeacherIdAndAcademicYear(@Param("teacherId") Long teacherId,
                                       @Param("academicYear") String academicYear);

  Boolean existsByClasse_Id(Long classeId);
}
