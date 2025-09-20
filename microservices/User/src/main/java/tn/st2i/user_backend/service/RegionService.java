package tn.st2i.user_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.st2i.user_backend.dto.RegionDTO;
import tn.st2i.user_backend.entity.Region;
import tn.st2i.user_backend.repository.RegionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RegionDTO getRegionById(Long id) {
        return regionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Region not found"));
    }

    public RegionDTO createRegion(RegionDTO regionDTO) {
        Region region = new Region();
        region.setNom(regionDTO.getNom());
        return convertToDTO(regionRepository.save(region));
    }

    public RegionDTO updateRegion(Long id, RegionDTO regionDTO) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        region.setNom(regionDTO.getNom());
        return convertToDTO(regionRepository.save(region));
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    private RegionDTO convertToDTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setId(region.getId());
        dto.setNom(region.getNom());
        return dto;
    }
} 