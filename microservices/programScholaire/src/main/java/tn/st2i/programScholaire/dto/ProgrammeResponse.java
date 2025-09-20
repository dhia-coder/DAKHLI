
package tn.st2i.programScholaire.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProgrammeResponse {
    private Long id;
    private String nom;
    private String niveau;
    private String specialite;
    private Boolean actif;
    private Long etablissementId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProgrammeDisciplineResponse> disciplines;
  private Integer totalHours;

    public Integer getTotalHours() { return totalHours; }
    public void setTotalHours(Integer totalHours) { this.totalHours = totalHours; }

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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<ProgrammeDisciplineResponse> getDisciplines() { return disciplines; }
    public void setDisciplines(List<ProgrammeDisciplineResponse> disciplines) { this.disciplines = disciplines; }
}
