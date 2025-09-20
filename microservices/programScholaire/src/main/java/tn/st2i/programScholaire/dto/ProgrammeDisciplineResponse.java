
package tn.st2i.programScholaire.dto;

import java.time.LocalDateTime;

public class ProgrammeDisciplineResponse {
    private Long id;
    private Long disciplineId;
    private Integer hoursPerWeek;
    private Boolean consecutiveSlots;
    private Boolean core;
    private String requiredRoomType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDisciplineId() { return disciplineId; }
    public void setDisciplineId(Long disciplineId) { this.disciplineId = disciplineId; }
    public Integer getHoursPerWeek() { return hoursPerWeek; }
    public void setHoursPerWeek(Integer hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }
    public Boolean getConsecutiveSlots() { return consecutiveSlots; }
    public void setConsecutiveSlots(Boolean consecutiveSlots) { this.consecutiveSlots = consecutiveSlots; }
    public Boolean getCore() { return core; }
    public void setCore(Boolean core) { this.core = core; }
    public String getRequiredRoomType() { return requiredRoomType; }
    public void setRequiredRoomType(String requiredRoomType) { this.requiredRoomType = requiredRoomType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
