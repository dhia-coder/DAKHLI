
package tn.st2i.programScholaire.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.programScholaire.entity.Programme;

public interface ProgrammeRepository extends JpaRepository<Programme, Long> {
    Page<Programme> findByEtablissementId(Long etablissementId, Pageable pageable);
    Page<Programme> findByEtablissementIdAndActif(Long etablissementId, Boolean actif, Pageable pageable);
    boolean existsByEtablissementIdAndNomAndNiveau(Long etablissementId, String nom, String niveau);
}
