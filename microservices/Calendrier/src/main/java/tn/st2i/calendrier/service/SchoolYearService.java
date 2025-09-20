package tn.st2i.calendrier.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.st2i.calendrier.dto.ClasseDto;
import tn.st2i.calendrier.dto.SchoolYearDto;
import tn.st2i.calendrier.entity.SchoolYear;
import tn.st2i.calendrier.mapper.SchoolYearMapper;
import tn.st2i.calendrier.repository.SchoolYearRepository;

import java.util.List;
import java.util.stream.Collectors;

import tn.st2i.calendrier.client.UserServiceClient;

@Service
public class SchoolYearService {
    private final SchoolYearRepository repo;
    private final UserServiceClient userServiceClient;

    public SchoolYearService(SchoolYearRepository repo, UserServiceClient userServiceClient) {
        this.repo = repo;
        this.userServiceClient = userServiceClient;
    }


    @Transactional
    public SchoolYearDto create(SchoolYearDto dto) {
        SchoolYear entity = toEntity(dto);

        if (dto.getClasseId() != null) {
            ClasseDto cls = userServiceClient.getClasseById(dto.getClasseId(), null);
            if (cls == null) throw new IllegalArgumentException("Classe not found: " + dto.getClasseId());
            entity.setClasseId(cls.getId());
            entity.setNiveau(cls.getNiveau());
        }

        SchoolYear saved = repo.save(entity);
        return toDto(saved);
    }

    public List<SchoolYearDto> getAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    public SchoolYearDto getById(Long id) {
        return toDto(repo.findById(id).orElseThrow());
    }

    @Transactional
    public SchoolYearDto update(Long id, SchoolYearDto dto) {
        SchoolYear e = repo.findById(id).orElseThrow();
        e.setYear(dto.getYear());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setDivisionType(dto.getDivisionType());
        if (dto.getClasseId() != null && !dto.getClasseId().equals(e.getClasseId())) {
            ClasseDto cls = userServiceClient.getClasseById(dto.getClasseId(), null);
            e.setClasseId(cls.getId());
            e.setNiveau(cls.getNiveau());
        }
        return toDto(repo.save(e));
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }


    private SchoolYear toEntity(SchoolYearDto dto) {
        if (dto == null) return null;
        SchoolYear e = new SchoolYear();
        e.setId(dto.getId());
        e.setYear(dto.getYear());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setDivisionType(dto.getDivisionType());
        e.setClasseId(dto.getClasseId()); 
        e.setNiveau(dto.getNiveau());     
        return e;
    }

    private SchoolYearDto toDto(SchoolYear e) {
        if (e == null) return null;
        SchoolYearDto dto = new SchoolYearDto();
        dto.setId(e.getId());
        dto.setYear(e.getYear());
        dto.setHolidays(
                e.getHolidays()
                        .stream()
                        .map(SchoolYearMapper::toDto) // map each holiday to DTO
                        .toList()
                );
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        dto.setDivisionType(e.getDivisionType());
        dto.setClasseId(e.getClasseId());
        dto.setNiveau(e.getNiveau());
        return dto;
    }
    @Transactional(readOnly = true)
public SchoolYearDto findSchoolYearByClasseId(Long classeId) {
    return repo.findTopByClasseIdOrderByStartDateDesc(classeId)
               .map(this::toDto)
               .orElseThrow(() -> new IllegalArgumentException(
                   "No school year found for classeId=" + classeId
               ));
}


}
