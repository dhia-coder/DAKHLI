package tn.st2i.user_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.st2i.user_backend.dto.ClasseDTO;
import tn.st2i.user_backend.dto.EtablissementDTO;
import tn.st2i.user_backend.service.ClasseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "http://localhost:4200") 
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @GetMapping
    @PreAuthorize("hasAuthority('user.read')")
    public List<ClasseDTO> getAllClasses(
            Optional<Long> regionId,
            Optional<Long> etablissementId

    ) {
        return classeService.getAllClasses(regionId,etablissementId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user.read')")
    public ClasseDTO getClasseById(@PathVariable Long id) {
        return classeService.getClasseById(id);
    }

    @GetMapping("/by-etablissement/{etablissementId}")
// @PreAuthorize("hasAuthority('user.read')")
    public List<ClasseDTO> getClassesByEtablissement(@PathVariable Long etablissementId) {
        return classeService.getClassesByEtablissement(etablissementId);
    }




    @PostMapping
    @PreAuthorize("hasAuthority('user.create')")
    public ClasseDTO createClasse(@RequestBody ClasseDTO classeDTO) {
        return classeService.createClasse(classeDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user.update')")
    public ClasseDTO updateClasse(@PathVariable Long id, @RequestBody ClasseDTO classeDTO) {
        return classeService.updateClasse(id, classeDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete')")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{classeId}/etablissement/{etablissementId}")
    @PreAuthorize("hasAuthority('user.update')")
    public ClasseDTO affecterEtablissement(
            @PathVariable Long classeId,
            @PathVariable Long etablissementId) {
        return classeService.affecterEtablissement(classeId, etablissementId);
    }
} 