package tn.st2i.user_backend.entity;

import java.util.List;

public record TeacherScheduleResponse(
  Long teacherId,
  String teacherName,
  String academicYear,
  List<Slot> slots
) {
  public record Slot(String dayOfWeek, String startTime, String endTime,
                     String classe, String discipline, String room){}
}
