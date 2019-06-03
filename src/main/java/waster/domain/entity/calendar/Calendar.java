package waster.domain.entity.calendar;

import lombok.Getter;
import waster.utuls.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Calendar implements Serializable {
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

        if (isOverlapWithSaturday(operation)) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(new Date(operation.getInitialStartDate()));
            c.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
            c.set(java.util.Calendar.HOUR_OF_DAY, 8);

            operation.setInitialStartDate(c.getTime().getTime() - START_DATE.getTime());
        }
        Schedule newTask = new Schedule(operation);
        schedules.add(newTask);

        return newTask;
    }

    private boolean isOverlapWithSaturday(Operation operation) {
        Long startA = getStartSunday(operation.getInitialStartDate());
        Long endA = getEndSunday(operation.getInitialStartDate());
        Long startB = operation.getInitialStartDate() + START_DATE.getTime();
        Long endB = operation.getEndTime() + START_DATE.getTime();
        return  DateUtils.isOverlap(startA, endA, startB, endB);

    }

    private boolean isSaturday(Long time) {
        Date date = new Date(time + START_DATE.getTime());
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1;
    }

    private Long getStartSunday(Long time) {
        Date date = new Date(time + START_DATE.getTime());
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        c.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        return c.getTime().getTime();
    }

    private Long getEndSunday(Long time) {
        Date date = new Date(time + START_DATE.getTime());
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
        c.set(java.util.Calendar.HOUR_OF_DAY, 8);
        return c.getTime().getTime();
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
                .filter(schedule -> DateUtils. isOverlap(schedule, newTask))
                .findFirst();
        if (overlappedSchedule.isPresent()) {
            Schedule engaged = overlappedSchedule.get();
            newTask.setStart(engaged.getEnd() + 1);
            calculateInitialStartTime(newTask);
        }
    }

    //TODO WTF???



    public Long lastActionEndTime() {
        return schedules.stream()
                .map(Schedule::getEnd)
                .max(Long::compareTo)
                .orElse(0L);
    }
}
