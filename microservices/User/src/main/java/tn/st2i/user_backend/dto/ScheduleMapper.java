package tn.st2i.user_backend.mapper;

import tn.st2i.user_backend.dto.*;
import tn.st2i.user_backend.entity.*;

import java.util.stream.Collectors;

public class ScheduleMapper {

    public static ScheduleDTO toDTO(Schedule schedule) {
        if (schedule == null) return null;

        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setAcademicYear(schedule.getAcademicYear());
        dto.setTitle(schedule.getTitle());
        if (schedule.getClasse() != null) {
            dto.setClasseId(schedule.getClasse().getId());
        }

        dto.setItems(
                schedule.getItems().stream()
                        .map(ScheduleMapper::toDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    public static ScheduleItemDTO toDTO(ScheduleItem item) {
        if (item == null) return null;

        ScheduleItemDTO dto = new ScheduleItemDTO();
        dto.setId(item.getId());
        dto.setDayOfWeek(item.getDayOfWeek());
        dto.setStartTime(item.getStartTime());
        dto.setEndTime(item.getEndTime());
        dto.setRoom(item.getRoom());
        if (item.getTeacher() != null) {
            dto.setTeacherId(item.getTeacher().getId());
        }
        return dto;
    }
}
