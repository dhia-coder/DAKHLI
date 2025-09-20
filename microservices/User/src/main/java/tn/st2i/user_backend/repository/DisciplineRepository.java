package tn.st2i.user_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.st2i.user_backend.entity.Discipline;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    List<Discipline> findAllByNiveau(String niveau);
}