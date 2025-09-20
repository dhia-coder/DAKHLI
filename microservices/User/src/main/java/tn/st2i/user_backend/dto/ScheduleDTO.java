package tn.st2i.user_backend.dto;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDTO {

    private Long id;
    private Long classeId;          // Only store the ID of Classe
    private String classeName;      // Optional, for display
    private String academicYear;
    private String title;
    private List<ScheduleItemDTO> items = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClasseId() { return classeId; }
    public void setClasseId(Long classeId) { this.classeId = classeId; }

    public String getClasseName() { return classeName; }
    public void setClasseName(String classeName) { this.classeName = classeName; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<ScheduleItemDTO> getItems() { return items; }
    public void setItems(List<ScheduleItemDTO> items) { this.items = items; }
}