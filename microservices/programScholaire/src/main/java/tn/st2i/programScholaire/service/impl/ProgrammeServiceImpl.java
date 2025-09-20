// src/main/java/tn/st2i/programScholaire/service/impl/ProgrammeServiceImpl.java
package tn.st2i.programScholaire.service.impl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.st2i.programScholaire.dto.ProgrammeDisciplineRequest;
import tn.st2i.programScholaire.dto.ProgrammeDisciplineResponse;
import tn.st2i.programScholaire.dto.ProgrammeRequest;
import tn.st2i.programScholaire.dto.ProgrammeResponse;
import tn.st2i.programScholaire.entity.Programme;
import tn.st2i.programScholaire.entity.ProgrammeDiscipline;
import tn.st2i.programScholaire.exception.ConflictException;
import tn.st2i.programScholaire.exception.NotFoundException;
import tn.st2i.programScholaire.repository.ProgrammeDisciplineRepository;
import tn.st2i.programScholaire.repository.ProgrammeRepository;
import tn.st2i.programScholaire.service.ProgrammeService;

@Service
@Transactional
public class ProgrammeServiceImpl implements ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final ProgrammeDisciplineRepository disciplineRepository;
    private final ChatClient chatClient;

    public ProgrammeServiceImpl(ProgrammeRepository programmeRepository,
                                ProgrammeDisciplineRepository disciplineRepository,
                                ChatClient.Builder chatClientBuilder) {
        this.programmeRepository = programmeRepository;
        this.disciplineRepository = disciplineRepository;
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String chatWithGroq(String message) {
        return chatClient.prompt()
                .system("Tu es un assistant pour une plateforme d'administration systeme scolaire.")
                .user(message)
                .call()
                .content();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProgrammeResponse> list(Long etablissementId, Boolean actif, Pageable pageable) {
        Page<Programme> page;
        if (etablissementId != null && actif != null) {
            page = programmeRepository.findByEtablissementIdAndActif(etablissementId, actif, pageable);
        } else if (etablissementId != null) {
            page = programmeRepository.findByEtablissementId(etablissementId, pageable);
        } else {
            page = programmeRepository.findAll(pageable);
        }
        return page.map(this::toProgrammeResponseWithoutDisciplines);
    }

    @Transactional(readOnly = true)
    @Override
    public ProgrammeResponse get(Long id) {
        Programme p = programmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programme " + id + " introuvable"));
        ProgrammeResponse resp = toProgrammeResponseWithoutDisciplines(p);
        List<ProgrammeDiscipline> items = disciplineRepository.findByProgrammeId(p.getId());
        resp.setDisciplines(items.stream().map(this::toDisciplineResponse).toList());
        return resp;
    }

    @Override
    public ProgrammeResponse create(ProgrammeRequest request) {
        if (programmeRepository.existsByEtablissementIdAndNomAndNiveau(
                request.getEtablissementId(), request.getNom(), request.getNiveau())) {
            throw new ConflictException("Un programme avec le même nom et niveau existe déjà pour cet établissement");
        }

        Programme p = new Programme(
                request.getNom(),
                request.getNiveau(),
                request.getSpecialite(),
                request.getActif(),
                request.getEtablissementId(),
                request.getTotalHours() 
        );

        Programme saved = programmeRepository.save(p);
        return toProgrammeResponseWithoutDisciplines(saved);
    }

    @Override
    public ProgrammeResponse update(Long id, ProgrammeRequest request) {
        Programme p = programmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programme " + id + " introuvable"));

        if ((!p.getEtablissementId().equals(request.getEtablissementId()) ||
                !p.getNom().equals(request.getNom()) ||
                !p.getNiveau().equals(request.getNiveau()))
                && programmeRepository.existsByEtablissementIdAndNomAndNiveau(
                request.getEtablissementId(), request.getNom(), request.getNiveau())) {
            throw new ConflictException("Un programme avec le même nom et niveau existe déjà pour cet établissement");
        }

        p.setNom(request.getNom());
        p.setNiveau(request.getNiveau());
        p.setSpecialite(request.getSpecialite());
        p.setActif(request.getActif());
        p.setEtablissementId(request.getEtablissementId());
        p.setTotalHours(request.getTotalHours()); 

        Programme saved = programmeRepository.save(p);
        return toProgrammeResponseWithoutDisciplines(saved);
    }

    @Override
    public void delete(Long id) {
        Programme p = programmeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programme " + id + " introuvable"));
        programmeRepository.delete(p);
    }

    @Override
    public ProgrammeDisciplineResponse addDiscipline(Long programmeId, ProgrammeDisciplineRequest request) {
        Programme programme = programmeRepository.findById(programmeId)
                .orElseThrow(() -> new NotFoundException("Programme " + programmeId + " introuvable"));

        if (disciplineRepository.existsByProgrammeIdAndDisciplineId(programmeId, request.getDisciplineId())) {
            throw new ConflictException("Cette discipline est déjà associée au programme");
        }

        ProgrammeDiscipline d = new ProgrammeDiscipline(
                programme,
                request.getDisciplineId(),
                request.getHoursPerWeek(),
                request.getConsecutiveSlots(),
                request.getCore(),
                request.getRequiredRoomType()
        );
        ProgrammeDiscipline saved = disciplineRepository.save(d);
        return toDisciplineResponse(saved);
    }

    @Override
    public ProgrammeDisciplineResponse updateDiscipline(Long programmeId, Long disciplineItemId, ProgrammeDisciplineRequest request) {
        ProgrammeDiscipline d = disciplineRepository.findByIdAndProgrammeId(disciplineItemId, programmeId)
                .orElseThrow(() -> new NotFoundException("Association discipline " + disciplineItemId + " introuvable pour le programme " + programmeId));

        if (!d.getDisciplineId().equals(request.getDisciplineId())
                && disciplineRepository.existsByProgrammeIdAndDisciplineId(programmeId, request.getDisciplineId())) {
            throw new ConflictException("Cette discipline est déjà associée au programme");
        }

        d.setDisciplineId(request.getDisciplineId());
        d.setHoursPerWeek(request.getHoursPerWeek());
        d.setConsecutiveSlots(request.getConsecutiveSlots());
        d.setCore(request.getCore());
        d.setRequiredRoomType(request.getRequiredRoomType());

        ProgrammeDiscipline saved = disciplineRepository.save(d);
        return toDisciplineResponse(saved);
    }

    @Override
    public void removeDiscipline(Long programmeId, Long disciplineItemId) {
        ProgrammeDiscipline d = disciplineRepository.findByIdAndProgrammeId(disciplineItemId, programmeId)
                .orElseThrow(() -> new NotFoundException("Association discipline " + disciplineItemId + " introuvable pour le programme " + programmeId));
        disciplineRepository.delete(d);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProgrammeDisciplineResponse> listDisciplines(Long programmeId, Pageable pageable) {
        List<ProgrammeDisciplineResponse> all = disciplineRepository.findByProgrammeId(programmeId)
                .stream().map(this::toDisciplineResponse).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<ProgrammeDisciplineResponse> content = start > end ? List.of() : all.subList(start, end);

        return new org.springframework.data.domain.PageImpl<>(content, pageable, all.size());
    }

    private ProgrammeResponse toProgrammeResponseWithoutDisciplines(Programme p) {
        ProgrammeResponse r = new ProgrammeResponse();
        r.setId(p.getId());
        r.setNom(p.getNom());
        r.setNiveau(p.getNiveau());
        r.setSpecialite(p.getSpecialite());
        r.setActif(p.getActif());
        r.setEtablissementId(p.getEtablissementId());
        r.setTotalHours(p.getTotalHours());    
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }

    private ProgrammeDisciplineResponse toDisciplineResponse(ProgrammeDiscipline d) {
        ProgrammeDisciplineResponse r = new ProgrammeDisciplineResponse();
        r.setId(d.getId());
        r.setDisciplineId(d.getDisciplineId());
        r.setHoursPerWeek(d.getHoursPerWeek());
        r.setConsecutiveSlots(d.getConsecutiveSlots());
        r.setCore(d.getCore());
        r.setRequiredRoomType(d.getRequiredRoomType());
        r.setCreatedAt(d.getCreatedAt());
        r.setUpdatedAt(d.getUpdatedAt());
        return r;
    }
}
