package tn.st2i.programScholaire.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

// tn/st2i/programScholaire/entity/Emploi.java
@Entity
@Table(name = "emplois",
       indexes = {
         @Index(name="idx_emploi_date_programme", columnList = "date_cours, programme_id"),
         @Index(name="idx_emploi_teacher_date", columnList = "teacher_user_id,date_cours"),
         @Index(name="idx_emploi_classe_date", columnList = "classe_id,date_cours")
       })
public class Emploi {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "programme_id", nullable = false)
  private Programme programme;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "programme_discipline_id", nullable = false)
  private ProgrammeDiscipline programmeDiscipline;

  // 🔁 user-service reference (teacher)
  @NotNull
  @Column(name = "teacher_user_id", nullable = false)
  private Long teacherUserId;

  // 🔁 user-service reference (which class this session is for)
  @NotNull
  @Column(name = "classe_id", nullable = false)
  private Long classeId;

  @NotNull @Column(name="date_cours", nullable=false)
  private LocalDate dateCours;

  @NotNull @Column(name="heure_debut", nullable=false)
  private LocalTime heureDebut;

  @NotNull @Column(name="heure_fin", nullable=false)
  private LocalTime heureFin;

  @Column(name="salle")
  private String salle;

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

  public ProgrammeDiscipline getProgrammeDiscipline() {
    return programmeDiscipline;
  }

  public void setProgrammeDiscipline(ProgrammeDiscipline programmeDiscipline) {
    this.programmeDiscipline = programmeDiscipline;
  }

  public Long getTeacherUserId() {
    return teacherUserId;
  }

  public void setTeacherUserId(Long teacherUserId) {
    this.teacherUserId = teacherUserId;
  }

  public Long getClasseId() {
    return classeId;
  }

  public void setClasseId(Long classeId) {
    this.classeId = classeId;
  }

  public LocalDate getDateCours() {
    return dateCours;
  }

  public void setDateCours(LocalDate dateCours) {
    this.dateCours = dateCours;
  }

  public LocalTime getHeureDebut() {
    return heureDebut;
  }

  public void setHeureDebut(LocalTime heureDebut) {
    this.heureDebut = heureDebut;
  }

  public LocalTime getHeureFin() {
    return heureFin;
  }

  public void setHeureFin(LocalTime heureFin) {
    this.heureFin = heureFin;
  }

  public String getSalle() {
    return salle;
  }

  public void setSalle(String salle) {
    this.salle = salle;
  }

  

}
