package tn.st2i.user_backend.service;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Correct import for Spring's Transactional

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import tn.st2i.user_backend.entity.Classe;
import tn.st2i.user_backend.entity.CreateScheduleRequest;
import tn.st2i.user_backend.entity.Discipline;
import tn.st2i.user_backend.entity.Schedule;
import tn.st2i.user_backend.entity.ScheduleItem;
import tn.st2i.user_backend.entity.TeacherScheduleResponse;
import tn.st2i.user_backend.entity.User;
import tn.st2i.user_backend.repository.ClasseRepository;
import tn.st2i.user_backend.repository.DisciplineRepository;
import tn.st2i.user_backend.repository.ScheduleItemRepository;
import tn.st2i.user_backend.repository.ScheduleRepository;
import tn.st2i.user_backend.repository.UserRepository;
import tn.st2i.user_backend.util.Roles;

@Service
public class ScheduleService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepo;
    private final ScheduleItemRepository itemRepo;
    private final ClasseRepository classeRepo;
    private final DisciplineRepository disciplineRepo;
    private final UserRepository userRepo;

    public ScheduleService(
            ScheduleRepository scheduleRepo,
            ScheduleItemRepository itemRepo,
            ClasseRepository classeRepo,
            DisciplineRepository disciplineRepo,
            UserRepository userRepo
    ) {
        this.scheduleRepo = scheduleRepo;
        this.itemRepo = itemRepo;
        this.classeRepo = classeRepo;
        this.disciplineRepo = disciplineRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Long createSchedule(CreateScheduleRequest req) {
        try {
            // Find class or throw
            Classe classe = classeRepo.findById(req.classeId())
                    .orElseThrow(() -> new IllegalArgumentException("Classe not found: " + req.classeId()));

            // Check for existing schedule
            scheduleRepo.findByClasseIdAndAcademicYear(req.classeId(), req.academicYear())
                    .ifPresent(s -> { throw new IllegalStateException("Schedule already exists for class " + req.classeId() + " and year " + req.academicYear()); });

            // Create new schedule
            Schedule schedule = new Schedule();
            schedule.setClasse(classe);
            schedule.setAcademicYear(req.academicYear());
            schedule.setTitle(req.title());
            if (schedule.getItems() == null) {
                schedule.setItems(new ArrayList<>());
            }

            // Process each item
            for (CreateScheduleRequest.Item i : req.items()) {
                // Normalize and parse day
                String dayStr = i.dayOfWeek().toUpperCase();
                DayOfWeek day = DayOfWeek.valueOf(dayStr);

                // Parse times
                LocalTime start = LocalTime.parse(i.startTime());
                LocalTime end = LocalTime.parse(i.endTime());
                if (!end.isAfter(start)) {
                    throw new IllegalArgumentException("End must be after start for " + day);
                }

                // Find discipline and teacher
                Discipline d = disciplineRepo.findById(i.disciplineId())
                        .orElseThrow(() -> new IllegalArgumentException("Discipline not found: " + i.disciplineId()));
                User teacher = userRepo.findById(i.teacherId())
                        .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + i.teacherId()));

                // Role check
                if (teacher.getRole() == null || !Roles.TEACHER.equalsIgnoreCase(teacher.getRole().getName())) {
                    throw new IllegalArgumentException("User " + i.teacherId() + " is not a TEACHER");
                }

                // Check class overlap in payload
                boolean classPayloadOverlap = schedule.getItems().stream().anyMatch(
                        si -> si.getDayOfWeek() == day &&
                                si.getStartTime().isBefore(end) && si.getEndTime().isAfter(start)
                );
                if (classPayloadOverlap) {
                    throw new IllegalArgumentException("Class overlap within payload on " + day);
                }

                // Check teacher overlap in payload
                boolean teacherPayloadOverlap = schedule.getItems().stream().anyMatch(
                        si -> si.getTeacher().getId().equals(teacher.getId()) &&
                                si.getDayOfWeek() == day &&
                                si.getStartTime().isBefore(end) && si.getEndTime().isAfter(start)
                );
                if (teacherPayloadOverlap) {
                    throw new IllegalArgumentException("Teacher overlap within payload on " + day);
                }

                // Check room overlap in payload (assuming room is unique and not null)
                if (i.room() != null && !i.room().isEmpty()) {
                    boolean roomPayloadOverlap = schedule.getItems().stream().anyMatch(
                            si -> si.getRoom() != null && si.getRoom().equals(i.room()) &&
                                    si.getDayOfWeek() == day &&
                                    si.getStartTime().isBefore(end) && si.getEndTime().isAfter(start)
                    );
                    if (roomPayloadOverlap) {
                        throw new IllegalArgumentException("Room overlap within payload on " + day);
                    }
                }

                // Create item
                ScheduleItem si = new ScheduleItem();
                si.setDayOfWeek(day);
                si.setStartTime(start);
                si.setEndTime(end);
                si.setDiscipline(d);
                si.setTeacher(teacher);
                si.setRoom(i.room());

                // Set bidirectional relationship
                si.setSchedule(schedule);
                schedule.addItem(si);

                log.debug("Added item for {}: {} - {} with teacher {}", day, start, end, teacher.getId());
            }

            // Save schedule
            Schedule savedSchedule = scheduleRepo.save(schedule);
            scheduleRepo.flush();

            // Post-save checks for teacher conflicts with existing schedules
            for (ScheduleItem si : savedSchedule.getItems()) {
                boolean teacherConflict = itemRepo.existsOverlapForTeacherExcluding(
                        si.getTeacher().getId(),
                        savedSchedule.getAcademicYear(),
                        si.getDayOfWeek(),
                        si.getStartTime(),
                        si.getEndTime(),
                        si.getId()
                );
                if (teacherConflict) {
                    throw new IllegalStateException("Teacher overlap with existing schedule for " + si.getTeacher().getFirstName());
                }

                // Added: Post-save check for room conflicts with existing schedules
                if (si.getRoom() != null && !si.getRoom().isEmpty()) {
                    boolean roomConflict = itemRepo.existsOverlapForRoomExcluding(
                            si.getDayOfWeek(),
                            si.getStartTime(),
                            si.getEndTime(),
                            si.getRoom(),
                            si.getId()
                    );
                    if (roomConflict) {
                        throw new IllegalStateException("Room overlap with existing schedule on " + si.getDayOfWeek());
                    }
                }
            }

            log.info("Schedule created successfully with ID: {}", savedSchedule.getId());
            return savedSchedule.getId();

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation during schedule save: {}", e.getMessage(), e);
            throw new IllegalStateException("Database constraint violated (e.g., null foreign key). Check relationships.", e);
        } catch (Exception e) {
            log.error("Unexpected error creating schedule: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to create schedule: " + e.getMessage(), e);
        }
    }
    @Transactional
    public Schedule generateScheduleAutomatically(Long classId) {
        try {
            // Determine academic year based on current date (September 14, 2025 -> "2025-2026")
            String academicYear = "2025-2026";

            // Find class or throw
            Classe classe = classeRepo.findById(classId)
                    .orElseThrow(() -> new IllegalArgumentException("Classe not found: " + classId));

            // Check for existing schedule
            scheduleRepo.findByClasseIdAndAcademicYear(classId, academicYear)
                    .ifPresent(s -> { throw new IllegalStateException("Schedule already exists for class " + classId + " and year " + academicYear); });

            // Create new schedule
            Schedule schedule = new Schedule();
            schedule.setClasse(classe);
            schedule.setAcademicYear(academicYear);
            schedule.setTitle("Emploi du temps  " + classe.getNom());
            if (schedule.getItems() == null) {
                schedule.setItems(new ArrayList<>());
            }

            // Fetch all disciplines :: add Niveau ! if needed !!
            List<Discipline> disciplines = disciplineRepo.findAllByNiveau(classe.getNiveau());

            // Fetch all teachers
            List<User> allTeachers = userRepo.findAllByRegion_Id(classe.getEtablissement().getRegion().getId())
                    .stream()
                    .filter(u -> u.getRole() != null && Roles.TEACHER.equalsIgnoreCase(u.getRole().getName()))
                    .toList();
            if (allTeachers.isEmpty()) {
                throw new IllegalStateException("No teachers available");
            }

            // Available rooms (hardcoded for simplicity)
            List<String> rooms = List.of(
                    "Room 101", "Room 102", "Room 103", "Room 104", "Room 105",
                    "Room 110", "Room 109", "Room 108", "Room 107", "Room 106"

            );

            // Days: Monday to Friday
            DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};

            // Time slots: 9:00 to 17:00 (1-hour slots, starting from 9:00 to 16:00)
            List<LocalTime> startTimes = new ArrayList<>();
            for (int h = 9; h < 17; h++) {
                startTimes.add(LocalTime.of(h, 0));
            }
            // For each discipline, assign the required number of 1-hour sessions
            for (Discipline d : disciplines) {
                int requiredHours = d.getRequiredHours();
                int assignedHours = 0;

                while (assignedHours < requiredHours) {
                    boolean assigned = false;

                    List<User> teachers = userRepo.findByDiscipline_IdAndRegion_Id(d.getId(),classe.getEtablissement().getRegion().getId());
                    if (teachers.isEmpty()) {
                        throw new IllegalStateException("No teachers available for discipline " + d.getNom() +" in  region : "  +classe.getEtablissement().getRegion().getNom());
                    }

                    dayLoop: for (DayOfWeek day : days) {
                        // NEW: Check max hours per day for this discipline (max 2 hours/day)
                        long currentHoursForThisDay = schedule.getItems().stream()
                                .filter(si -> si.getDiscipline().getId().equals(d.getId()) && si.getDayOfWeek() == day)
                                .count();  // Each item is 1 hour, so count == hours
                        if (currentHoursForThisDay >= 2) {
                            continue;  // Skip this day; try next day
                        }

                        for (LocalTime start : startTimes) {
                            LocalTime end = start.plusHours(1);

                            // Check class overlap within this new schedule (payload)
                            boolean classOverlap = schedule.getItems().stream().anyMatch(
                                    si -> si.getDayOfWeek() == day &&
                                            si.getStartTime().isBefore(end) && si.getEndTime().isAfter(start)
                            );
                            if (classOverlap) continue;

                            for (User teacher : teachers) {
                                // NEW: Check max classes per teacher in total (max 5 classes across all schedules)
                                long currentTeacherClasses = scheduleRepo.countByTeacherIdAndAcademicYearExcludingClasse(
                                        teacher.getId(), academicYear, classId);  // Assuming repo method counts schedules for teacher in year excluding this class
                                if (currentTeacherClasses >= 5) {
                                    continue;  // Skip this teacher; would exceed 5 classes total
                                }

                                // Check teacher availability (no overlap with existing schedules)
                                boolean teacherOverlap = itemRepo.existsOverlapForTeacherExcluding(
                                        teacher.getId(),
                                        academicYear,
                                        day,
                                        start,
                                        end,
                                        null // Null to check all (assume repo handles as no exclude)
                                );
                                if (teacherOverlap) continue;

                                // Check teacher overlap within payload
                                boolean teacherPayloadOverlap = schedule.getItems().stream().anyMatch(
                                        si -> si.getTeacher().getId().equals(teacher.getId()) &&
                                                si.getDayOfWeek() == day &&
                                                si.getStartTime().isBefore(end) && si.getEndTime().isAfter(start)
                                );
                                if (teacherPayloadOverlap) continue;

                                for (String room : rooms) {
                                    // Check room availability (no overlap with existing schedules)
                                    boolean roomOverlap = itemRepo.existsOverlapForRoomExcluding(
                                            day,
                                            start,
                                            end,
                                            room,
                                            null // Null to check all
                                    );
                                    if (roomOverlap) continue;

                                    // Check room overlap within payload
                                    boolean roomPayloadOverlap = schedule.getItems().stream().anyMatch(
                                            si -> si.getRoom() != null && si.getRoom().equals(room) &&
                                                    si.getDayOfWeek() == day &&
                                                    si.getStartTime().isBefore(end) && si.getEndTime().isAfter(start)
                                    );
                                    if (roomPayloadOverlap) continue;

                                    // Assign the item
                                    ScheduleItem si = new ScheduleItem();
                                    si.setDayOfWeek(day);
                                    si.setStartTime(start);
                                    si.setEndTime(end);
                                    si.setDiscipline(d);
                                    si.setTeacher(teacher);
                                    si.setRoom(room);
                                    si.setSchedule(schedule);
                                    schedule.addItem(si);

                                    log.debug("Auto-added item for {}: {} - {} with teacher {} for discipline {}", day, start, end, teacher.getId(), d.getNom());

                                    assigned = true;
                                    assignedHours++;
                                    break dayLoop; // Break out of all nested loops for this session
                                }
                            }
                        }
                    }

                    if (!assigned) {
                        throw new IllegalStateException("Cannot assign session for discipline " + d.getNom() + ": No free slot/teacher/room found");
                    }
                }
            }

            // Save schedule
            Schedule savedSchedule = scheduleRepo.save(schedule);
            scheduleRepo.flush();

            // Post-save checks (though pre-checks should prevent issues)
            for (ScheduleItem si : savedSchedule.getItems()) {
                boolean teacherConflict = itemRepo.existsOverlapForTeacherExcluding(
                        si.getTeacher().getId(),
                        savedSchedule.getAcademicYear(),
                        si.getDayOfWeek(),
                        si.getStartTime(),
                        si.getEndTime(),
                        si.getId()
                );
                if (teacherConflict) {
                    throw new IllegalStateException("Teacher overlap with existing schedule for " + si.getTeacher().getFirstName());
                }

                // NEW: Post-save check for teacher max classes
                long totalTeacherClasses = scheduleRepo.countByTeacherIdAndAcademicYear(si.getTeacher().getId(), savedSchedule.getAcademicYear());
                if (totalTeacherClasses > 5) {
                    throw new IllegalStateException("Teacher " + si.getTeacher().getFirstName() + " exceeds max 5 classes in total");
                }

                if (si.getRoom() != null && !si.getRoom().isEmpty()) {
                    boolean roomConflict = itemRepo.existsOverlapForRoomExcluding(
                            si.getDayOfWeek(),
                            si.getStartTime(),
                            si.getEndTime(),
                            si.getRoom(),
                            si.getId()
                    );
                    if (roomConflict) {
                        throw new IllegalStateException("Room overlap with existing schedule on " + si.getDayOfWeek());
                    }
                }
            }

            log.info("Auto-generated schedule created successfully with ID: {}", savedSchedule.getId());
            return savedSchedule;

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation during auto-schedule save: {}", e.getMessage(), e);
            throw new IllegalStateException("Database constraint violated (e.g., null foreign key). Check relationships.", e);
        } catch (Exception e) {
            log.error("Unexpected error creating auto-schedule: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to create auto-schedule: " + e.getMessage(), e);
        }
    }
    public byte[] generateSchedulePdf(Long classId, String academicYear) {
        try {
            // Fetch the schedule
            Schedule schedule = scheduleRepo.findByClasseIdAndAcademicYear(classId, academicYear)
                    .orElseThrow(() -> new IllegalArgumentException("No schedule found for class " + classId + " and year " + academicYear));

            // Prepare PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph("Schedule for Class: " + schedule.getClasse().getNom() + " - Year: " + academicYear);
            title.setFontSize(18).setBold().setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Create table: 6 columns (Time + 5 days)
            float[] columnWidths = {1, 2, 2, 2, 2, 2};
            Table table = new Table(columnWidths);
            table.setWidth(100f); // Full width

            // Headers
            String[] headers = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            for (String header : headers) {
                Cell cell = new Cell().add(new Paragraph(header));
                cell.setBold().setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
                table.addCell(cell);
            }

            // Time slots from 9:00 to 16:00 (assuming 1-hour slots up to 17:00 end)
            List<LocalTime> startTimes = new ArrayList<>();
            for (int h = 9; h < 17; h++) {
                startTimes.add(LocalTime.of(h, 0));
            }

            DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};

            // Fill rows
            for (LocalTime start : startTimes) {
                LocalTime end = start.plusHours(1);
                String timeSlot = start.toString() + " - " + end.toString();

                // Time cell
                Cell timeCell = new Cell().add(new Paragraph(timeSlot));
                timeCell.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
                table.addCell(timeCell);

                // For each day
                for (DayOfWeek day : days) {
                    String content = "";
                    for (ScheduleItem item : schedule.getItems()) {
                        if (item.getDayOfWeek() == day && item.getStartTime().equals(start)) {
                            String teacherName = (item.getTeacher().getFirstName() != null ? item.getTeacher().getFirstName() : "") + " " +
                                    (item.getTeacher().getLastName() != null ? item.getTeacher().getLastName() : "");
                            content = item.getDiscipline().getNom() + "\n" + teacherName.trim() + "\n" + item.getRoom();
                            break; // Assume one item per slot
                        }
                    }
                    Cell dayCell = new Cell().add(new Paragraph(content));
                    dayCell.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
                    table.addCell(dayCell);
                }
            }

            document.add(table);
            document.close();

            log.info("PDF generated for schedule of class {} and year {}", classId, academicYear);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    public TeacherScheduleResponse getTeacherSchedule(Long teacherId, String year) {
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        var items = itemRepo.findForTeacher(teacherId, year);

        var slots = items.stream()
                .map(si -> new TeacherScheduleResponse.Slot(
                        si.getDayOfWeek().name(),
                        si.getStartTime().toString(),
                        si.getEndTime().toString(),
                        si.getSchedule().getClasse().getNom(),
                        si.getDiscipline().getNom(),
                        si.getRoom()
                )).toList();

        String teacherName = (teacher.getFirstName() != null ? teacher.getFirstName() : "")
                + " "
                + (teacher.getLastName() != null ? teacher.getLastName() : "");
        return new TeacherScheduleResponse(teacherId, teacherName.trim(), year, slots);
    }

    public List<ScheduleItem> getSchedulesByClassId(Long classId, String year) {
        Schedule schedule = scheduleRepo.findByClasseIdAndAcademicYear(classId, year)
                .orElseThrow(() -> new IllegalArgumentException("No schedule found for classId " + classId + " and year " + year));
        return new ArrayList<>(schedule.getItems());
    }
    public Optional<Schedule> getScheduleByClassId(Long classId) {
        return scheduleRepo.getSchedulesByClasseId(classId);
    }
    public List<Schedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }
    public boolean hasSchedule(Long classId) {
        return scheduleRepo.existsByClasse_Id(classId);
    }
}