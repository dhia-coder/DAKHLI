package tn.st2i.user_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.st2i.user_backend.dto.EtablissementDTO;
import tn.st2i.user_backend.service.EtablissementService;

import java.util.List;

@RestController
@RequestMapping("/api/etablissements")
@CrossOrigin(origins = "http://localhost:4200") 
public class    EtablissementController {

    @Autowired
    private EtablissementService etablissementService;

    @GetMapping
    // @PreAuthorize("hasAuthority('user.read')")
    public List<EtablissementDTO> getAllEtablissements() {
        return etablissementService.getAllEtablissements();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('user.read')")
    public EtablissementDTO getEtablissementById(@PathVariable Long id) {
        return etablissementService.getEtablissementById(id);
    }

    @PostMapping    
    @PreAuthorize("hasAuthority('user.create')")
    public EtablissementDTO createEtablissement(@RequestBody EtablissementDTO etablissementDTO) {
        return etablissementService.createEtablissement(etablissementDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user.update')")
    public EtablissementDTO updateEtablissement(@PathVariable Long id, @RequestBody EtablissementDTO etablissementDTO) {
        return etablissementService.updateEtablissement(id, etablissementDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete')")
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        etablissementService.deleteEtablissement(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/by-region/{regionId}")
    //@PreAuthorize("hasAuthority('user.read')")
    public List<EtablissementDTO> getEtablissementsByRegion(@PathVariable Long regionId) {
        return etablissementService.getEtablissementsByRegion(regionId);
    }


    @PutMapping("/{etablissementId}/region/{regionId}")
    @PreAuthorize("hasAuthority('user.update')")
    public EtablissementDTO affecterRegion(
            @PathVariable Long etablissementId,
            @PathVariable Long regionId) {
        return etablissementService.affecterRegion(etablissementId, regionId);
    }
} 