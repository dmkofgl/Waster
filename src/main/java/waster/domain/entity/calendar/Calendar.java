package waster.domain.entity.calendar;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Calendar {
    public static Date START_DATE = new Date();
    @Getter
    private List<Schedule> schedules = new ArrayList<>();

    public void addOperation(Operation operation) {
        Schedule newTask = applyOperation(operation);
        schedules.add(newTask);
    }

    @Deprecated
    public void addSchedule(Schedule newSchedule) {
        schedules.add(newSchedule);
    }

    public Schedule applyOperation(Operation operation) {
        operation.setInitialStartDate(calculateStartTimeForOperation(operation));
        Schedule newTask = new Schedule(operation);
        schedules.add(newTask);

        return newTask;
    }

    public Long calculateStartTimeForOperation(Operation operation) {
        Operation newOperation = Operation.builder()
                .initialStartDate(operation.getInitialStartDate())
                .length(operation.getLength())
                .setting(operation.getSetting())
                .build();

        Schedule newTask = new Schedule(newOperation);
        calculateInitialStartTime(newTask);
        return newTask.getStart();
    }

    private void calculateInitialStartTime(Schedule newTask) {
        Optional<Schedule> overlappedSchedule = schedules.stream()
                .filter(schedule -> isOverlap(schedule, newTask))
                .findFirst();
        if (overlappedSchedule.isPresent()) {
            Schedule engaged = overlappedSchedule.get();
            newTask.setStart(engaged.getEnd()+1);
            calculateInitialStartTime(newTask);
        }
    }

    //TODO WTF???
    private boolean isOverlap(Schedule existSchedule, Schedule newSchedule) {
        Long startA = existSchedule.getStart();
        Long endA = existSchedule.getEnd();
        Long startB = newSchedule.getStart();
        Long endB = newSchedule.getEnd();

        return max(startA, startB) <= min(endA, endB);
    }

    private Long max(Long a, Long b) {
        return a > b ? a : b;
    }

    private Long min(Long a, Long b) {
        return a < b ? a : b;
    }

    public Long lastActionEndTime() {
        return schedules.stream()
                .map(Schedule::getEnd)
                .max(Long::compareTo)
                .orElse(0L);
    }
}
