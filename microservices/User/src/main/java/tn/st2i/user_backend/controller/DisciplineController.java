package tn.st2i.user_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.st2i.user_backend.dto.DisciplineDTO;
import tn.st2i.user_backend.dto.EtablissementDTO;
import tn.st2i.user_backend.service.DisciplineService;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
@CrossOrigin(origins = "http://localhost:4200") 
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @GetMapping
    // @PreAuthorize("hasAuthority('user.read')")
    public List<DisciplineDTO> getAllDisciplines() {
        return disciplineService.getAllDisciplines();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('user.read')")
    public DisciplineDTO getDisciplineById(@PathVariable Long id) {
        return disciplineService.getDisciplineById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user.create')")
    public DisciplineDTO createDiscipline(@RequestBody DisciplineDTO disciplineDTO) {
        return disciplineService.createDiscipline(disciplineDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user.update')")
    public DisciplineDTO updateDiscipline(@PathVariable Long id, @RequestBody DisciplineDTO disciplineDTO) {
        return disciplineService.updateDiscipline(id, disciplineDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user.delete')")
    public ResponseEntity<Void> deleteDiscipline(@PathVariable Long id) {
        disciplineService.deleteDiscipline(id);
        return ResponseEntity.ok().build();
    }
} 