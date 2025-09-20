package tn.st2i.user_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.user_backend.dto.DisciplineDTO;
import tn.st2i.user_backend.dto.EtablissementDTO;
import tn.st2i.user_backend.entity.Classe;
import tn.st2i.user_backend.entity.Discipline;
import tn.st2i.user_backend.entity.Etablissement;
import tn.st2i.user_backend.entity.Region;
import tn.st2i.user_backend.repository.ClasseRepository;
import tn.st2i.user_backend.repository.DisciplineRepository;
import tn.st2i.user_backend.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private ClasseRepository classeRepository;

    public List<DisciplineDTO> getAllDisciplines() {
        return disciplineRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DisciplineDTO getDisciplineById(Long id) {
        return disciplineRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));
    }

    public DisciplineDTO createDiscipline(DisciplineDTO disciplineDTO) {
        Discipline discipline = new Discipline();
        discipline.setNom(disciplineDTO.getNom());
        discipline.setRequiredHours(disciplineDTO.getRequiredHours());
        discipline.setNiveau(disciplineDTO.getNiveau());
        return convertToDTO(disciplineRepository.save(discipline));
    }

    public DisciplineDTO updateDiscipline(Long id, DisciplineDTO disciplineDTO) {
        Discipline discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));
        discipline.setNom(disciplineDTO.getNom());
        discipline.setNiveau(disciplineDTO.getNiveau());
        discipline.setRequiredHours(disciplineDTO.getRequiredHours());
        return convertToDTO(disciplineRepository.save(discipline));
    }

    public void deleteDiscipline(Long id) {
        disciplineRepository.deleteById(id);
    }

    private DisciplineDTO convertToDTO(Discipline discipline) {
        DisciplineDTO dto = new DisciplineDTO();
        dto.setId(discipline.getId());
        dto.setNom(discipline.getNom());
        dto.setRequiredHours(discipline.getRequiredHours());
        dto.setNiveau(discipline.getNiveau());
        return dto;
    }

}