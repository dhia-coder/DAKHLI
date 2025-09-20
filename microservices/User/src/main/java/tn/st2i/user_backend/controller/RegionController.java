package tn.st2i.user_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.st2i.user_backend.dto.RegionDTO;
import tn.st2i.user_backend.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@CrossOrigin(origins = "http://localhost:4200") 
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    // @PreAuthorize("hasAuthority('user.read')")
    public List<RegionDTO> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('user.read')")
    public RegionDTO getRegionById(@PathVariable Long id) {
        return regionService.getRegionById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user.create')")
    public RegionDTO createRegion(@RequestBody RegionDTO regionDTO) {
        return regionService.createRegion(regionDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user.update')")
    public RegionDTO updateRegion(@PathVariable Long id, @RequestBody RegionDTO regionDTO) {
        return regionService.updateRegion(id, regionDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete')")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.ok().build();
    }
} 