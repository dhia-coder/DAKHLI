package tn.st2i.user_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.user_backend.entity.Etablissement;
import tn.st2i.user_backend.entity.Region;

import java.util.List;

public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {
    List<Etablissement> findByRegion(Region region);
} 