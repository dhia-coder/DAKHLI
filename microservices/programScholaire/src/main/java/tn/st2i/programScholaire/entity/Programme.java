package tn.st2i.programScholaire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
    name = "programmes",
    indexes = {
        @Index(name = "idx_programme_etablissement_actif", columnList = "etablissement_id, actif")
    }
)
public class Programme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du programme est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le niveau est obligatoire")
    @Column(nullable = false)
    private String niveau;

    @Column(name = "specialite")
    private String specialite;

    @NotNull(message = "Le statut actif est obligatoire")
    @Column(nullable = false)
    private Boolean actif = true;

    @NotNull(message = "L'établissement est obligatoire")
    @Column(name = "etablissement_id", nullable = false)
    private Long etablissementId;

    @NotNull(message = "Le nombre total d'heures est obligatoire")
    @Column(name = "total_hours", nullable = false)
    private Integer totalHours;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "programme", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProgrammeDiscipline> disciplines = new ArrayList<>();

    // --- Lifecycle hooks ---
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public Programme() {}

    public Programme(String nom,
                     String niveau,
                     String specialite,
                     Boolean actif,
                     Long etablissementId,
                     Integer totalHours) {
        this.nom = nom;
        this.niveau = niveau;
        this.specialite = specialite;
        this.actif = actif;
        this.etablissementId = etablissementId;
        this.totalHours = totalHours;
    }


    public Programme(String nom,
                     String niveau,
                     String specialite,
                     Boolean actif,
                     Long etablissementId) {
        this(nom, niveau, specialite, actif, etablissementId, 0);
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

    public Long getEtablissementId() { return etablissementId; }
    public void setEtablissementId(Long etablissementId) { this.etablissementId = etablissementId; }

    public Integer getTotalHours() { return totalHours; }
    public void setTotalHours(Integer totalHours) { this.totalHours = totalHours; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ProgrammeDiscipline> getDisciplines() { return disciplines; }
    public void setDisciplines(List<ProgrammeDiscipline> disciplines) { this.disciplines = disciplines; }

    // --- Helper methods ---
    public void addDiscipline(ProgrammeDiscipline discipline) {
        disciplines.add(discipline);
        discipline.setProgramme(this);
    }

    public void removeDiscipline(ProgrammeDiscipline discipline) {
        disciplines.remove(discipline);
        discipline.setProgramme(null);
    }
}
