package tn.st2i.calendrier.dto;

import tn.st2i.calendrier.entity.CalendarEvent;
import tn.st2i.calendrier.entity.DivisionType;
import tn.st2i.calendrier.entity.ExamPeriod;

import java.time.LocalDate;
import java.util.List;

public class SchoolYearDto {
    private Long id;
    private String year;
    private LocalDate startDate;
    private LocalDate endDate;
    private DivisionType divisionType;
    private List<HolidayDto> holidays;
    private List<SemesterDto> semesters;
    private Long classeId;
    private String niveau;

    public String getNiveau() {
        return niveau;

    }
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    
    public Long getClasseId() {
        return classeId;
    }
    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DivisionType getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(DivisionType divisionType) {
        this.divisionType = divisionType;
    }

    public List<HolidayDto> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<HolidayDto> holidays) {
        this.holidays = holidays;
    }

    public List<SemesterDto> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<SemesterDto> semesters) {
        this.semesters = semesters;
    }
}
