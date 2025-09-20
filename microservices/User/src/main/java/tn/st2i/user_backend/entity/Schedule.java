package tn.st2i.user_backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
  name = "schedules",
  uniqueConstraints = @UniqueConstraint(name = "uk_schedule_class_year",
                                        columnNames = {"classe_id","academic_year"})
)
public class Schedule {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "classe_id")
  @JsonManagedReference

  private Classe classe;

  @Column(name = "academic_year", nullable = false, length = 16)
  private String academicYear;

  private String title;

  @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ScheduleItem> items = new ArrayList<>();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Classe getClasse() { return classe; }
  public void setClasse(Classe classe) { this.classe = classe; }
  public String getAcademicYear() { return academicYear; }
  public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public List<ScheduleItem> getItems() { return items; }
  public void setItems(List<ScheduleItem> items) { this.items = items; }
  public void addItem(ScheduleItem item) {
    if (item != null) {
      items.add(item);
      item.setSchedule(this);  // Ensure owning side is set (redundant but safe)
    }
}}
