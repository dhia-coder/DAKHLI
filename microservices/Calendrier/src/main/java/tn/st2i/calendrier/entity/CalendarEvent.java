package tn.st2i.calendrier.entity;
import jakarta.persistence.*;
import lombok.*;
import tn.st2i.calendrier.enums.RecurrenceType;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    private Integer recurrenceInterval;
}
