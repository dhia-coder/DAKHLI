package tn.st2i.calendrier.mapper;

import tn.st2i.calendrier.dto.HolidayDto;
import tn.st2i.calendrier.dto.SchoolYearDto;
import tn.st2i.calendrier.dto.SemesterDto;
import tn.st2i.calendrier.entity.Holiday;
import tn.st2i.calendrier.entity.SchoolYear;
import tn.st2i.calendrier.entity.Semester;

import java.util.stream.Collectors;

public class SchoolYearMapper {
    public static SchoolYearDto toDto(SchoolYear entity) {
        SchoolYearDto dto = new SchoolYearDto();
        dto.setId(entity.getId());
        dto.setYear(entity.getYear());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDivisionType(entity.getDivisionType());
        dto.setHolidays(entity.getHolidays().stream().map(SchoolYearMapper::toDto).collect(Collectors.toList()));
        dto.setSemesters(entity.getSemesters().stream().map(SchoolYearMapper::toDto).collect(Collectors.toList()));
        return dto;
    }

    public static HolidayDto toDto(Holiday entity) {
        HolidayDto dto = new HolidayDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setType(entity.getType());
        dto.setColor(entity.getColor());
        return dto;
    }

    public static SemesterDto toDto(Semester entity) {
        SemesterDto dto = new SemesterDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }

    // Mapping inverses
    public static SchoolYear toEntity(SchoolYearDto dto) {
        SchoolYear entity = new SchoolYear();
        entity.setYear(dto.getYear());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDivisionType(dto.getDivisionType());
        if (dto.getHolidays() != null) {
            for (HolidayDto h : dto.getHolidays()) {
                Holiday holiday = toEntity(h);
                holiday.setSchoolYear(entity);
                entity.getHolidays().add(holiday);
            }
        }
        if (dto.getSemesters() != null) {
            for (SemesterDto s : dto.getSemesters()) {
                Semester semester = toEntity(s);
                semester.setSchoolYear(entity);
                entity.getSemesters().add(semester);
            }
        }
        return entity;
    }

    public static Holiday toEntity(HolidayDto dto) {
        Holiday entity = new Holiday();
        entity.setName(dto.getName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setType(dto.getType());
        entity.setColor(dto.getColor());
        return entity;
    }

    public static Semester toEntity(SemesterDto dto) {
        Semester entity = new Semester();
        entity.setName(dto.getName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }
}

