// src/main/java/tn/st2i/programScholaire/controller/ProgrammeController.java
package tn.st2i.programScholaire.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tn.st2i.programScholaire.dto.ProgrammeDisciplineRequest;
import tn.st2i.programScholaire.dto.ProgrammeDisciplineResponse;
import tn.st2i.programScholaire.dto.ProgrammeRequest;
import tn.st2i.programScholaire.dto.ProgrammeResponse;
import tn.st2i.programScholaire.service.ProgrammeService;

@RestController
@RequestMapping("/api/programmes")
@CrossOrigin(origins = "http://localhost:4200") 

public class ProgrammeController {

    private final ProgrammeService programmeService;

    public ProgrammeController(ProgrammeService programmeService) {
        this.programmeService = programmeService;
    }

    @GetMapping
    public Page<ProgrammeResponse> list(
            @RequestParam(required = false) Long etablissementId,
            @RequestParam(required = false) Boolean actif,
            Pageable pageable) {
        return programmeService.list(etablissementId, actif, pageable);
    }

    @GetMapping("/{id}")
    public ProgrammeResponse get(@PathVariable Long id) {
        return programmeService.get(id);
    }

    @PostMapping
    public ResponseEntity<ProgrammeResponse> create(@Valid @RequestBody ProgrammeRequest request) {
        return ResponseEntity.ok(programmeService.create(request));
    }

    @PutMapping("/{id}")
    public ProgrammeResponse update(@PathVariable Long id, @Valid @RequestBody ProgrammeRequest request) {
        return programmeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programmeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{programmeId}/disciplines")
    public Page<ProgrammeDisciplineResponse> listDisciplines(@PathVariable Long programmeId, Pageable pageable) {
        return programmeService.listDisciplines(programmeId, pageable);
    }

    @PostMapping("/{programmeId}/disciplines")
    public ResponseEntity<ProgrammeDisciplineResponse> addDiscipline(@PathVariable Long programmeId,
                                                                     @Valid @RequestBody ProgrammeDisciplineRequest request) {
        return ResponseEntity.ok(programmeService.addDiscipline(programmeId, request));
    }

    @PutMapping("/{programmeId}/disciplines/{itemId}")
    public ProgrammeDisciplineResponse updateDiscipline(@PathVariable Long programmeId,
                                                        @PathVariable Long itemId,
                                                        @Valid @RequestBody ProgrammeDisciplineRequest request) {
        return programmeService.updateDiscipline(programmeId, itemId, request);
    }

    @DeleteMapping("/{programmeId}/disciplines/{itemId}")
    public ResponseEntity<Void> removeDiscipline(@PathVariable Long programmeId,
                                                 @PathVariable Long itemId) {
        programmeService.removeDiscipline(programmeId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithGroq(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        return ResponseEntity.ok(programmeService.chatWithGroq(message));
    }
}
