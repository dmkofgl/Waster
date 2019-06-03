package waster.domain.entity.calendar;

import lombok.Getter;
import waster.domain.service.InterruptionService;
import waster.utuls.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Calendar implements Serializable {
    public static Date START_DATE = new Date();

    private transient InterruptionService interruptionService;

    @Getter
    private List<Schedule> schedules = new ArrayList<>();

    public Calendar(InterruptionService interruptionService) {
        this.interruptionService = interruptionService;
    }

    @Deprecated
    public void addSchedule(Schedule newSchedule) {
        schedules.add(newSchedule);
    }

    //TODO check if exist case when replace after interrupt when operation on this time already exists
    public Schedule applyOperation(Operation operation) {
        Long calculatedStartTime = calculateStartTimeForOperation(operation);
        operation.setInitialStartDate(calculatedStartTime);

        batchingOperationIfOverlap(operation);

        Schedule newTask = new Schedule(operation);
        schedules.add(newTask);

        return newTask;
    }

    //TODO DANGEROUS RECURSION
    //TODO BREAKPOINT
    private void batchingOperationIfOverlap(Operation operation) {
        List<Interruption> allOverlapInteraptions = interruptionService.getAllOverlapInteraptions(operation, START_DATE);

        allOverlapInteraptions.ifPresent(i -> {
            if(operation) {
                Long availableWorkingTime =
                        Long newLength =
                        Operation oper = Operation.builder()
                        .initialStartDate(operation.getInitialStartDate())
                        .length(operation.getLength())
                        .setting(operation.getSetting())
                        .build();
            }
            operation.setInitialStartDate(i.getEnd().getTime() - START_DATE.getTime());
        });

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
                .filter(schedule -> DateUtils.isOverlap(schedule, newTask))
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
