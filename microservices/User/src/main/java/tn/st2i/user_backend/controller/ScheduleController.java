package tn.st2i.user_backend.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.st2i.user_backend.entity.CreateScheduleRequest;
import tn.st2i.user_backend.service.ScheduleService;
import tn.st2i.user_backend.dto.*;
import tn.st2i.user_backend.entity.*;
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
  private final ScheduleService service;
  public ScheduleController(ScheduleService service) { this.service = service; }
  @GetMapping("/{classeId}/exists")
    public ResponseEntity<?> HasSchedule(@PathVariable Long classeId) {
    return ResponseEntity.ok(service.hasSchedule(classeId));
    }
  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateScheduleRequest req) {
    try {
      var scheduleDetails = service.getScheduleByClassId(req.classeId());
      Schedule schedule = scheduleDetails
              .orElseGet(() ->
                      service.generateScheduleAutomatically(req.classeId())
              );
      return ResponseEntity.ok(schedule);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping(value = "/{classeId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<?> generatePdf(
          @PathVariable Long classeId,
          @RequestParam String academicYear) {
    try {
      byte[] pdfBytes = service.generateSchedulePdf(classeId, academicYear);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("attachment", "schedule_" + classeId + ".pdf");
      headers.setContentLength(pdfBytes.length);

      return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("/teachers/{teacherId}")
  public ResponseEntity<?> teacherView(@PathVariable Long teacherId,
                                       @RequestParam String year) {
    return ResponseEntity.ok(service.getTeacherSchedule(teacherId, year));
  }
  @GetMapping
public ResponseEntity<?> getSchedulesByClassId(@RequestParam Long classId, @RequestParam String year) {
    return ResponseEntity.ok(service.getSchedulesByClassId(classId, year));
}



}
