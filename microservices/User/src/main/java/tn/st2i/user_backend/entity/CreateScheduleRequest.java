package tn.st2i.user_backend.entity;

import java.util.List;

public record CreateScheduleRequest(
  Long classeId,
  String academicYear,
  String title,
  List<Item> items
) {
  public record Item(
    String dayOfWeek,      
    String startTime,      
    String endTime,       
    Long disciplineId,
    Long teacherId,
    String room
  ) {}
}
