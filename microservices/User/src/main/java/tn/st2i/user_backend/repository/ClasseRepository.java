package tn.st2i.user_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.user_backend.entity.Classe;

import java.util.List;

public interface ClasseRepository extends JpaRepository<Classe, Long> {
    // Good ! Utilise l'id de l'établissement, pas l'objet
    List<Classe> findByEtablissementId(Long etablissementId);

    List<Classe> findAllByEtablissement_Region_Id(Long etablissementRegionId);
} 