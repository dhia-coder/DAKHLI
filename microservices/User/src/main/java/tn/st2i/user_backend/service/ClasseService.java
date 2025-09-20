package tn.st2i.user_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.user_backend.dto.ClasseDTO;
import tn.st2i.user_backend.dto.UserDTO;
import tn.st2i.user_backend.entity.Classe;
import tn.st2i.user_backend.entity.Etablissement;
import tn.st2i.user_backend.repository.ClasseRepository;
import tn.st2i.user_backend.repository.EtablissementRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClasseService {

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private EtablissementRepository etablissementRepository;

    public List<ClasseDTO> getAllClasses() {
        return classeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClasseDTO getClasseById(Long id) {
        return classeRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Classe not found"));
    }

    public List<ClasseDTO> getClassesByEtablissement(Long etablissementId) {
        List<Classe> classes = classeRepository.findByEtablissementId(etablissementId);
        return classes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClasseDTO createClasse(ClasseDTO classeDTO) {
        Classe classe = new Classe();
        classe.setNom(classeDTO.getNom());
        classe.setNiveau(classeDTO.getNiveau());
        // capacité (0 par défaut si null)
        classe.setCapacite(classeDTO.getCapacite() != null ? classeDTO.getCapacite() : 0);

        Etablissement etablissement = etablissementRepository.findById(classeDTO.getEtablissementId())
                .orElseThrow(() -> new RuntimeException("Etablissement not found"));
        classe.setEtablissement(etablissement);

        return convertToDTO(classeRepository.save(classe));
    }

    public ClasseDTO updateClasse(Long id, ClasseDTO classeDTO) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe not found"));

        classe.setNom(classeDTO.getNom());
        classe.setNiveau(classeDTO.getNiveau());
        if (classeDTO.getCapacite() != null) {
            classe.setCapacite(classeDTO.getCapacite());
        }

        if (classeDTO.getEtablissementId() != null) {
            Etablissement etablissement = etablissementRepository.findById(classeDTO.getEtablissementId())
                    .orElseThrow(() -> new RuntimeException("Etablissement not found"));
            classe.setEtablissement(etablissement);
        }

        return convertToDTO(classeRepository.save(classe));
    }

    public void deleteClasse(Long id) {
        classeRepository.deleteById(id);
    }

    public ClasseDTO affecterEtablissement(Long classeId, Long etablissementId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe not found"));
        Etablissement etablissement = etablissementRepository.findById(etablissementId)
                .orElseThrow(() -> new RuntimeException("Etablissement not found"));
        classe.setEtablissement(etablissement);
        return convertToDTO(classeRepository.save(classe));
    }

    private ClasseDTO convertToDTO(Classe classe) {
        ClasseDTO dto = new ClasseDTO();
        dto.setId(classe.getId());
        dto.setNom(classe.getNom());
        dto.setNiveau(classe.getNiveau());
        dto.setCapacite(classe.getCapacite()); // map capacité
        dto.setEtablissementId(classe.getEtablissement().getId());
        dto.setEtablissementNom(classe.getEtablissement().getNom());

        List<UserDTO> students = classe.getUsers().stream()
                .map(user -> {
                    UserDTO udto = new UserDTO();
                    udto.setId(user.getId());
                    udto.setFirstName(user.getFirstName());
                    udto.setLastName(user.getLastName());
                    udto.setEmail(user.getEmail());
                    return udto;
                })
                .collect(Collectors.toList());
        dto.setStudents(students);

        return dto;
    }

    public List<ClasseDTO> getAllClasses(Optional<Long> regionId, Optional<Long> etablissementId) {
        List<Classe> classes;
        if (etablissementId.isPresent()) {
            classes = classeRepository.findByEtablissementId(etablissementId.get());
        } else if (regionId.isPresent()) {
            classes = classeRepository.findAllByEtablissement_Region_Id(regionId.get());
        } else {
            classes = classeRepository.findAll();
        }
        return classes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
