package tn.st2i.programScholaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "programme_disciplines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_programme_discipline", columnNames = {"programme_id", "discipline_id"})
        })
public class ProgrammeDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le programme est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id", nullable = false)
    private Programme programme;

    @NotNull(message = "La discipline est obligatoire")
    @Column(name = "discipline_id", nullable = false)
    private Long disciplineId;

    @NotNull(message = "Le nombre d'heures par semaine est obligatoire")
    @Min(value = 1, message = "Le nombre d'heures par semaine doit être au moins 1")
    @Column(name = "hours_per_week", nullable = false)
    private Integer hoursPerWeek;

    @Column(name = "consecutive_slots")
    private Boolean consecutiveSlots = false;

    @NotNull(message = "Le statut tronc commun est obligatoire")
    @Column(name = "core", nullable = false)
    private Boolean core = false;

    @Column(name = "required_room_type")
    private String requiredRoomType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public ProgrammeDiscipline() {}

    public ProgrammeDiscipline(Programme programme, Long disciplineId, Integer hoursPerWeek,
                               Boolean consecutiveSlots, Boolean core, String requiredRoomType) {
        this.programme = programme;
        this.disciplineId = disciplineId;
        this.hoursPerWeek = hoursPerWeek;
        this.consecutiveSlots = consecutiveSlots;
        this.core = core;
        this.requiredRoomType = requiredRoomType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public Boolean getConsecutiveSlots() {
        return consecutiveSlots;
    }

    public void setConsecutiveSlots(Boolean consecutiveSlots) {
        this.consecutiveSlots = consecutiveSlots;
    }

    public Boolean getCore() {
        return core;
    }

    public void setCore(Boolean core) {
        this.core = core;
    }

    public String getRequiredRoomType() {
        return requiredRoomType;
    }

    public void setRequiredRoomType(String requiredRoomType) {
        this.requiredRoomType = requiredRoomType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

