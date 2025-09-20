
package tn.st2i.programScholaire.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProgrammeDisciplineRequest {
    @NotNull private Long disciplineId;
    @NotNull @Min(1) private Integer hoursPerWeek;
    private Boolean consecutiveSlots = false;
    @NotNull private Boolean core;
    private String requiredRoomType;

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
}
