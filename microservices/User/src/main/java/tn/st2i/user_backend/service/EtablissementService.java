package tn.st2i.user_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.user_backend.dto.EtablissementDTO;
import tn.st2i.user_backend.entity.Etablissement;
import tn.st2i.user_backend.entity.Region;
import tn.st2i.user_backend.repository.EtablissementRepository;
import tn.st2i.user_backend.repository.RegionRepository;
import tn.st2i.user_backend.repository.ClasseRepository;
import tn.st2i.user_backend.entity.Classe;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtablissementService {

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ClasseRepository classeRepository;

    public List<EtablissementDTO> getAllEtablissements() {
        return etablissementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EtablissementDTO getEtablissementById(Long id) {
        return etablissementRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Etablissement not found"));
    }

    public EtablissementDTO createEtablissement(EtablissementDTO etablissementDTO) {
        Etablissement etablissement = new Etablissement();
        etablissement.setNom(etablissementDTO.getNom());
        etablissement.setAdresse(etablissementDTO.getAdresse());
        
        Region region = regionRepository.findById(etablissementDTO.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));
        etablissement.setRegion(region);

        if (etablissementDTO.getClasseIds() != null) {
            etablissement.setClasses(new java.util.HashSet<>(classeRepository.findAllById(etablissementDTO.getClasseIds())));
        }

        return convertToDTO(etablissementRepository.save(etablissement));
    }

    public EtablissementDTO updateEtablissement(Long id, EtablissementDTO etablissementDTO) {
        Etablissement etablissement = etablissementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etablissement not found"));

        etablissement.setNom(etablissementDTO.getNom());
        etablissement.setAdresse(etablissementDTO.getAdresse());

        if (etablissementDTO.getRegionId() != null) {
            Region region = regionRepository.findById(etablissementDTO.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Region not found"));
            etablissement.setRegion(region);
        }

        if (etablissementDTO.getClasseIds() != null) {
            etablissement.setClasses(new java.util.HashSet<>(classeRepository.findAllById(etablissementDTO.getClasseIds())));
        }

        return convertToDTO(etablissementRepository.save(etablissement));
    }

    public void deleteEtablissement(Long id) {
        etablissementRepository.deleteById(id);
    }

    public EtablissementDTO affecterRegion(Long etablissementId, Long regionId) {
        Etablissement etablissement = etablissementRepository.findById(etablissementId)
                .orElseThrow(() -> new RuntimeException("Etablissement not found"));
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        etablissement.setRegion(region);
        return convertToDTO(etablissementRepository.save(etablissement));
    }
    /**
     * Returns all Etablissements for a given regionId.
     */
    public List<EtablissementDTO> getEtablissementsByRegion(Long regionId) {
        // Get the Region entity, throw if not found
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found"));

        // Get all etablissements with this region
        List<Etablissement> etablissements = etablissementRepository.findByRegion(region);

        // Convert to DTO
        return etablissements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EtablissementDTO convertToDTO(Etablissement etablissement) {
        EtablissementDTO dto = new EtablissementDTO();
        dto.setId(etablissement.getId());
        dto.setNom(etablissement.getNom());
        dto.setAdresse(etablissement.getAdresse());
        dto.setRegionId(etablissement.getRegion().getId());
        dto.setRegionNom(etablissement.getRegion().getNom());
        if (etablissement.getClasses() != null) {
            dto.setClasseIds(etablissement.getClasses().stream().map(Classe::getId).collect(java.util.stream.Collectors.toList()));
        }
        return dto;
    }
} 