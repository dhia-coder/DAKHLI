
package tn.st2i.programScholaire.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.st2i.programScholaire.entity.ProgrammeDiscipline;

public interface ProgrammeDisciplineRepository extends JpaRepository<ProgrammeDiscipline, Long> {
    List<ProgrammeDiscipline> findByProgrammeId(Long programmeId);
    Optional<ProgrammeDiscipline> findByIdAndProgrammeId(Long id, Long programmeId);
    boolean existsByProgrammeIdAndDisciplineId(Long programmeId, Long disciplineId);
}
