package tn.st2i.user_backend.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
  name = "schedule_items",
  indexes = {
    @Index(name="idx_item_teacher", columnList = "teacher_id"),
    @Index(name="idx_item_day", columnList = "day_of_week")
  }
)
public class ScheduleItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "schedule_id")
   @JsonIgnore 
  private Schedule schedule;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false, length = 16)
  private DayOfWeek dayOfWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @ManyToOne(optional = false)
  @JoinColumn(name = "discipline_id")
  private Discipline discipline;

  // User with role TEACHER
  @ManyToOne(optional = false)
  @JoinColumn(name = "teacher_id")
  private User teacher;

  @Column(length = 64)
  private String room;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Schedule getSchedule() { return schedule; }
  public void setSchedule(Schedule schedule) { this.schedule = schedule; }
  public DayOfWeek getDayOfWeek() { return dayOfWeek; }
  public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
  public LocalTime getStartTime() { return startTime; }
  public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
  public LocalTime getEndTime() { return endTime; }
  public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
  public Discipline getDiscipline() { return discipline; }
  public void setDiscipline(Discipline discipline) { this.discipline = discipline; }
  public User getTeacher() { return teacher; }
  public void setTeacher(User teacher) { this.teacher = teacher; }
  public String getRoom() { return room; }
  public void setRoom(String room) { this.room = room; }
}
