// src/main/java/tn/st2i/programScholaire/service/ProgrammeService.java
package tn.st2i.programScholaire.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tn.st2i.programScholaire.dto.ProgrammeDisciplineRequest;
import tn.st2i.programScholaire.dto.ProgrammeDisciplineResponse;
import tn.st2i.programScholaire.dto.ProgrammeRequest;
import tn.st2i.programScholaire.dto.ProgrammeResponse;

public interface ProgrammeService {
    Page<ProgrammeResponse> list(Long etablissementId, Boolean actif, Pageable pageable);
    ProgrammeResponse get(Long id);
    ProgrammeResponse create(ProgrammeRequest request);
    ProgrammeResponse update(Long id, ProgrammeRequest request);
    void delete(Long id);

    ProgrammeDisciplineResponse addDiscipline(Long programmeId, ProgrammeDisciplineRequest request);
    ProgrammeDisciplineResponse updateDiscipline(Long programmeId, Long disciplineItemId, ProgrammeDisciplineRequest request);
    void removeDiscipline(Long programmeId, Long disciplineItemId);
    Page<ProgrammeDisciplineResponse> listDisciplines(Long programmeId, Pageable pageable);
    // src/main/java/tn/st2i/programScholaire/service/ProgrammeService.java
    String chatWithGroq(String message);

}
